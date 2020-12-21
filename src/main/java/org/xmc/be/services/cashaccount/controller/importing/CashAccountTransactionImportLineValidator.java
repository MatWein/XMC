package org.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultErrors;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class CashAccountTransactionImportLineValidator implements BiFunction<DtoCashAccountTransaction, Integer, List<DtoImportFileValidationResultErrors>> {
	@Override
	public List<DtoImportFileValidationResultErrors> apply(DtoCashAccountTransaction transaction, Integer lineIndex) {
		List<DtoImportFileValidationResultErrors> errors = Lists.newArrayList();
		
		String fieldCannotBeEmptyError = MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_FIELD_ERROR);
		
		if (StringUtils.isBlank(transaction.getUsage())) {
			errors.add(new DtoImportFileValidationResultErrors(
					lineIndex, String.format(fieldCannotBeEmptyError,
					MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, CashAccountTransactionImportColmn.USAGE))));
		}
		
		if (transaction.getValutaDate() == null) {
			errors.add(new DtoImportFileValidationResultErrors(
					lineIndex, String.format(fieldCannotBeEmptyError,
					MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, CashAccountTransactionImportColmn.VALUTA_DATE))));
		}
		
		if (transaction.getValue() == null) {
			errors.add(new DtoImportFileValidationResultErrors(
					lineIndex, String.format(fieldCannotBeEmptyError,
					MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, CashAccountTransactionImportColmn.VALUE))));
		}
		
		return errors;
	}
}
