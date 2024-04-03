package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WriteData {
    public static void writeDataToExcel(List<String> data,List<String> columnNames,String fileName) throws IOException {
        File file = new File(fileName);
        Workbook workbook;

        if (file.exists()) {
            workbook = WorkbookFactory.create(new FileInputStream(file));
        } else {
            workbook = new XSSFWorkbook();
        }

        Sheet sheet = workbook.getSheet(data.get(0));

        if (sheet == null) {
            sheet = workbook.createSheet(data.get(0));

            // Write column names
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i < columnNames.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNames.get(i));
            }
        }

        int rowNum = sheet.getLastRowNum() + 1;

        // Create a new row
        Row row = sheet.createRow(rowNum);

        // Write data to the row
        for (int i = 1; i < data.size(); i++) {
            Cell cell = row.createCell(i);
            System.out.print(data.get(i) + " ");
            cell.setCellValue(data.get(i));
        }

        // Write the workbook to a file
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        }
    }
}
