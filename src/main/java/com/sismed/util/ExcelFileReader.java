package com.sismed.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sismed.dto.ExcelField;
import com.sismed.util.enums.FieldType;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExcelFileReader {

	final static SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");

	private ExcelFileReader() {
	}

	public static Workbook readExcel(final String fullFilePath)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(new File(fullFilePath));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return wb;
	}

	public static Map<String, List<ExcelField[]>> getExcelRowValues(final Sheet sheet) {
		Map<String, List<ExcelField[]>> excelMap = new HashMap<>();
		Map<String, ExcelField[]> excelSectionHeaders = getExcelHeaderSections();
		int totalRows = sheet.getLastRowNum();
		excelSectionHeaders.forEach((section, excelFields) -> {
			List<ExcelField[]> excelFieldList = new ArrayList<>();
			for (int i = 1; i <= totalRows; i++) {
				Row row = sheet.getRow(i);
				ExcelField[] excelFieldArr = new ExcelField[excelFields.length];
				int k = 0;
				for (ExcelField ehc : excelFields) {
					int cellIndex = ehc.getExcelIndex();
					String cellType = ehc.getExcelColType();
					Cell cell = row.getCell(cellIndex);
					ExcelField excelField = new ExcelField();
					excelField.setExcelColType(ehc.getExcelColType());
					excelField.setExcelHeader(ehc.getExcelHeader());
					excelField.setExcelIndex(ehc.getExcelIndex());
					excelField.setPojoAttribute(ehc.getPojoAttribute());
					if (cell != null && !cell.equals("")) {
						loadCell(cellType, cell, excelField);
					} else {
						excelField.setExcelValue(null);
					}
					excelFieldArr[k++] = excelField;
				}
				excelFieldList.add(excelFieldArr);
			}
			excelMap.put(section, excelFieldList);
		});
		return excelMap;
	}

	private static void loadCell(String cellType, Cell cell, ExcelField excelField) {
		try {
			if (FieldType.STRING.getValue().equalsIgnoreCase(cellType)) {
				DataFormatter formatter = new DataFormatter();
				excelField.setExcelValue(formatter.formatCellValue(cell));
			} else if (FieldType.DOUBLE.getValue().equalsIgnoreCase(cellType)
					|| FieldType.INTEGER.getValue().equalsIgnoreCase(cellType)) {
				excelField.setExcelValue(String.valueOf(cell.getNumericCellValue()));
			} else if (DateUtil.isCellDateFormatted(cell)) {
				if (cell.getDateCellValue() != null) {
					excelField.setExcelValue(dtf.format(cell.getDateCellValue()));
				} else {
					excelField.setExcelValue(null);
				}
			}
		} catch (IllegalStateException e) {
			excelField.setExcelValue(null);
		}
	}

	private static Map<String, ExcelField[]> getExcelHeaderSections() {
		List<Map<String, List<ExcelField>>> jsonConfigMap = getExcelHeaderFieldSections();
		Map<String, ExcelField[]> jsonMap = new HashMap<>();
		jsonConfigMap.forEach(jps -> {
			jps.forEach((section, values) -> {
				ExcelField[] excelFields = new ExcelField[values.size()];
				jsonMap.put(section, values.toArray(excelFields));
			});
		});
		return jsonMap;
	}

	private static List<Map<String, List<ExcelField>>> getExcelHeaderFieldSections() {
		List<Map<String, List<ExcelField>>> jsonMap = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonConfig = new String(
					Files.readAllBytes(Paths.get(ResourceUtils.getFile("classpath:json/excel.json").toURI())));

			jsonMap = objectMapper.readValue(jsonConfig, new TypeReference<>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonMap;
	}

}
