package com.employeemanager.crudtask.service.serviceImpl;

import com.employeemanager.crudtask.dto.request.EmployeeRequest;
import com.employeemanager.crudtask.dto.response.EmployeeResponse;
import com.employeemanager.crudtask.entity.Employee;

import com.employeemanager.crudtask.exception.DuplicateEmailException;
import com.employeemanager.crudtask.exception.EmployeeNotFoundException;

import com.employeemanager.crudtask.exception.ExcelProcessingException;
import com.employeemanager.crudtask.mapper.EmployeeMapper;
import com.employeemanager.crudtask.repository.EmployeeRepository;
import com.employeemanager.crudtask.service.service.EmployeeService;

import com.employeemanager.crudtask.excel.ExcelHelper;
import com.employeemanager.crudtask.exception.InvalidFileFormatException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // VALIDATE EMAIL
    private void validateEmail(String email, Long id) {

        employeeRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
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
    public EmployeeResponse create(EmployeeRequest dto) {

        Employee employee = EmployeeMapper.toEntity(dto);

        validateEmail(employee.getEmail(), null);
        validateSalary(employee);

        Employee saved = employeeRepository.save(employee);

        return EmployeeMapper.toResponse(saved);
    }

    // UPDATE
    @Override
    public EmployeeResponse update(Long id, EmployeeRequest dto) {

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
    public EmployeeResponse getById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Not found"));

        return EmployeeMapper.toResponse(employee);
    }

    // GET ALL (PAGINATION)

    @Override
    public Page<EmployeeResponse> getAll(int page, int size, String sort,
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

    @Override
    public void importExcel(MultipartFile file){

        if (!ExcelHelper.isExcelFile(file)) {
            throw new InvalidFileFormatException("Invalid file format");
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            //Skip Header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                Employee employee = new Employee();

                employee.setFirstName(row.getCell(0).getStringCellValue());
                employee.setLastName(row.getCell(1).getStringCellValue());
                employee.setEmail(row.getCell(2).getStringCellValue());
                employee.setDepartment(row.getCell(3).getStringCellValue());

                employee.setSalary(
                        BigDecimal.valueOf(
                                row.getCell(4).getNumericCellValue()
                        )
                );
                employee.setDateOfJoining(
                        LocalDate.parse(
                                row.getCell(5).getStringCellValue()
                        )
                );
                employee.setActive(row.getCell(6).getBooleanCellValue());

                //Validations
                validateEmail(employee.getEmail(), null);
                validateSalary(employee);

                employeeRepository.save(employee);
            }
        } catch (Exception e) {
            throw new ExcelProcessingException("Failed to process Excel file");
        }
    }

}