package io.github.matwein.xmc.be.services.depot.controller.importing;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.be.services.importing.controller.IImportRowValidator;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepotTransactionImportLineValidator implements IImportRowValidator<DtoDepotTransactionImportRow> {
	@Override
	public List<DtoImportFileValidationResultError> apply(DtoDepotTransactionImportRow importRow, Integer lineIndex) {
		List<DtoImportFileValidationResultError> errors = Lists.newArrayList();
		
		if (StringUtils.isBlank(importRow.getIsin())) {
			errors.add(createValueEmptyError(lineIndex, DepotTransactionImportColmn.ISIN));
		}
		
		if (importRow.getValutaDate() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotTransactionImportColmn.VALUTA_DATE));
		}
		
		if (importRow.getAmount() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotTransactionImportColmn.AMOUNT));
		}
		
		if (importRow.getCourse() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotTransactionImportColmn.COURSE));
		}
		
		if (importRow.getValue() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotTransactionImportColmn.VALUE));
		}
		
		if (importRow.getCurrency() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotTransactionImportColmn.CURRENCY));
		}
		
		return errors;
	}
	
	private DtoImportFileValidationResultError createValueEmptyError(Integer lineIndex, DepotTransactionImportColmn importColmn) {
		String fieldCannotBeEmptyError = MessageAdapter.getByKey(MessageKey.VALIDATION_IMPORT_FIELD_ERROR);
		
		return new DtoImportFileValidationResultError(
				lineIndex + 1, String.format(fieldCannotBeEmptyError,
				MessageAdapter.getByKey(MessageKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, importColmn)));
	}
}
