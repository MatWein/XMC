package org.xmc.fe.stages.main.cashaccount;

import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IDialogWithAsyncData;

import java.util.List;

@FxmlController
public class CashAccountTransactionEditController implements IDialogWithAsyncData<List<DtoCashAccountTransaction>> {
    @Override
    public void acceptAsyncData(List<DtoCashAccountTransaction> data) {

    }
}
