package com.employeemanager.crudtask.service.service;

import com.employeemanager.crudtask.dto.request.EmployeeRequest;
import com.employeemanager.crudtask.dto.response.EmployeeResponse;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;

public interface EmployeeService {

    // Pagination + Filtering
    Page<EmployeeResponse> getAll(int page, int size, String sort,
                                  String department, Boolean active);
    EmployeeResponse create(EmployeeRequest dto);
    EmployeeResponse update(Long id, EmployeeRequest dto);
    EmployeeResponse getById(Long id);

    // Soft delete
    void delete(Long id);
    void hardDelete(Long id);
    void importExcel(MultipartFile file);
}