package org.xmc.fe.stages.main.depot.importing.transactions.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.stages.main.depot.importing.transactions.DepotTransactionImportStep4Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

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
