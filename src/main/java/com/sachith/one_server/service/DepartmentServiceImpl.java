package com.sachith.one_server.service;

import com.sachith.one_server.dto.DepartmentRequest;
import com.sachith.one_server.dto.DepartmentResponse;
import com.sachith.one_server.exception.ResourceNotFoundException;
import com.sachith.one_server.model.Department;
import com.sachith.one_server.repository.DepartmentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable)
                .map(dept -> new DepartmentResponse(
                        dept.getId(),
                        dept.getName()
                ));
    }

    @Override
    public DepartmentResponse getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Department not found"));
        return new DepartmentResponse(
                department.getId(),
                department.getName()
        );
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        Department createdDepartment = departmentRepository.save(department);
        return new DepartmentResponse(
                createdDepartment.getId(),
                createdDepartment.getName()
        );
    }

}
