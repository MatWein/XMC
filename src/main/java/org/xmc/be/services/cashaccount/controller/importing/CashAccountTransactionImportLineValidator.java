package org.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.be.services.importing.controller.IImportRowValidator;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;

@Component
public class CashAccountTransactionImportLineValidator implements IImportRowValidator<DtoCashAccountTransaction> {
	@Override
	public List<DtoImportFileValidationResultError> apply(DtoCashAccountTransaction transaction, Integer lineIndex) {
		List<DtoImportFileValidationResultError> errors = Lists.newArrayList();
		
		if (StringUtils.isBlank(transaction.getUsage())) {
			errors.add(createValueEmptyError(lineIndex, CashAccountTransactionImportColmn.USAGE));
		}
		
		if (transaction.getValutaDate() == null) {
			errors.add(createValueEmptyError(lineIndex, CashAccountTransactionImportColmn.VALUTA_DATE));
		}
		
		if (transaction.getValue() == null) {
			errors.add(createValueEmptyError(lineIndex, CashAccountTransactionImportColmn.VALUE));
		}
		
		return errors;
	}
	
	private DtoImportFileValidationResultError createValueEmptyError(Integer lineIndex, CashAccountTransactionImportColmn importColmn) {
		String fieldCannotBeEmptyError = MessageAdapter.getByKey(MessageKey.VALIDATION_IMPORT_FIELD_ERROR);
		
		return new DtoImportFileValidationResultError(
				lineIndex + 1, String.format(fieldCannotBeEmptyError,
				MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, importColmn)));
	}
}
