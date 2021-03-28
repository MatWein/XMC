package org.xmc.fe.stages.main.depot.importing.transactions.populator;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.stages.main.depot.importing.transactions.DepotTransactionImportStep3Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class DepotTransactionImportStep3Populator implements IWizardStepPopulator<DtoImportData<DepotTransactionImportColmn>, DepotTransactionImportStep3Controller> {
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	
	@Autowired
	public DepotTransactionImportStep3Populator(DtoColumnMappingFactory dtoColumnMappingFactory) {
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
	}
	
	@Override
	public void populateState(DtoImportData<DepotTransactionImportColmn> input, DepotTransactionImportStep3Controller controller) {
		input.setStartWithLine((int)controller.getStartWithLineNumberField().getValue());
		input.setColmuns(Lists.newArrayList(controller.getColumnMappingTable().getItems()));
	}
	
	@Override
	public void populateGui(DtoImportData<DepotTransactionImportColmn> input, DepotTransactionImportStep3Controller controller) {
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