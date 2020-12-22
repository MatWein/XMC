package org.xmc.be.services.importing.controller;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportPreparationController;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RawImportFileReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(RawImportFileReader.class);
	
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
			Charset charset = findCharsetOrDefault(importData.getEncoding());
			return rawImportCsvFileReader.read(importData.getFileToImport(), importData.getStartWithLine(), importData.getCsvSeparator(), charset);
		} else if (CashAccountTransactionImportPreparationController.VALID_EXCEL_MIME_TYPES.contains(contentType)) {
			return rawImportExcelFileReader.read(importData.getFileToImport(), importData.getStartWithLine());
		} else {
			String message = String.format("Cannot import invalid file type: %s", contentType);
			throw new IllegalArgumentException(message);
		}
	}
	
	private Charset findCharsetOrDefault(String encoding) {
		try {
			return Charset.forName(encoding);
		} catch (Throwable e) {
			LOGGER.warn("Got invalid charset. Fall back to UTF-8.", e);
			return StandardCharsets.UTF_8;
		}
	}
}
