package com.employeemanager.crudtask.service;

import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.repository.EmployeeRepository;
import com.employeemanager.crudtask.service.EmployeeService;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImplementation implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        return null;
    }

    @Override
    public Employee getById(Long id) {
        return null;
    }

    @Override
    public List<Employee> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}


