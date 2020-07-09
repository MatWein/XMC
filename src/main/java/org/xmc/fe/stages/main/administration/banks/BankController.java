package org.xmc.fe.stages.main.administration.banks;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.bank.BankService;
import org.xmc.common.stubs.bank.BankOverviewFields;
import org.xmc.common.stubs.bank.DtoBankOverview;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.components.async.AsyncButton;
import org.xmc.fe.ui.components.table.TableViewEx;

@FxmlController
public class BankController {
    private final BankService bankService;

    @FXML private TableViewEx<DtoBankOverview, BankOverviewFields> tableView;
    @FXML private AsyncButton editButton;
    @FXML private Button deleteButton;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.bindDisable(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);
        tableView.setDataProvider(bankService::loadOverview);
    }

    @FXML
    public void onNewBank() {
    }

    @FXML
    public void onEditBank() {
    }

    @FXML
    public void onDeleteBank() {
    }
}
