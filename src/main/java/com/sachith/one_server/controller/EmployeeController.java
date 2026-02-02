package com.sachith.one_server.controller;


import com.sachith.one_server.dto.BaseResponse;
import com.sachith.one_server.dto.EmployeeRequest;
import com.sachith.one_server.dto.EmployeeResponse;
import com.sachith.one_server.model.Employee;
import com.sachith.one_server.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/one/v1/employee")
public class EmployeeController {

    public EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // get all employees
    @GetMapping
    public ResponseEntity<BaseResponse<Page<EmployeeResponse>>> getAllEmployees(Pageable pageable) {
        Page<EmployeeResponse> employeeResponsePage = service.getAllEmployees(pageable);
        return ResponseEntity.ok(BaseResponse.success("Fetched employees", employeeResponsePage));
    }

    // get employee by id
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EmployeeResponse>> getEmployeeById(@PathVariable Integer id) {
        EmployeeResponse response = service.getEmployeeById(id);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    // create new employee
    @PostMapping
    public ResponseEntity<BaseResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse created = service.createEmployee(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success("Employee created", created));
    }

    // update employee
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<EmployeeResponse>> updateEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeResponse request) {
        EmployeeResponse updated = service.updateEmployee(id, request);
        return ResponseEntity.ok(BaseResponse.success("Employee updated", updated));
    }

    // delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteEmployee(@PathVariable Integer id) {
        service.deleteEmployee(id);
        return ResponseEntity.ok(BaseResponse.success("Employee deleted", null));
    }
}
