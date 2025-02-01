package org.test.vkapi.excel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.test.vkapi.config.ExcelProperties;
import org.test.vkapi.dto.User;
import org.test.vkapi.repository.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@AllArgsConstructor
public class ExcelUserWriter {

    private final UserRepository userRepository;

    private final ExcelProperties excelProperties;

    public void writeUsersToExcel() throws InterruptedException {
        try (Workbook workbook = new XSSFWorkbook()) {
            log.info("Началась загрузка в файл");
            Sheet sheet = workbook.createSheet(excelProperties.getSheetName());

            Row headerRow = sheet.createRow(0);
            String[] columns = {"user_id", "user_f_name", "user_l_name", "user_b_date", "user_city", "user_contacts"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);

                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            long usersCount = userRepository.count();
            int pageSize = 500;
            int nThreads = 100;
            long tasksCount = usersCount / pageSize + 1;

            CountDownLatch latch = new CountDownLatch((int) tasksCount);
            ExecutorService executor = Executors.newFixedThreadPool(nThreads);

            for (int i = 1; i <= tasksCount; i++) {
                int page = i;
                Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(pageSize));
                executor.submit(() -> {
                    synchronized (latch) {
                        int rowNum = pageSize * (page - 1) + 1;
                        List<User> userList = userRepository.findAll(pageable).stream().toList();
                        for (User user : userList) {
                            Row row = sheet.createRow(rowNum++);
                            row.createCell(0).setCellValue(user.getUserId());
                            row.createCell(1).setCellValue(user.getUserFirstName());
                            row.createCell(2).setCellValue(user.getUserLastName());
                            row.createCell(3).setCellValue(user.getUserBirthdayDate());
                            row.createCell(4).setCellValue(user.getUserCity());
                            row.createCell(5).setCellValue(user.getUserContacts());
                        }
                    }
                    latch.countDown();
                });
            }
            latch.await();


            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelProperties.getFilePath() + excelProperties.getFileName())) {
                workbook.write(fileOut);
            }

            log.info("Excel-файл успешно создан: {}", excelProperties.getFilePath() + excelProperties.getFileName());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
