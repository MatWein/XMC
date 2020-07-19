package org.xmc.fe.stages.main.cashaccount;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.xmc.common.stubs.cashaccount.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccountTransactionOverview;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.components.table.TableViewEx;

@FxmlController
public class CashAccountTransactionsController implements IAfterInit<CashAccountController> {
    @FXML private TableViewEx<DtoCashAccountTransactionOverview, CashAccountTransactionOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CashAccountController parentController;

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);

//        tableView.setDataProvider(cashAccountService::loadOverview);
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
