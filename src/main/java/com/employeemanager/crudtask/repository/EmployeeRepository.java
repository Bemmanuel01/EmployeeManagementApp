package com.employeemanager.crudtask.repository;

import com.employeemanager.crudtask.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    static <T> ScopedValue<T> findById(Long id) {
        return null;
    }

    Optional<Object> findByEmail(String email);
}


