package org.xmc.fe.stages.main.cashaccount.importing.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoCashAccountTransactionImportData;
import org.xmc.fe.stages.main.cashaccount.importing.CashAccountTransactionImportStep4Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class CashAccountTransactionImportStep4Populator implements IWizardStepPopulator<DtoCashAccountTransactionImportData, CashAccountTransactionImportStep4Controller> {
    @Override
    public void populateState(DtoCashAccountTransactionImportData input, CashAccountTransactionImportStep4Controller controller) {
        input.setSaveTemplate(controller.getSaveTemplateCheckbox().isSelected());
        input.setTemplateToSaveName(controller.getTemplateToSaveName().getText());
    }
}
