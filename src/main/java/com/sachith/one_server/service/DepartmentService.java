package com.sachith.one_server.service;

import com.sachith.one_server.dto.DepartmentRequest;
import com.sachith.one_server.model.Department;

import java.util.List;

public interface DepartmentService {
    public List<Department> getAllDepartments();

    public Department getDepartmentById(Integer id);

    public Department createDepartment(DepartmentRequest request);

    public Department updateDepartment(Integer id, Department department);

    public void deleteDepartment(Integer id);

}
