package com.employeemanager.crudtask.service;

import com.employeemanager.crudtask.entity.Employee;
import java.util.*;

//The services available on the employee manager's app
public interface EmployeeService{
    Employee create(Employee employee);
    Employee update(Employee employee);
    Employee getById(Long id);

    List<Employee> getAll();
    void delete(Long id);
}
