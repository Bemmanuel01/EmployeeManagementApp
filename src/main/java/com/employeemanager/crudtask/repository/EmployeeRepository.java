package com.employeemanager.crudtask.repository;

import com.employeemanager.crudtask.entity.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employee by email
    Optional<Employee> findByEmail(String email);

    // Pagination + filtering by department
    Page<Employee> findByDepartment(String department, Pageable pageable);

    // Pagination + filtering by active status
    Page<Employee> findByActive(Boolean active, Pageable pageable);
}