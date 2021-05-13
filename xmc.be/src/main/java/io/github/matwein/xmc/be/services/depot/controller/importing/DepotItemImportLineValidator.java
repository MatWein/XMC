package io.github.matwein.xmc.be.services.depot.controller.importing;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.services.importing.controller.IImportRowValidator;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepotItemImportLineValidator implements IImportRowValidator<DtoDepotItemImportRow> {
	@Override
	public List<DtoImportFileValidationResultError> apply(DtoDepotItemImportRow importRow, Integer lineIndex) {
		List<DtoImportFileValidationResultError> errors = Lists.newArrayList();
		
		if (StringUtils.isBlank(importRow.getIsin())) {
			errors.add(createValueEmptyError(lineIndex, DepotItemImportColmn.ISIN));
		}
		
		if (importRow.getAmount() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotItemImportColmn.AMOUNT));
		}
		
		if (importRow.getCourse() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotItemImportColmn.COURSE));
		}
		
		if (importRow.getValue() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotItemImportColmn.VALUE));
		}
		
		if (importRow.getCurrency() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotItemImportColmn.CURRENCY));
		}
		
		return errors;
	}
	
	private DtoImportFileValidationResultError createValueEmptyError(Integer lineIndex, DepotItemImportColmn importColmn) {
		String fieldCannotBeEmptyError = MessageAdapter.getByKey(MessageKey.VALIDATION_IMPORT_FIELD_ERROR);
		
		return new DtoImportFileValidationResultError(
				lineIndex + 1, String.format(fieldCannotBeEmptyError,
				MessageAdapter.getByKey(MessageKey.DEPOT_ITEM_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, importColmn)));
	}
}
