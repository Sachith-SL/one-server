package com.sachith.one_server.controller;


import com.sachith.one_server.dto.BaseResponse;
import com.sachith.one_server.dto.DepartmentRequest;
import com.sachith.one_server.dto.DepartmentResponse;
import com.sachith.one_server.model.Department;
import com.sachith.one_server.repository.DepartmentRepository;
import com.sachith.one_server.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/one/v1/department")
public class DepartmentController {

    private DepartmentService service;

    private DepartmentRepository repository;

    public DepartmentController(DepartmentService service, DepartmentRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    // get all departments
    @GetMapping
    public ResponseEntity<BaseResponse<Page<DepartmentResponse>>> getAllDepartments(Pageable pageable) {
        Page<DepartmentResponse> page = service.getAllDepartments(pageable);
        return ResponseEntity.ok(BaseResponse.success("Fetched departments", page));
    }

    // get department by id
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DepartmentResponse>> getDepartmentById(@PathVariable Integer id) {
        DepartmentResponse departmentResponse = service.getDepartmentById(id);
        return ResponseEntity.ok(BaseResponse.success(departmentResponse));
    }

    // create new department
    @PostMapping
    public ResponseEntity<BaseResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse created = service.createDepartment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success("Department created", created));
    }

    //this is some experiment I will use get method to save department with request body
    @GetMapping("/create")
    public ResponseEntity<BaseResponse<Department>> createDepartmentGet(@RequestBody DepartmentRequest request) {
        Department newDepartment = new Department();
        newDepartment.setName(request.getName());
        repository.save(newDepartment);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success("Department created via GET", newDepartment));
    }

}
