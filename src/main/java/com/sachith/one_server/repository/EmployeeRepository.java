package com.sachith.one_server.repository;

import com.sachith.one_server.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	// Search employees by name (case-insensitive, partial match)
	Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
