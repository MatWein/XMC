package org.xmc.fe.stages.main.cashaccount.importing.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.stages.main.cashaccount.importing.CashAccountTransactionImportStep4Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class CashAccountTransactionImportStep4Populator implements IWizardStepPopulator<DtoImportData<CashAccountTransactionImportColmn>, CashAccountTransactionImportStep4Controller> {
    @Override
    public void populateState(DtoImportData<CashAccountTransactionImportColmn> input, CashAccountTransactionImportStep4Controller controller) {
        input.setSaveTemplate(controller.getSaveTemplateCheckbox().isSelected());
        input.setTemplateToSaveName(controller.getTemplateToSaveName().getTextOrNull());
    }
	
	@Override
	public void populateGui(DtoImportData<CashAccountTransactionImportColmn> input, CashAccountTransactionImportStep4Controller controller) {
		controller.getTemplateToSaveName().setText(input.getTemplateToSaveName());
	}
}
