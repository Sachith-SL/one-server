package com.sachith.one_server.dto;

import org.springframework.stereotype.Component;

@Component
public class DepartmentRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
