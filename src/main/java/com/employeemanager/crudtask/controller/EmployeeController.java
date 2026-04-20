package com.employeemanager.crudtask.controller;

import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees") //The base url

public class EmployeeController {
    private final EmployeeService employeeService;

    //Constructor of the Employee Service
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Employee Creation
    @PostMapping
    public Employee save(@RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    //Updating an employee
    @PutMapping
    public Employee update(@RequestBody Employee employee) {
        return employeeService.update(employee);
    }
}
