package com.sachith.one_server.service;

import com.sachith.one_server.dto.EmployeeRequest;
import com.sachith.one_server.dto.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    public Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    // Search employees by name (case-insensitive, partial match)
    public Page<EmployeeResponse> searchEmployeesByName(String name, Pageable pageable);

    public EmployeeResponse getEmployeeById(Integer id);

    public EmployeeResponse createEmployee(EmployeeRequest request);

    public EmployeeResponse updateEmployee(Integer id, EmployeeResponse request);

    public void deleteEmployee(Integer id);
}