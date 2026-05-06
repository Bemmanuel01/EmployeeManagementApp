package com.employeemanager.crudtask.controller;

import com.employeemanager.crudtask.dto.EmployeeRequestDto;
import com.employeemanager.crudtask.dto.EmployeeResponseDto;
import com.employeemanager.crudtask.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    //Employee Creation
    @PostMapping
    public EmployeeResponseDto create(@Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.create(dto);
    }
    //Update an Employee
    @PutMapping("/{id}")
    public EmployeeResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.update(id, dto);
    }
    // Get all employee using pagination
    @GetMapping
    public Page<EmployeeResponseDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean active
    ) {
        return employeeService.getAll(page, size, sort, department, active);
    }
    // GET BY ID
    @GetMapping("/{id}")
    public EmployeeResponseDto getById(@PathVariable Long id) {
        return employeeService.getById(id);
    }
    // SOFT DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }

    // HARD DELETE
    @DeleteMapping("/{id}/hard")
    public void hardDelete(@PathVariable Long id) {
        employeeService.hardDelete(id);
    }
}