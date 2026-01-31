package com.sachith.one_server.service;

import com.sachith.one_server.dto.DepartmentRequest;
import com.sachith.one_server.exception.ResourceNotFoundException;
import com.sachith.one_server.model.Department;
import com.sachith.one_server.repository.DepartmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Department not found"));
    }

    @Override
    public Department createDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Integer id, Department department) {
        return null;
    }

    @Override
    public void deleteDepartment(Integer id) {

    }
}
