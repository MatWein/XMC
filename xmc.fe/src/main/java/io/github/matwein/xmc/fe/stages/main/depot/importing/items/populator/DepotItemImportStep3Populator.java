package io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.importing.DtoColumnMappingFactory;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.DepotItemImportStep3Controller;
import io.github.matwein.xmc.fe.ui.wizard.IWizardStepPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepotItemImportStep3Populator implements IWizardStepPopulator<DtoImportData<DepotItemImportColmn>, DepotItemImportStep3Controller> {
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	
	@Autowired
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
