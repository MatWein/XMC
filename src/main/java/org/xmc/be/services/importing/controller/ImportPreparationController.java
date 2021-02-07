package org.xmc.be.services.importing.controller;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.importing.mapper.DtoImportFileValidationResultMapper;
import org.xmc.common.FileMimeType;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import org.xmc.common.stubs.importing.exceptions.ImportFileTypeException;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

@Component
public class ImportPreparationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportPreparationController.class);
	
	public static final Set<String> VALID_CSV_MIME_TYPES = Sets.newHashSet(
			FileMimeType.CSV.getMimeType()
	);
	
	public static final Set<String> VALID_EXCEL_MIME_TYPES = Sets.newHashSet(
			FileMimeType.MS_EXCELO.getMimeType(),
			FileMimeType.MS_EXCELX.getMimeType()
	);
	
	public static final Set<String> VALID_MIME_TYPES = Sets.union(VALID_CSV_MIME_TYPES, VALID_EXCEL_MIME_TYPES);
	
	private final RawImportFileReader rawImportFileReader;
	private final DtoImportFileValidationResultMapper dtoImportFileValidationResultMapper;
	
	@Autowired
	public ImportPreparationController(
			RawImportFileReader rawImportFileReader,
			DtoImportFileValidationResultMapper dtoImportFileValidationResultMapper) {
		
		this.rawImportFileReader = rawImportFileReader;
		this.dtoImportFileValidationResultMapper = dtoImportFileValidationResultMapper;
	}
	
	public <RESULT_DTO_TYPE extends Serializable, IMPORT_COLUMN_ENUM extends Enum<IMPORT_COLUMN_ENUM>>
	DtoImportFileValidationResult<RESULT_DTO_TYPE> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<IMPORT_COLUMN_ENUM> importData,
			IImportRowMapper<RESULT_DTO_TYPE, IMPORT_COLUMN_ENUM> rowMapper,
			IImportRowValidator<RESULT_DTO_TYPE> rowValidator) {
		
		try {
			return readAndValidateWithoutErrorHandling(monitor, importData, rowMapper, rowValidator);
		} catch (ImportFileTypeException e) {
			LOGGER.warn("Unknown error on reading import file: {}", importData.getFileToImport(), e);
			return createGeneralErrorResult(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_FILETYPE_ERROR);
		} catch (Throwable e) {
			LOGGER.warn("Unknown error on reading import file: {}", importData.getFileToImport(), e);
			return createGeneralErrorResult(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COMMON_ERROR);
		}
	}
	
	public <RESULT_DTO_TYPE extends Serializable, IMPORT_COLUMN_ENUM extends Enum<IMPORT_COLUMN_ENUM>>
	DtoImportFileValidationResult<RESULT_DTO_TYPE> readAndValidateWithoutErrorHandling(
			AsyncMonitor monitor,
			DtoImportData<IMPORT_COLUMN_ENUM> importData,
			IImportRowMapper<RESULT_DTO_TYPE, IMPORT_COLUMN_ENUM> rowMapper,
			IImportRowValidator<RESULT_DTO_TYPE> rowValidator) throws Exception {
		
		int processItemCount = 3;
		int processedItems = 0;
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_VALIDATE_IMPORT_FILE);
		String contentType = validateFileContentType(importData.getFileToImport());
		monitor.setProgressByItemCount(++processedItems, processItemCount);
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_READ_IMPORT_FILE);
		List<List<String>> rawFileContent = rawImportFileReader.read(importData, contentType);
		monitor.setProgressByItemCount(++processedItems, processItemCount);
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_MAP_IMPORT_FILE);
		DtoImportFileValidationResult<RESULT_DTO_TYPE> result = dtoImportFileValidationResultMapper.map(
				rawFileContent,
				importData.getColmuns(),
				rowMapper,
				rowValidator);
		monitor.setProgressByItemCount(++processedItems, processItemCount);
		
		return result;
	}
	
	private String validateFileContentType(File fileToImport) throws ImportFileTypeException {
		try {
			String contentType = Files.probeContentType(fileToImport.toPath());
			if (!VALID_MIME_TYPES.contains(contentType)) {
				throw new ImportFileTypeException();
			}
			return contentType;
		} catch (IOException e) {
			throw new ImportFileTypeException();
		}
	}
	
	private <RESULT_DTO_TYPE extends Serializable>
	DtoImportFileValidationResult<RESULT_DTO_TYPE> createGeneralErrorResult(MessageKey messageKey, Object... args) {
		var result = new DtoImportFileValidationResult<RESULT_DTO_TYPE>();
		
		result.getErrors().add(new DtoImportFileValidationResultError(0, MessageAdapter.getByKey(messageKey, args)));
		
		return result;
	}
}
