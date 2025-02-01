package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDAO;
import org.example.excel.ExcelUserWriter;
import org.example.service.UserService;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();
            UserService userService = new UserService(userDAO);
            ExcelUserWriter excelUserWriter = new ExcelUserWriter(userDAO);
            userService.update();
            excelUserWriter.writeUsersToExcel();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}