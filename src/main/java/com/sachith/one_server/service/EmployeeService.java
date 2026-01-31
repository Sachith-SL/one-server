package com.sachith.one_server.service;

import com.sachith.one_server.dto.EmployeeRequest;
import com.sachith.one_server.model.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> getAllEmployees();
    public Employee getEmployeeById(Integer id);
    public Employee createEmployee(EmployeeRequest request);
    public Employee updateEmployee(Integer id, Employee employee);
    public void deleteEmployee(Integer id);
}