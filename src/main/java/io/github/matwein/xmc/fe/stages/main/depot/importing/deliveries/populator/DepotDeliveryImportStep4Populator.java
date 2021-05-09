package io.github.matwein.xmc.fe.stages.main.depot.importing.deliveries.populator;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.stages.main.depot.importing.deliveries.DepotDeliveryImportStep4Controller;
import io.github.matwein.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class DepotDeliveryImportStep4Populator implements IWizardStepPopulator<DtoImportData<DepotDeliveryImportColmn>, DepotDeliveryImportStep4Controller> {
	@Override
	public void populateState(DtoImportData<DepotDeliveryImportColmn> input, DepotDeliveryImportStep4Controller controller) {
		input.setSaveTemplate(controller.getSaveTemplateCheckbox().isSelected());
		input.setTemplateToSaveName(controller.getTemplateToSaveName().getTextOrNull());
	}
	
	@Override
	public void populateGui(DtoImportData<DepotDeliveryImportColmn> input, DepotDeliveryImportStep4Controller controller) {
		controller.getTemplateToSaveName().setText(input.getTemplateToSaveName());
	}
}
