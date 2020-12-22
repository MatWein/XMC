package org.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.common.importing.DtoImportFileValidationResultMapper;
import org.xmc.be.common.importing.RawImportFileReader;
import org.xmc.common.FileMimeType;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import org.xmc.common.stubs.importing.exceptions.ImportFileTypeException;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

@Component
public class CashAccountTransactionImportPreparationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportPreparationController.class);
	
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
	private final CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper;
	private final CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator;
	
	@Autowired
	public CashAccountTransactionImportPreparationController(
			RawImportFileReader rawImportFileReader,
			DtoImportFileValidationResultMapper dtoImportFileValidationResultMapper,
			CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper,
			CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator) {
		
		this.rawImportFileReader = rawImportFileReader;
		this.dtoImportFileValidationResultMapper = dtoImportFileValidationResultMapper;
		this.cashAccountTransactionImportLineMapper = cashAccountTransactionImportLineMapper;
		this.cashAccountTransactionImportLineValidator = cashAccountTransactionImportLineValidator;
	}
	
	public DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<CashAccountTransactionImportColmn> importData) {
		
		try {
			return readAndValidateWithoutErrorHandling(monitor, importData);
		} catch (ImportFileTypeException e) {
			LOGGER.warn("Unknown error on reading import file: {}", importData.getFileToImport(), e);
			return createGeneralErrorResult(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_FILETYPE_ERROR);
		} catch (Throwable e) {
			LOGGER.warn("Unknown error on reading import file: {}", importData.getFileToImport(), e);
			return createGeneralErrorResult(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COMMON_ERROR);
		}
	}
	
	public DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateWithoutErrorHandling(
			AsyncMonitor monitor,
			DtoImportData<CashAccountTransactionImportColmn> importData) throws Exception {
		
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
		DtoImportFileValidationResult<DtoCashAccountTransaction> result = dtoImportFileValidationResultMapper.map(
				rawFileContent,
				importData.getColmuns(),
				cashAccountTransactionImportLineMapper,
				cashAccountTransactionImportLineValidator);
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
	
	private DtoImportFileValidationResult<DtoCashAccountTransaction> createGeneralErrorResult(MessageKey messageKey, Object... args) {
		var result = new DtoImportFileValidationResult<DtoCashAccountTransaction>();
		
		result.getErrors().add(new DtoImportFileValidationResultError(0, MessageAdapter.getByKey(messageKey, args)));
		
		return result;
	}
}
