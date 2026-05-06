package com.employeemanager.crudtask.controller;

import com.employeemanager.crudtask.dto.request.EmployeeRequest;
import com.employeemanager.crudtask.dto.response.EmployeeResponse;
import com.employeemanager.crudtask.service.service.EmployeeService;

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
    public EmployeeResponse create(@Valid @RequestBody EmployeeRequest dto) {
        return employeeService.create(dto);
    }
    //Update an Employee
    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id,
                                   @Valid @RequestBody EmployeeRequest dto) {
        return employeeService.update(id, dto);
    }
    // Get all employee using pagination
    @GetMapping
    public Page<EmployeeResponse> getAll(
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
    public EmployeeResponse getById(@PathVariable Long id) {
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