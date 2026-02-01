package com.sachith.one_server.service;

import com.sachith.one_server.dto.DepartmentRequest;
import com.sachith.one_server.dto.DepartmentResponse;
import com.sachith.one_server.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable);

    public DepartmentResponse getDepartmentById(Integer id);

    public DepartmentResponse createDepartment(DepartmentRequest request);

}
