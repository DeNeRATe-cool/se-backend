package com.se.utils;


import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelUtils {

    public static List<List<String>> readFile(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename().toLowerCase();  // ✅ 改为获取原始文件名

        if (name.endsWith(".csv")) {
            return readCSV(file.getInputStream(), "GBK"); // 或 "UTF-8"
        } else if (name.endsWith(".xlsx")) {
            return readExcel(file.getInputStream(), true);
        } else if (name.endsWith(".xls")) {
            return readExcel(file.getInputStream(), false);
        } else {
            throw new IllegalArgumentException("Unsupported file type.");
        }
    }

    private static List<List<String>> readCSV(InputStream inputStream, String encoding) throws Exception {
        List<List<String>> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(inputStream, encoding))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(Arrays.asList(line));
            }
        }
        return data;
    }

    private static List<List<String>> readExcel(InputStream inputStream, boolean isXlsx) throws Exception {
        List<List<String>> data = new ArrayList<>();
        Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                cell.setCellType(CellType.STRING); // 强制为字符串
                rowData.add(cell.getStringCellValue());
            }
            data.add(rowData);
        }
        workbook.close();
        return data;
    }


    public static void main(String[] args)throws Exception {
//        File file = new File("src/main/resources/csv-example/test.csv");  // 也可以是 .xls 或 .xlsx
//        File file = new File("src/main/resources/csv-example/import.xlsx");
//        System.out.println(file.getAbsolutePath());
//        List<List<String>> content = ExcelUtils.readFile(file);
//
//        for (List<String> row : content) {
//            System.out.println(row);
//        }
    }

}
