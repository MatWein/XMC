package io.github.matwein.xmc.be.services.importing.controller;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RawImportExcelFileReader {
	public List<List<String>> read(File fileToImport, int startWithLine) throws IOException {
		try (Workbook workbook = WorkbookFactory.create(fileToImport)) {
			return readFromWorkbook(workbook, startWithLine);
		}
	}
	
	private List<List<String>> readFromWorkbook(Workbook workbook, int startWithLine) throws IOException {
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		
		List<List<String>> lines = new ArrayList<>(sheet.getLastRowNum() + 1);
		
		for (int rowIndex = startWithLine - 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			
			List<String> columValues = new ArrayList<>(row.getLastCellNum() + 1);
			
			short firstCellIndex = row.getFirstCellNum();
			short lastCellIndex = row.getLastCellNum();
			for (short columnIndex = firstCellIndex; columnIndex < lastCellIndex; columnIndex++) {
				Cell cell = row.getCell(columnIndex);
				if (cell == null) {
					columValues.add("");
					continue;
				}
				
				columValues.add(formatter.formatCellValue(cell));
			}
			
			lines.add(columValues);
		}
		
		if (lines.isEmpty()) {
			throw new IOException("Error on reading excel file.");
		}
		
		return lines;
	}
}
