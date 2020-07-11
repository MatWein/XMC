package org.xmc.fe.stages.main.cashaccount;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.bank.BankService;
import org.xmc.be.services.cashaccount.CashAccountService;
import org.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.cashaccount.mapper.CashAccountEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.BreadcrumbBar;
import org.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;
import org.xmc.fe.ui.components.table.TableViewEx;

import java.util.Optional;

@FxmlController
public class CashAccountController {
    private final CashAccountService cashAccountService;
    private final CashAccountEditDialogMapper cashAccountEditDialogMapper;
    private final AsyncProcessor asyncProcessor;
    private final BankService bankService;

    @FXML private BreadcrumbBar<?> breadcrumbBar;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private TableViewEx<DtoCashAccountOverview, CashAccountOverviewFields> tableView;

    @Autowired
    public CashAccountController(
            CashAccountService cashAccountService,
            CashAccountEditDialogMapper cashAccountEditDialogMapper,
            AsyncProcessor asyncProcessor,
            BankService bankService) {

        this.cashAccountService = cashAccountService;
        this.cashAccountEditDialogMapper = cashAccountEditDialogMapper;
        this.asyncProcessor = asyncProcessor;
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW)));

        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);
        tableView.setDataProvider(cashAccountService::loadOverview);
    }

    @FXML
    public void onNewCashAccount() {
        createOrEditCashAccount(null);
    }

    @FXML
    public void onEditCashAccount() {
        DtoCashAccountOverview selectedCashAccount = tableView.getSelectionModel().getSelectedItem();
        createOrEditCashAccount(selectedCashAccount);
    }

    @FXML
    public void onDeleteCashAccount() {
    }

    private void createOrEditCashAccount(DtoCashAccountOverview input) {
        Optional<DtoCashAccount> dtoCashAccount = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_EDIT_TITLE)
                .addButton(MessageKey.CASHACCOUNT_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CASHACCOUNT_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CASH_ACCOUNT_EDIT)
                .withDefaultIcon()
                .withMapper(cashAccountEditDialogMapper)
                .withInput(input)
                .withAsyncDataLoading(bankService::loadAllBanks)
                .build()
                .showAndWait();

        if (dtoCashAccount.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> cashAccountService.saveOrUpdate(monitor, dtoCashAccount.get()),
                    () -> tableView.reload()
            );
        }
    }
}
