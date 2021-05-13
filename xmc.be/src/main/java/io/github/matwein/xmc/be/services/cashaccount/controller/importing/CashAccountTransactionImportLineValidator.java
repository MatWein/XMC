package io.github.matwein.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.services.importing.controller.IImportRowValidator;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashAccountTransactionImportLineValidator implements IImportRowValidator<DtoCashAccountTransaction> {
	@Override
	public List<DtoImportFileValidationResultError> apply(DtoCashAccountTransaction transaction, Integer lineIndex) {
		List<DtoImportFileValidationResultError> errors = Lists.newArrayList();
		
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
