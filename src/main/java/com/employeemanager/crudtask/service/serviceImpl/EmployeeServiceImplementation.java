package com.employeemanager.crudtask.service.serviceImpl;

import com.employeemanager.crudtask.dto.EmployeeRequestDto;
import com.employeemanager.crudtask.dto.EmployeeResponseDto;
import com.employeemanager.crudtask.entity.Employee;
import com.employeemanager.crudtask.exception.DuplicateEmailException;
import com.employeemanager.crudtask.exception.EmployeeNotFoundException;
import com.employeemanager.crudtask.mapper.EmployeeMapper;
import com.employeemanager.crudtask.repository.EmployeeRepository;
import com.employeemanager.crudtask.service.EmployeeService;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // VALIDATE EMAIL
    private void validateEmail(String email, Long id) {

        employeeRepository.findByEmail(email).ifPresent(existing -> {
            if (id == null || !existing.getId().equals(id)) {
                throw new DuplicateEmailException("Email already exists: " + email);
            }
        });
    }

    // VALIDATE SALARY

    private void validateSalary(Employee employee) {

        String dept = employee.getDepartment().toLowerCase();
        BigDecimal salary = employee.getSalary();

        if (dept.contains("intern")) {
            if (salary.compareTo(new BigDecimal("15000")) < 0) {
                throw new IllegalArgumentException("Intern salary must be at least 15000");
            }
        } else {
            if (salary.compareTo(new BigDecimal("30000")) < 0) {
                throw new IllegalArgumentException("Minimum salary is 30000");
            }
        }
    }

    // CREATE
    @Override
    public EmployeeResponseDto create(EmployeeRequestDto dto) {

        Employee employee = EmployeeMapper.toEntity(dto);

        validateEmail(employee.getEmail(), null);
        validateSalary(employee);

        Employee saved = employeeRepository.save(employee);

        return EmployeeMapper.toResponse(saved);
    }

    // UPDATE
    @Override
    public EmployeeResponseDto update(Long id, EmployeeRequestDto dto) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        validateEmail(dto.getEmail(), id);

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        existing.setSalary(dto.getSalary());
        existing.setDateOfJoining(dto.getDateOfJoining());
        existing.setActive(dto.getActive());

        validateSalary(existing);

        Employee updated = employeeRepository.save(existing);

        return EmployeeMapper.toResponse(updated);
    }

    // GET BY ID

    @Override
    public EmployeeResponseDto getById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Not found"));

        return EmployeeMapper.toResponse(employee);
    }

    // GET ALL (PAGINATION)

    @Override
    public Page<EmployeeResponseDto> getAll(int page, int size, String sort,
                                            String department, Boolean active) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<Employee> employees;

        if (department != null) {
            employees = employeeRepository.findByDepartment(department, pageable);
        } else if (active != null) {
            employees = employeeRepository.findByActive(active, pageable);
        } else {
            employees = employeeRepository.findAll(pageable);
        }

        return employees.map(EmployeeMapper::toResponse);
    }

    // DELETE (SOFT)
    @Override
    public void delete(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        employee.setActive(false);

        employeeRepository.save(employee);
    }

    // HARD DELETE

    public void hardDelete(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        if (Boolean.TRUE.equals(employee.getActive())) {
            throw new IllegalStateException("Cannot hard delete active employee");
        }

        employeeRepository.delete(employee);
    }
}