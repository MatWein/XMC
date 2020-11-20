package org.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class RawImportExcelFileReader {
	public List<List<String>> read(File fileToImport, int startWithLine) throws IOException {
		Workbook workbook = WorkbookFactory.create(fileToImport);
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		
		List<List<String>> lines = Lists.newArrayListWithExpectedSize(sheet.getLastRowNum() + 1);
		
		for (int rowIndex = startWithLine - 1; rowIndex < sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			
			List<String> columValues = Lists.newArrayListWithExpectedSize(row.getLastCellNum() + 1);
			
			short firstCellIndex = row.getFirstCellNum();
			short lastCellIndex = row.getLastCellNum();
			for (short columnIndex = firstCellIndex; columnIndex < lastCellIndex; columnIndex++) {
				Cell cell = row.getCell(columnIndex);
				if (cell == null) {
					continue;
				}
				
				columValues.add(formatter.formatCellValue(cell));
			}
			
			lines.add(columValues);
		}
		
		return lines;
	}
}
