package org.example.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.config.ConfigLoader;
import org.example.dao.UserDAO;
import org.example.dto.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExcelUserWriter {

    private final String SHEET_NAME = ConfigLoader.get("excel.sheetName");
    private final String FILE_PATH = ConfigLoader.get("excel.filePath");
    private final String FILE_NAME = ConfigLoader.get("excel.fileName");
    private final String FULL_NAME = FILE_PATH + FILE_NAME;
    private final String[] COLUMNS = {"user_id", "user_f_name", "user_l_name", "user_b_date", "user_city", "user_contacts"};
    private final String DATE_FORMAT = "dd.MM.yyyy";

    private final UserDAO userDAO;

    public ExcelUserWriter(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public void writeUsersToExcel() throws InterruptedException {
        log.info("Начинаю эскпорт данных в excel файл");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));

            Row headerRow = sheet.createRow(0);


            for (int i = 0; i < COLUMNS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNS[i]);

                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            unloadingDataFromDB(sheet, cellStyle);

            for (int i = 0; i < COLUMNS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(FULL_NAME)) {
                workbook.write(fileOut);
            }

            log.info("Данные по user_info успещно записаны в excel файл: {}", FULL_NAME);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void unloadingDataFromDB(Sheet sheet, CellStyle cellStyle) throws InterruptedException {
        long usersCount = userDAO.count();
        int pageSize = 500;
        int nThreads = 100;
        long tasksCount = usersCount / pageSize + 1;

        CountDownLatch latch = new CountDownLatch((int) tasksCount);
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        for (int i = 1; i <= tasksCount; i++) {
            int page = i;
            executor.submit(() -> {
                synchronized (latch) {
                    int rowNum = pageSize * (page - 1) + 1;
                    List<User> userList = userDAO.getUsersPageable(page, pageSize);
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
