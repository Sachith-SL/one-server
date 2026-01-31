package com.sachith.one_server.controller;


import com.sachith.one_server.dto.BaseResponse;
import com.sachith.one_server.dto.EmployeeRequest;
import com.sachith.one_server.model.Employee;
import com.sachith.one_server.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*")
public class EmployeeController {

    public EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // get all employees
    @GetMapping
    public ResponseEntity<BaseResponse<List<Employee>>> getAllEmployees() {
        List<Employee> list = service.getAllEmployees();
        return ResponseEntity.ok(BaseResponse.success("Fetched employees", list));
    }

    // get employee by id
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Employee>> getEmployeeById(@PathVariable Integer id) {
        Employee e = service.getEmployeeById(id);
        return ResponseEntity.ok(BaseResponse.success(e));
    }

    // create new employee
    @PostMapping
    public ResponseEntity<BaseResponse<Employee>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee created = service.createEmployee(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.success("Employee created", created));
    }

    // update employee
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Employee>> updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        Employee updated = service.updateEmployee(id, employee);
        return ResponseEntity.ok(BaseResponse.success("Employee updated", updated));
    }

    // delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteEmployee(@PathVariable Integer id) {
        service.deleteEmployee(id);
        return ResponseEntity.ok(BaseResponse.success("Employee deleted", null));
    }
}
