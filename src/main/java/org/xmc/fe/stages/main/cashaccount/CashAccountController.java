package org.xmc.fe.stages.main.cashaccount;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.cashaccount.CashAccountService;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.fe.stages.main.cashaccount.mapper.CashAccountEditDialogMapper;
import org.xmc.fe.ui.DialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.BreadcrumbBar;
import org.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;
import org.xmc.fe.ui.components.TableViewEx;

import java.util.List;
import java.util.Optional;

@Component
public class CashAccountController {
    private final CashAccountService cashAccountService;
    private final CashAccountEditDialogMapper cashAccountEditDialogMapper;

    @FXML private BreadcrumbBar<?> breadcrumbBar;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private TableViewEx<DtoCashAccount> tableView;

    @Autowired
    public CashAccountController(
            CashAccountService cashAccountService,
            CashAccountEditDialogMapper cashAccountEditDialogMapper) {

        this.cashAccountService = cashAccountService;
        this.cashAccountEditDialogMapper = cashAccountEditDialogMapper;
    }

    @FXML
    public void initialize() {
        breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW)));

        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

        resetTableView();
    }

    private void resetTableView() {
        List<DtoCashAccount> cashAccounts = cashAccountService.load(0, 50);

        tableView.getItems().clear();
        tableView.getItems().addAll(cashAccounts);
    }

    @FXML
    public void onNewCashAccount() {
        createOrEditCashAccount(null);

        resetTableView();
    }

    @FXML
    public void onEditCashAccount() {
        DtoCashAccount selectedCashAccount = tableView.getSelectionModel().getSelectedItem();
        createOrEditCashAccount(selectedCashAccount);

        resetTableView();
    }

    private void createOrEditCashAccount(DtoCashAccount input) {
        Optional<DtoCashAccount> dtoCashAccount = DialogBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_EDIT_TITLE)
                .addButton(MessageKey.CASHACCOUNT_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CASHACCOUNT_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CASH_ACCOUNT_EDIT)
                .withDefaultIcon()
                .resultConverter(cashAccountEditDialogMapper)
                .inputConverter(cashAccountEditDialogMapper)
                .withInput(input)
                .build()
                .showAndWait();

        if (dtoCashAccount.isPresent()) {
            cashAccountService.saveOrUpdate(dtoCashAccount.get());
        }
    }
}
