package org.xmc.fe.stages.main.cashaccount;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.cashaccount.CashAccountTransactionService;
import org.xmc.be.services.category.CategoryService;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep2Populator;
import org.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep3Populator;
import org.xmc.fe.stages.main.cashaccount.mapper.CashAccountTransactionEditDialogMapper;
import org.xmc.fe.stages.main.cashaccount.mapper.DtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.TableViewEx;
import org.xmc.fe.ui.wizard.WizardBuilder;

import java.util.Optional;

@FxmlController
public class CashAccountTransactionsController implements IAfterInit<CashAccountController> {
    private final CategoryService categoryService;
    private final CashAccountTransactionEditDialogMapper cashAccountTransactionEditDialogMapper;
    private final CashAccountTransactionService cashAccountTransactionService;
    private final AsyncProcessor asyncProcessor;
    private final DtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper dtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper;
    private final CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator;
    private final CashAccountTransactionImportStep3Populator cashAccountTransactionImportStep3Populator;

    @FXML private TableViewEx<DtoCashAccountTransactionOverview, CashAccountTransactionOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CashAccountController parentController;

    @Autowired
    public CashAccountTransactionsController(
            CategoryService categoryService,
            CashAccountTransactionEditDialogMapper cashAccountTransactionEditDialogMapper,
            CashAccountTransactionService cashAccountTransactionService,
            AsyncProcessor asyncProcessor,
            DtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper dtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper,
            CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator,
            CashAccountTransactionImportStep3Populator cashAccountTransactionImportStep3Populator) {

        this.categoryService = categoryService;
        this.cashAccountTransactionEditDialogMapper = cashAccountTransactionEditDialogMapper;
        this.cashAccountTransactionService = cashAccountTransactionService;
        this.asyncProcessor = asyncProcessor;
        this.dtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper = dtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper;
        this.cashAccountTransactionImportStep2Populator = cashAccountTransactionImportStep2Populator;
        this.cashAccountTransactionImportStep3Populator = cashAccountTransactionImportStep3Populator;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);

        tableView.setDoubleClickConsumer(dtoCashAccountTransactionOverview -> onEditTransaction());
    }

    @Override
    public void afterInitialize(CashAccountController parentController) {
        this.parentController = parentController;

        long cashAccountId = parentController.getSelectedCashAccount().getId();
        tableView.setDataProvider((monitor, pagingParams) -> cashAccountTransactionService.loadOverview(monitor, cashAccountId, pagingParams));
    }

    @FXML
    public void onNewTransaction() {
        createOrEditCashAccountTransaction(null);
    }

    @FXML
    public void onEditTransaction() {
        var selectedCashAccountTransaction = tableView.getSelectionModel().getSelectedItem();
        createOrEditCashAccountTransaction(selectedCashAccountTransaction);
    }

    @FXML
    public void onDeleteTransaction() {
        var selectedCashAccountTransaction = tableView.getSelectionModel().getSelectedItem();

        if (DialogHelper.showConfirmDialog(MessageKey.CASHACCOUNT_TRANSACTION_CONFIRM_DELETE)) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> cashAccountTransactionService.markAsDeleted(monitor, selectedCashAccountTransaction.getId()),
                    () -> tableView.reload()
            );
        }
    }

    @FXML
    public void onImportTransactions() {
        WizardBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_TITLE)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP1_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP1)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP2_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP2, cashAccountTransactionImportStep2Populator)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP3_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP3, cashAccountTransactionImportStep3Populator)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4)
                .build()
                .showAndWait();
    }

    private void createOrEditCashAccountTransaction(DtoCashAccountTransactionOverview input) {
        long cashAccountId = parentController.getSelectedCashAccount().getId();

        Optional<DtoCashAccountTransaction> dtoCashAccountTransaction = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_TITLE)
                .addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CASH_ACCOUNT_TRANSACTION_EDIT)
                .withMapper(cashAccountTransactionEditDialogMapper)
                .withInput(dtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper.map(input))
                .withAsyncDataLoading(monitor -> ImmutablePair.of(categoryService.loadAllCategories(monitor), cashAccountId))
                .build()
                .showAndWait();

        if (dtoCashAccountTransaction.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> cashAccountTransactionService.saveOrUpdate(monitor, cashAccountId, dtoCashAccountTransaction.get()),
                    () -> tableView.reload()
            );
        }
    }
}
