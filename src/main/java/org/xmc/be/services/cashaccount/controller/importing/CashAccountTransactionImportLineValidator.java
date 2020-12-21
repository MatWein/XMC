package org.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultErrors;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class CashAccountTransactionImportLineValidator implements BiFunction<DtoCashAccountTransaction, Integer, List<DtoImportFileValidationResultErrors>> {
	@Override
	public List<DtoImportFileValidationResultErrors> apply(DtoCashAccountTransaction dtoCashAccountTransaction, Integer lineIndex) {
		return Lists.newArrayList();
	}
}
