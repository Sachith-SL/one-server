package com.sachith.one_server.service;

import com.sachith.one_server.dto.EmployeeRequest;
import com.sachith.one_server.dto.EmployeeResponse;
import com.sachith.one_server.exception.ResourceNotFoundException;
import com.sachith.one_server.model.Department;
import com.sachith.one_server.model.Employee;
import com.sachith.one_server.repository.DepartmentRepository;
import com.sachith.one_server.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeServiceImpl using Mockito.
 *
 * WHY this is the most important service-layer test:
 * ─────────────────────────────────────────────────────
 * EmployeeServiceImpl is the core business service — it has the most logic:
 * • Mapping between Entity ↔ DTO
 * • Cross-repository interaction (Employee + Department)
 * • Throwing ResourceNotFoundException on missing data
 *
 * Testing with Mockito lets us verify this logic IN ISOLATION,
 * without a database, without Spring context, so tests run in milliseconds.
 */
@ExtendWith(MockitoExtension.class) // ← enables @Mock / @InjectMocks
class EmployeeServiceImplTest {

    @Mock // ← fake repository (no real DB)
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks // ← injects the two mocks above
    private EmployeeServiceImpl employeeService;

    // ═══════════════════════════════════════════════════════════════════
    // createEmployee – happy path WITH department
    // ═══════════════════════════════════════════════════════════════════
    @Test
    void createEmployee_withDepartment_shouldReturnCorrectResponse() {
        // ── Arrange ──
        Department dept = Department.builder()
                .id(1).name("Engineering").build();

        Employee savedEmployee = Employee.builder()
                .id(10).name("Sachith").salary(75000.0)
                .department(dept).build();

        EmployeeRequest request = EmployeeRequest.builder()
                .name("Sachith").salary(75000.0)
                .departmentId(1).build();

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept));
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // ── Act ──
        EmployeeResponse response = employeeService.createEmployee(request);

        // ── Assert ──
        assertNotNull(response);
        assertEquals(10, response.getId());
        assertEquals("Sachith", response.getName());
        assertEquals(75000.0, response.getSalary());
        assertEquals(1, response.getDepartmentId());

        verify(departmentRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // ═══════════════════════════════════════════════════════════════════
    // createEmployee – happy path WITHOUT department
    // ═══════════════════════════════════════════════════════════════════
    @Test
    void createEmployee_withoutDepartment_shouldReturnResponseWithNullDeptId() {
        Employee savedEmployee = Employee.builder()
                .id(11).name("Kamal").salary(60000.0).build();

        EmployeeRequest request = EmployeeRequest.builder()
                .name("Kamal").salary(60000.0).build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertEquals("Kamal", response.getName());
        assertNull(response.getDepartmentId());
        verify(departmentRepository, never()).findById(any());
    }

    // ═══════════════════════════════════════════════════════════════════
    // createEmployee – department not found → exception
    // ═══════════════════════════════════════════════════════════════════
    @Test
    void createEmployee_withInvalidDepartment_shouldThrowResourceNotFound() {
        EmployeeRequest request = EmployeeRequest.builder()
                .name("Nimal").salary(50000.0)
                .departmentId(999).build();

        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.createEmployee(request));

        verify(employeeRepository, never()).save(any());
    }

    // ═══════════════════════════════════════════════════════════════════
    // getEmployeeById – found
    // ═══════════════════════════════════════════════════════════════════
    @Test
    void getEmployeeById_existingId_shouldReturnEmployee() {
        Department dept = Department.builder()
                .id(2).name("HR").build();

        Employee employee = Employee.builder()
                .id(5).name("Amara").salary(80000.0)
                .department(dept).build();

        when(employeeRepository.findById(5)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeById(5);

        assertEquals(5, response.getId());
        assertEquals("Amara", response.getName());
        assertEquals(2, response.getDepartmentId());
    }

    // ═══════════════════════════════════════════════════════════════════
    // getEmployeeById – NOT found → exception
    // ═══════════════════════════════════════════════════════════════════
    @Test
    void getEmployeeById_nonExistingId_shouldThrowResourceNotFound() {
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(999));

        assertTrue(ex.getMessage().contains("999"));
    }
}
