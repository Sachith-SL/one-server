package com.sachith.one_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sachith.one_server.model.Department;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeResponse {

    Integer id;
    String name;
    Integer departmentId;
    Double salary;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Integer id, String name, Integer departmentId, Double salary) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
