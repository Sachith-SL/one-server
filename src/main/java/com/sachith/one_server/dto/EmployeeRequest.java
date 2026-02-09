package com.sachith.one_server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double salary;

    private Integer departmentId;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotNull Double getSalary() {
        return salary;
    }

    public void setSalary(@NotNull Double salary) {
        this.salary = salary;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    // ── Builder Pattern ─────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Double salary;
        private Integer departmentId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder salary(Double salary) {
            this.salary = salary;
            return this;
        }

        public Builder departmentId(Integer deptId) {
            this.departmentId = deptId;
            return this;
        }

        public EmployeeRequest build() {
            EmployeeRequest r = new EmployeeRequest();
            r.setName(name);
            r.setSalary(salary);
            r.setDepartmentId(departmentId);
            return r;
        }
    }
}
