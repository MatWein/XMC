package org.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xmc.common.FileMimeType;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultErrors;
import org.xmc.common.stubs.importing.exceptions.ImportFileTypeException;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

@Component
public class CashAccountTransactionImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportController.class);
	
	private static final Set<String> VALID_MIME_TYPES = Sets.newHashSet(
			FileMimeType.MS_EXCELO.getMimeType(),
			FileMimeType.MS_EXCELX.getMimeType(),
			FileMimeType.CSV.getMimeType()
	);
	
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
	
	private DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateWithoutErrorHandling(
			AsyncMonitor monitor,
			DtoImportData<CashAccountTransactionImportColmn> importData) throws ImportFileTypeException {
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_VALIDATE_IMPORT_FILE);
		validateFileContentType(importData.getFileToImport());
		
		return null;
	}
	
	private void validateFileContentType(File fileToImport) throws ImportFileTypeException {
		try {
			String contentType = Files.probeContentType(fileToImport.toPath());
			if (!VALID_MIME_TYPES.contains(contentType)) {
				throw new ImportFileTypeException();
			}
		} catch (IOException e) {
			throw new ImportFileTypeException();
		}
	}
	
	private DtoImportFileValidationResult<DtoCashAccountTransaction> createGeneralErrorResult(MessageKey messageKey, Object... args) {
		var result = new DtoImportFileValidationResult<DtoCashAccountTransaction>();
		
		result.getErrors().add(new DtoImportFileValidationResultErrors(0, MessageAdapter.getByKey(messageKey, args)));
		
		return result;
	}
}
