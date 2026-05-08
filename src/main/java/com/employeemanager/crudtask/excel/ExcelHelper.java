package com.employeemanager.crudtask.excel;

import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {

    //Validating Excel Format "To check if the uploaded file is an excel file or not"

    public static boolean isExcelFile(MultipartFile file) {
        return file.getContentType() != null &&
                file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
