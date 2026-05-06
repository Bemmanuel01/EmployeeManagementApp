package com.employeemanager.crudtask.service;

import com.employeemanager.crudtask.dto.EmployeeRequestDto;
import com.employeemanager.crudtask.dto.EmployeeResponseDto;

import org.springframework.data.domain.Page;

public interface EmployeeService {

    // Pagination + Filtering
    Page<EmployeeResponseDto> getAll(int page, int size, String sort,
                                     String department, Boolean active);
    EmployeeResponseDto create(EmployeeRequestDto dto);
    EmployeeResponseDto update(Long id, EmployeeRequestDto dto);
    EmployeeResponseDto getById(Long id);

    // Soft delete
    void delete(Long id);
    void hardDelete(Long id);
}