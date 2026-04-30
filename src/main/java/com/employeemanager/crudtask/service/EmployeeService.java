package com.employeemanager.crudtask.service;

import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.repository.EmployeeRepository;
import com.employeemanager.crudtask.service.EmployeeService;
import com.employeemanager.crudtask.exception.EmployeeNotFoundException;
import com.employeemanager.crudtask.exception.DuplicateEmailException;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

//The services available on the employee manager's app
@Service
@Validated
public interface EmployeeService{
    Employee create(Employee employee);

    Employee update(Long id, Employee employee);

    Employee getById(Long id);

    List<Employee> getAll();
    void delete(Long id);
}
