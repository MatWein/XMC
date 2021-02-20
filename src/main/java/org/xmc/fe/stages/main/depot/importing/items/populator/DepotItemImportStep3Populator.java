package org.xmc.fe.stages.main.depot.importing.items.populator;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.stages.main.depot.importing.items.DepotItemImportStep3Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

import javax.inject.Inject;

@Component
public class DepotItemImportStep3Populator implements IWizardStepPopulator<DtoImportData<DepotItemImportColmn>, DepotItemImportStep3Controller> {
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	
	@Inject
	public DepotItemImportStep3Populator(DtoColumnMappingFactory dtoColumnMappingFactory) {
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
	}
	
	@Override
	public void populateState(DtoImportData<DepotItemImportColmn> input, DepotItemImportStep3Controller controller) {
		input.setStartWithLine((int)controller.getStartWithLineNumberField().getValue());
		input.setColmuns(Lists.newArrayList(controller.getColumnMappingTable().getItems()));
	}
	
	@Override
	public void populateGui(DtoImportData<DepotItemImportColmn> input, DepotItemImportStep3Controller controller) {
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
