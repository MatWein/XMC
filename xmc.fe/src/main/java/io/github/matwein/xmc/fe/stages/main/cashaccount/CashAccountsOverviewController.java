package io.github.matwein.xmc.fe.stages.main.cashaccount;

import io.github.matwein.xmc.common.services.bank.IBankService;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountService;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccount;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.analysis.logic.AnalysisAllAssetsRefreshController;
import io.github.matwein.xmc.fe.stages.main.cashaccount.mapper.CashAccountEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@FxmlController
public class CashAccountsOverviewController implements IAfterInit<CashAccountController> {
    private final ICashAccountService cashAccountService;
    private final CashAccountEditDialogMapper cashAccountEditDialogMapper;
    private final AsyncProcessor asyncProcessor;
    private final IBankService bankService;
	private final AnalysisAllAssetsRefreshController analysisAllAssetsRefreshController;
	
	@FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button navigateTransactionsButton;
    @FXML private ExtendedTable<DtoCashAccountOverview, CashAccountOverviewFields> tableView;

    private CashAccountController parentController;

    @Autowired
    public CashAccountsOverviewController(
		    ICashAccountService cashAccountService,
		    CashAccountEditDialogMapper cashAccountEditDialogMapper,
		    AsyncProcessor asyncProcessor,
		    IBankService bankService,
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
