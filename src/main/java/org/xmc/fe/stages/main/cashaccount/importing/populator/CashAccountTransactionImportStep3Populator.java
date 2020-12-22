package org.xmc.fe.stages.main.cashaccount.importing.populator;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.stages.main.cashaccount.importing.CashAccountTransactionImportStep3Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class CashAccountTransactionImportStep3Populator implements IWizardStepPopulator<DtoImportData<CashAccountTransactionImportColmn>, CashAccountTransactionImportStep3Controller> {
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	
	@Autowired
	public CashAccountTransactionImportStep3Populator(DtoColumnMappingFactory dtoColumnMappingFactory) {
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
	}
	
	@Override
    public void populateState(DtoImportData<CashAccountTransactionImportColmn> input, CashAccountTransactionImportStep3Controller controller) {
        input.setStartWithLine((int)controller.getStartWithLineNumberField().getValue());
        input.setColmuns(Lists.newArrayList(controller.getColumnMappingTable().getItems()));
    }
	
	@Override
	public void populateGui(DtoImportData<CashAccountTransactionImportColmn> input, CashAccountTransactionImportStep3Controller controller) {
		if (input.getStartWithLine() != 0) {
			controller.getStartWithLineNumberField().setValue(input.getStartWithLine());
		}
		
		if (CollectionUtils.isEmpty(input.getColmuns())) {
			input.setColmuns(dtoColumnMappingFactory.generateEmptyColumns());
		}
		
		controller.getColumnMappingTable().getItems().clear();
		controller.getColumnMappingTable().getItems().addAll(input.getColmuns());
	}
}
