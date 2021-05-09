package io.github.matwein.xmc.fe.stages.main.depot.importing.transactions.populator;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.stages.main.depot.importing.transactions.DepotTransactionImportStep4Controller;
import io.github.matwein.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class DepotTransactionImportStep4Populator implements IWizardStepPopulator<DtoImportData<DepotTransactionImportColmn>, DepotTransactionImportStep4Controller> {
	@Override
	public void populateState(DtoImportData<DepotTransactionImportColmn> input, DepotTransactionImportStep4Controller controller) {
		input.setSaveTemplate(controller.getSaveTemplateCheckbox().isSelected());
		input.setTemplateToSaveName(controller.getTemplateToSaveName().getTextOrNull());
	}
	
	@Override
	public void populateGui(DtoImportData<DepotTransactionImportColmn> input, DepotTransactionImportStep4Controller controller) {
		controller.getTemplateToSaveName().setText(input.getTemplateToSaveName());
	}
}
