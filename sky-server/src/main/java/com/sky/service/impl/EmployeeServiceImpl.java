package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.RedisLoginConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * Employee login
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //_0 Verify if the account is locked
        validateAccountLock(username);

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误

            //_1 record pwd error flag, and set the expiration time to 5 minutes
            redisTemplate.opsForValue().set(getKey(username), "-",5, TimeUnit.MINUTES);

            //_2 get the error flag, if quantity >= 5, set the lock flag for this account
            Set<Object> keys = redisTemplate.keys(RedisLoginConstant.LOGIN_PASSWORD_ERROR_KEY + username + ":*");
            if (keys != null && keys.size() >= 5){
                log.info("Enter password incorrectly more than 5 times in 5 minutes, lock account for 1 hour");
                redisTemplate.opsForValue().set(RedisLoginConstant.LOGIN_LOCK_ERROR_KEY+username,"-",60,TimeUnit.MINUTES);
                throw new AccountLockedException(MessageConstant.LOGIN_LOCK_ERROR);
            }

            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    private void validateAccountLock(String username) {
        Object flag = redisTemplate.opsForValue().get(RedisLoginConstant.LOGIN_LOCK_ERROR_KEY + username);
        if (ObjectUtils.isNotEmpty(flag)){ //account lock
            log.info("Account is locked, login invalid");
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
    }

    //Splice redis key
    private static String getKey(String username) {
        return RedisLoginConstant.LOGIN_PASSWORD_ERROR_KEY + username + ":" + RandomStringUtils.randomAlphabetic(5);
    }

    /**
     * Add Emp
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        Long currentId = BaseContext.getCurrentId();
        //Object Property Copy
        BeanUtils.copyProperties(employeeDTO, employee);

        //Set the status of the account, default normal status (1 means normal, 0 means locked)
        employee.setStatus(StatusConstant.ENABLE);

        //Set password, default password 123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //Set the creation time and modification time
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //Set the current record creator id and modifier id
        employee.setCreateUser(currentId);//TODO (recent data is fake)
        employee.setUpdateUser(currentId);

        employeeMapper.insert(employee);//TODO further process
    }

    /**
     * Employee Paging Query
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
//        // 1. set page parameters, select * from employee limit 0,10
//
//        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
//
//        // 2. start page query
//        List<Employee> employeeList = employeeMapper.list(employeePageQueryDTO.getName());
//
//        // 3. parse and encapsulate results
//        Page<Employee> page = (Page<Employee>) employeeList;
//
//        long total = page.getTotal();
//        List<Employee> records = page.getResult();
//
//        return new PageResult(total, records);


        // select * from employee limit 0,10
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);//后续定义

        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * Enable/Disable account
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .updateTime(LocalDateTime.now())
                .status(status)
                .id(id)
                .updateUser(BaseContext.getCurrentId())
                .build();

        employeeMapper.update(employee);
    }

    /**
     * according id query employee
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);

        return employee;
    }

    /**
     * update employee
     * @param employeeDTO
     * @return
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

}
