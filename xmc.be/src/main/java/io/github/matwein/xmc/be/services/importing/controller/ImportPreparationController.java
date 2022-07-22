package io.github.matwein.xmc.be.services.importing.controller;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.services.importing.mapper.DtoImportFileValidationResultMapper;
import io.github.matwein.xmc.common.FileMimeType;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import io.github.matwein.xmc.common.stubs.importing.exceptions.ImportFileTypeException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ImportPreparationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportPreparationController.class);
	
	public static final Set<String> VALID_CSV_EXTENSIONS = Set.of(
			FileMimeType.TEXT.getFileExtension().toUpperCase(),
			FileMimeType.CSV.getFileExtension().toUpperCase()
	);
	
	public static final Set<String> VALID_EXCEL_EXTENSIONS = Set.of(
			FileMimeType.MS_EXCELX.getFileExtension().toUpperCase(),
			FileMimeType.MS_EXCEL.getFileExtension().toUpperCase()
	);
	
	public static final Set<String> VALID_EXTENSIONS = Stream.concat(VALID_CSV_EXTENSIONS.stream(), VALID_EXCEL_EXTENSIONS.stream()).collect(Collectors.toSet());
	
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
			IAsyncMonitor monitor,
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
			IAsyncMonitor monitor,
			DtoImportData<IMPORT_COLUMN_ENUM> importData,
			IImportRowMapper<RESULT_DTO_TYPE, IMPORT_COLUMN_ENUM> rowMapper,
			IImportRowValidator<RESULT_DTO_TYPE> rowValidator) throws Exception {
		
		int processItemCount = 3;
		int processedItems = 0;
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_VALIDATE_IMPORT_FILE));
		String fileExtension = validateFileExtension(importData.getFileToImport());
		monitor.setProgressByItemCount(++processedItems, processItemCount);
		
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_READ_IMPORT_FILE));
		List<List<String>> rawFileContent = rawImportFileReader.read(importData, fileExtension);
		monitor.setProgressByItemCount(++processedItems, processItemCount);
		
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_MAP_IMPORT_FILE));
		DtoImportFileValidationResult<RESULT_DTO_TYPE> result = dtoImportFileValidationResultMapper.map(
				rawFileContent,
				importData.getColmuns(),
				rowMapper,
				rowValidator);
		monitor.setProgressByItemCount(++processedItems, processItemCount);
		
		return result;
	}
	
	private String validateFileExtension(File fileToImport) throws ImportFileTypeException {
		String contentType = FilenameUtils.getExtension(fileToImport.getAbsolutePath()).toUpperCase();
		if (!VALID_EXTENSIONS.contains(contentType)) {
			throw new ImportFileTypeException();
		}
		return contentType;
	}
	
	private <RESULT_DTO_TYPE extends Serializable>
	DtoImportFileValidationResult<RESULT_DTO_TYPE> createGeneralErrorResult(MessageKey messageKey, Object... args) {
		var result = new DtoImportFileValidationResult<RESULT_DTO_TYPE>();
		
		result.getErrors().add(new DtoImportFileValidationResultError(0, MessageAdapter.getByKey(messageKey, args)));
		
		return result;
	}
}
