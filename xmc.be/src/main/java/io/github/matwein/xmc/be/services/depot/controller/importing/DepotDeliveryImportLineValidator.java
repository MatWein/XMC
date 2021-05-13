package io.github.matwein.xmc.be.services.depot.controller.importing;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.be.services.importing.controller.IImportRowValidator;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepotDeliveryImportLineValidator implements IImportRowValidator<DtoDepotDeliveryImportRow> {
	@Override
	public List<DtoImportFileValidationResultError> apply(DtoDepotDeliveryImportRow importRow, Integer lineIndex) {
		List<DtoImportFileValidationResultError> errors = Lists.newArrayList();
		
		if (StringUtils.isBlank(importRow.getIsin())) {
			errors.add(createValueEmptyError(lineIndex, DepotDeliveryImportColmn.ISIN));
		}
		
		if (importRow.getDeliveryDate() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotDeliveryImportColmn.DELIVERY_DATE));
		}
		
		if (importRow.getAmount() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotDeliveryImportColmn.AMOUNT));
		}
		
		if (importRow.getCourse() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotDeliveryImportColmn.COURSE));
		}
		
		if (importRow.getValue() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotDeliveryImportColmn.VALUE));
		}
		
		if (importRow.getCurrency() == null) {
			errors.add(createValueEmptyError(lineIndex, DepotDeliveryImportColmn.CURRENCY));
		}
		
		return errors;
	}
	
	private DtoImportFileValidationResultError createValueEmptyError(Integer lineIndex, DepotDeliveryImportColmn importColmn) {
		String fieldCannotBeEmptyError = MessageAdapter.getByKey(MessageKey.VALIDATION_IMPORT_FIELD_ERROR);
		
		return new DtoImportFileValidationResultError(
				lineIndex + 1, String.format(fieldCannotBeEmptyError,
				MessageAdapter.getByKey(MessageKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, importColmn)));
	}
}
