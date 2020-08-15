package org.xmc.fe.stages.main.cashaccount.mapper;

import javafx.scene.control.ButtonBar;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.fe.stages.main.cashaccount.CashAccountTransactionEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class CashAccountTransactionEditDialogMapper implements IDialogMapper<CashAccountTransactionEditController, DtoCashAccountTransaction> {
    @Override
    public void accept(CashAccountTransactionEditController cashAccountTransactionEditController, DtoCashAccountTransaction dtoCashAccountTransaction) {

    }

    @Override
    public DtoCashAccountTransaction apply(ButtonBar.ButtonData buttonData, CashAccountTransactionEditController cashAccountTransactionEditController) {
        return null;
    }
}
