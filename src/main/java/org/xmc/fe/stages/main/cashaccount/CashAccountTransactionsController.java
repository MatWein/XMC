package org.xmc.fe.stages.main.cashaccount;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;

@FxmlController
public class CashAccountTransactionsController implements IAfterInit<CashAccountController> {
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CashAccountController parentController;

    @FXML
    public void initialize() {

    }

    @Override
    public void afterInitialize(CashAccountController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void onNewTransaction() {
    }

    @FXML
    public void onEditTransaction() {
    }

    @FXML
    public void onDeleteTransaction() {
    }
}
