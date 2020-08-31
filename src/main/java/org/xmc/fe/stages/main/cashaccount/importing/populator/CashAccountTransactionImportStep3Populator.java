package org.xmc.fe.stages.main.cashaccount.importing.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoCashAccountTransactionImportData;
import org.xmc.fe.stages.main.cashaccount.importing.CashAccountTransactionImportStep3Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

@Component
public class CashAccountTransactionImportStep3Populator implements IWizardStepPopulator<DtoCashAccountTransactionImportData, CashAccountTransactionImportStep3Controller> {
    @Override
    public void populateState(DtoCashAccountTransactionImportData input, CashAccountTransactionImportStep3Controller controller) {

    }
}
