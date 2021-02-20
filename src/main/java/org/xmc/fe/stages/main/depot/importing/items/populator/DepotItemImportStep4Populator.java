package org.xmc.fe.stages.main.depot.importing.items.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.stages.main.depot.importing.items.DepotItemImportStep4Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

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
