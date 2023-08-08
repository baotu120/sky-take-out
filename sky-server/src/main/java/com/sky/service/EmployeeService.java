package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * Emp login
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * Add Emp
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * Employee Paging Query
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * Enable/Disable account
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);

    /**
     * according id query employee
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * update employee
     * @param employeeDTO
     * @return
     */

    void update(EmployeeDTO employeeDTO);
}
