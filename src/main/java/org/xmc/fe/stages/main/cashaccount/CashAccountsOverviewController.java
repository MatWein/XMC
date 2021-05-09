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
import org.xmc.fe.stages.main.analysis.logic.AnalysisAllAssetsRefreshController;
import org.xmc.fe.stages.main.cashaccount.mapper.CashAccountEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;

@FxmlController
public class CashAccountsOverviewController implements IAfterInit<CashAccountController> {
    private final CashAccountService cashAccountService;
    private final CashAccountEditDialogMapper cashAccountEditDialogMapper;
    private final AsyncProcessor asyncProcessor;
    private final BankService bankService;
	private final AnalysisAllAssetsRefreshController analysisAllAssetsRefreshController;
	
	@FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button navigateTransactionsButton;
    @FXML private ExtendedTable<DtoCashAccountOverview, CashAccountOverviewFields> tableView;

    private CashAccountController parentController;

    @Autowired
    public CashAccountsOverviewController(
		    CashAccountService cashAccountService,
		    CashAccountEditDialogMapper cashAccountEditDialogMapper,
		    AsyncProcessor asyncProcessor,
		    BankService bankService,
		    AnalysisAllAssetsRefreshController analysisAllAssetsRefreshController) {

        this.cashAccountService = cashAccountService;
        this.cashAccountEditDialogMapper = cashAccountEditDialogMapper;
        this.asyncProcessor = asyncProcessor;
        this.bankService = bankService;
	    this.analysisAllAssetsRefreshController = analysisAllAssetsRefreshController;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);
        navigateTransactionsButton.disableProperty().bind(noTableItemSelected);

        tableView.setDataProvider(cashAccountService::loadOverview);
    }

    @Override
    public void afterInitialize(CashAccountController parentController) {
        this.parentController = parentController;
        tableView.setDoubleClickConsumer(parentController::switchToTransactions);
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
        DtoCashAccountOverview selectedCashAccount = tableView.getSelectionModel().getSelectedItem();

        if (DialogHelper.showConfirmDialog(MessageKey.CASHACCOUNT_CONFIRM_DELETE, selectedCashAccount.getName())) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> cashAccountService.markAsDeleted(monitor, selectedCashAccount.getId()),
		            this::refreshRelatedViews
            );
        }
    }

    @FXML
    public void onNavigateTransactions() {
        DtoCashAccountOverview selectedCashAccount = tableView.getSelectionModel().getSelectedItem();
        parentController.switchToTransactions(selectedCashAccount);
    }

    private void createOrEditCashAccount(DtoCashAccountOverview input) {
        Optional<DtoCashAccount> dtoCashAccount = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_EDIT_TITLE)
                .addButton(MessageKey.CASHACCOUNT_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CASHACCOUNT_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CASH_ACCOUNT_EDIT)
                .withMapper(cashAccountEditDialogMapper)
                .withInput(input)
                .withAsyncDataLoading(bankService::loadAllBanks)
                .build()
                .showAndWait();

        if (dtoCashAccount.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> cashAccountService.saveOrUpdate(monitor, dtoCashAccount.get()),
		            this::refreshRelatedViews
            );
        }
    }
	
	private void refreshRelatedViews() {
		tableView.reload();
		analysisAllAssetsRefreshController.refreshAllAssets();
	}
}
