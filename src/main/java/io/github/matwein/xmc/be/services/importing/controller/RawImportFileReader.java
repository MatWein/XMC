package io.github.matwein.xmc.be.services.importing.controller;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;

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
			DtoImportData<?> importData,
			String fileExtension) throws IOException, CsvValidationException {
		
		if (ImportPreparationController.VALID_CSV_EXTENSIONS.contains(fileExtension)) {
			Charset charset = findCharsetOrDefault(importData.getEncoding());
			return rawImportCsvFileReader.read(importData.getFileToImport(), importData.getStartWithLine(), importData.getCsvSeparator(), charset);
		} else if (ImportPreparationController.VALID_EXCEL_EXTENSIONS.contains(fileExtension)) {
			return rawImportExcelFileReader.read(importData.getFileToImport(), importData.getStartWithLine());
		} else {
			String message = String.format("Cannot import invalid file extension: %s", fileExtension);
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
