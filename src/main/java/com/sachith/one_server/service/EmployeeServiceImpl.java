package com.sachith.one_server.service;

import com.sachith.one_server.dto.EmployeeRequest;
import com.sachith.one_server.dto.EmployeeResponse;
import com.sachith.one_server.exception.ResourceNotFoundException;
import com.sachith.one_server.model.Department;
import com.sachith.one_server.model.Employee;
import com.sachith.one_server.repository.DepartmentRepository;
import com.sachith.one_server.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    public EmployeeRepository employeeRepository;
    public DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }



    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable)
                .map(emp -> new EmployeeResponse(
                        emp.getId(),
                        emp.getName(),
                        emp.getDepartment() != null ? emp.getDepartment().getId() : null,
                        emp.getSalary()
                ));
    }


    @Override
    public EmployeeResponse getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(
                        emp -> new EmployeeResponse(
                                emp.getId(),
                                emp.getName(),
                                emp.getDepartment() != null ? emp.getDepartment().getId() : null,
                                emp.getSalary()
                        )
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id)
                );
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setSalary(request.getSalary());

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Department not found"));
            employee.setDepartment(dept);
        }
        Employee updatedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(
                updatedEmployee.getId(),
                updatedEmployee.getName(),
                updatedEmployee.getDepartment() != null ? updatedEmployee.getDepartment().getId() : null,
                updatedEmployee.getSalary()
        );
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Integer id, EmployeeResponse request) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id)
                );

        existingEmployee.setName(request.getName());
        existingEmployee.setSalary(request.getSalary());
        if(request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Department not found"));
            existingEmployee.setDepartment(dept);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return new EmployeeResponse(
                updatedEmployee.getId(),
                updatedEmployee.getName(),
                updatedEmployee.getDepartment() != null ? updatedEmployee.getDepartment().getId() : null,
                updatedEmployee.getSalary()
        );
    }

    @Override
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

}
