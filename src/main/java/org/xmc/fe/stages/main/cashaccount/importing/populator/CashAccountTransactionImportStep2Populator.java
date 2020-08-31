package org.xmc.fe.stages.main.cashaccount.importing.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoCashAccountTransactionImportData;
import org.xmc.fe.stages.main.cashaccount.importing.CashAccountTransactionImportStep2Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class CashAccountTransactionImportStep2Populator implements IWizardStepPopulator<DtoCashAccountTransactionImportData, CashAccountTransactionImportStep2Controller> {
    @Override
    public void populateState(DtoCashAccountTransactionImportData input, CashAccountTransactionImportStep2Controller controller) {
        input.setFilePath(controller.getFileChooserField().getValueAsString());
    }
}
