package com.employeemanager.crudtask.service;

import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.exception.DuplicateEmailException;
import com.employeemanager.crudtask.exception.EmployeeNotFoundException;
import com.employeemanager.crudtask.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImplementation implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private void validateEmail(String email, Long id) {
        employeeRepository.findByEmail(email).ifPresent(existing -> {
            throw new DuplicateEmailException("Email already exists: " + email);
        });
    }

    @Override
    public Employee create(Employee employee) {
        validateEmail(employee.getEmail(), null);
        return employeeRepository.save(employee);
    }


    @Override
    public Employee update(Long id, Employee employee) {
        Employee existing = getById(id);
        validateEmail(employee.getEmail(), id);
        existing.setEmail(employee.getEmail());
        return employeeRepository.save(existing);
    }

    @Override
    public Employee getById(Long id) {
        assert EmployeeRepository.findById(id) != null;
        return (Employee) EmployeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public List<Employee> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}


