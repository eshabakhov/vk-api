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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@AllArgsConstructor
public class ExcelUserWriter {
    private final String[] COLUMNS = {"user_id", "user_f_name", "user_l_name", "user_b_date", "user_city", "user_contacts"};
    private final String DATE_FORMAT = "dd.MM.yyyy";

    private final UserRepository userRepository;

    private final ExcelProperties excelProperties;

    public ByteArrayOutputStream writeUsersToByteArrayOutputStream() throws InterruptedException {
        log.info("Start export in byte array stream");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(excelProperties.getSheetName());

            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));

            Row headerRow = sheet.createRow(0);
            String[] columns = COLUMNS;

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);

                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            unloadingDataFromDB(sheet, cellStyle);


            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            log.info("Data from user_info uploaded successfully into byte array");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return outputStream;
    }

    private void unloadingDataFromDB(Sheet sheet, CellStyle cellStyle) throws InterruptedException {
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
                        Cell cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(user.getUserBirthdayDate());
                        row.createCell(4).setCellValue(user.getUserCity());
                        row.createCell(5).setCellValue(user.getUserContacts());
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
