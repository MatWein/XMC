package io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator;

import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.DepotItemImportStep4Controller;
import io.github.matwein.xmc.fe.ui.wizard.IWizardStepPopulator;
import org.springframework.stereotype.Component;

@Component
public class DepotItemImportStep4Populator implements IWizardStepPopulator<DtoImportData<DepotItemImportColmn>, DepotItemImportStep4Controller> {
	@Override
	public void populateState(DtoImportData<DepotItemImportColmn> input, DepotItemImportStep4Controller controller) {
		input.setSaveTemplate(controller.getSaveTemplateCheckbox().isSelected());
		input.setTemplateToSaveName(controller.getTemplateToSaveName().getTextOrNull());
	}
	
	@Override
	public void populateGui(DtoImportData<DepotItemImportColmn> input, DepotItemImportStep4Controller controller) {
		controller.getTemplateToSaveName().setText(input.getTemplateToSaveName());
	}
}
