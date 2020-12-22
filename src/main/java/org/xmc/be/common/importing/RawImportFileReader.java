package org.xmc.be.common.importing;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportPreparationController;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;

import java.io.IOException;
import java.util.List;

@Component
public class RawImportFileReader {
	private final RawImportCsvFileReader rawImportCsvFileReader;
	private final RawImportExcelFileReader rawImportExcelFileReader;
	
	@Autowired
	public RawImportFileReader(
			RawImportCsvFileReader rawImportCsvFileReader,
			RawImportExcelFileReader rawImportExcelFileReader) {
		
		this.rawImportCsvFileReader = rawImportCsvFileReader;
		this.rawImportExcelFileReader = rawImportExcelFileReader;
	}
	
	public List<List<String>> read(
			DtoImportData<CashAccountTransactionImportColmn> importData,
			String contentType) throws IOException, CsvValidationException {
		
		if (CashAccountTransactionImportPreparationController.VALID_CSV_MIME_TYPES.contains(contentType)) {
			return rawImportCsvFileReader.read(importData.getFileToImport(), importData.getStartWithLine(), importData.getCsvSeparator());
		} else if (CashAccountTransactionImportPreparationController.VALID_EXCEL_MIME_TYPES.contains(contentType)) {
			return rawImportExcelFileReader.read(importData.getFileToImport(), importData.getStartWithLine());
		} else {
			String message = String.format("Cannot import invalid file type: %s", contentType);
			throw new IllegalArgumentException(message);
		}
	}
}
