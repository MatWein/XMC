package org.xmc.fe.stages.main.depot.importing.deliveries.populator;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.stages.main.depot.importing.deliveries.DepotDeliveryImportStep3Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class DepotDeliveryImportStep3Populator implements IWizardStepPopulator<DtoImportData<DepotDeliveryImportColmn>, DepotDeliveryImportStep3Controller> {
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	
	@Autowired
	public DepotDeliveryImportStep3Populator(DtoColumnMappingFactory dtoColumnMappingFactory) {
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
	}
	
	@Override
	public void populateState(DtoImportData<DepotDeliveryImportColmn> input, DepotDeliveryImportStep3Controller controller) {
		input.setStartWithLine((int)controller.getStartWithLineNumberField().getValue());
		input.setColmuns(Lists.newArrayList(controller.getColumnMappingTable().getItems()));
	}
	
	@Override
	public void populateGui(DtoImportData<DepotDeliveryImportColmn> input, DepotDeliveryImportStep3Controller controller) {
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
