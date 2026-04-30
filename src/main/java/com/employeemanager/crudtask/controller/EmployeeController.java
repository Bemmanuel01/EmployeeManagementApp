package com.employeemanager.crudtask.controller;

import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees") //The base url

public class EmployeeController {
    private final EmployeeService employeeService;

    //Constructor of the Employee Service
    public EmployeeController(EmployeeService employeeService) {

        this.employeeService = employeeService;
    }

    //Adding new employee
    @PostMapping
    public Employee save(@Valid @RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    //Updating employee records
    @PutMapping
    public Employee update(@PathVariable Long id,
                           @Valid @RequestBody Employee employee) {
        return employeeService.update(id, employee);
    }

    //Fetching all employees
    @GetMapping
    public List<Employee> findAll() {

        return employeeService.getAll();
    }

    //Fetch employee by Id
    @GetMapping("/id")
    public Employee getById(@PathVariable Long id){

        return employeeService.getById(id);
    }


}
