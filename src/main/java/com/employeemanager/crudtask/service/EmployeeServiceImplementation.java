package com.employeemanager.crudtask.service;

import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.exception.DuplicateEmailException;
import com.employeemanager.crudtask.exception.EmployeeNotFoundException;
import com.employeemanager.crudtask.repository.EmployeeRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class EmployeeServiceImplementation implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    private void validateEmail(String email, Long id) {
        employeeRepository.findByEmail(email).ifPresent(existing -> {
            throw new DuplicateEmailException(STR."Email already exists: \{email}");
        });
    }
    // Validating the Salary Rule
    public void validateSalary(Employee employee) {
        if (employee.getDepartment() == null) {
            throw new EmployeeNotFoundException("Department not found");
        }
        String dept =  employee.getDepartment().toLowerCase();
        BigDecimal  salary = employee.getSalary();

        //Intern Rule
        if (dept.contains("intern")){
            if (salary.compareTo(new BigDecimal("15000")) <= 0) {
                throw new IllegalArgumentException("Intern salary must be at least 15000");
            }
        }
        //Other department Minimum Salary is 30000
        else {
            if (salary.compareTo(new BigDecimal("30000")) <= 0) {
                throw new IllegalArgumentException("Minimum Salary is 30000");
            }
        }
    }

    @Override
    public Employee create(Employee employee) {
        validateEmail(employee.getEmail(), null);
        validateSalary(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id, Employee employee) {
        Employee existing = getById(id);
        validateEmail(employee.getEmail(), id);
        validateSalary(employee);
        existing.setFirstName(employee.getFirstName());
        existing.setLastName(employee.getLastName());
        existing.setEmail(employee.getEmail());
        existing.setSalary(employee.getSalary());
        existing.setDepartment(employee.getDepartment());
        existing.setDateOfJoining(employee.getDateOfJoining());
        existing.setActive(employee.getActive());

        return employeeRepository.save(existing);
    }

    @Override
    public Employee getById(Long id) {
        assert EmployeeRepository.findById(id) != null;
        return (Employee) EmployeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(STR."Employee not found with id: \{id}"));
    }

    @Override
    public List<Employee> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}


