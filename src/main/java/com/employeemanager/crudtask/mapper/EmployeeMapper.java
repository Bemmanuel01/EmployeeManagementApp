package com.employeemanager.crudtask.mapper;

import com.employeemanager.crudtask.dto.request.EmployeeRequest;
import com.employeemanager.crudtask.dto.response.EmployeeResponse;
import com.employeemanager.crudtask.entity.Employee;

public class EmployeeMapper {

    // ==========================
    // DTO → ENTITY
    // ==========================
    public static Employee toEntity(EmployeeRequest dto) {

        Employee employee = new Employee();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setActive(dto.getActive());

        return employee;
    }
    // Mapping ENTITY → DTO

    public static EmployeeResponse toResponse(Employee employee) {

        EmployeeResponse dto = new EmployeeResponse();

        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setSalary(employee.getSalary());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setActive(employee.getActive());

        // audit fields
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        return dto;
    }
}