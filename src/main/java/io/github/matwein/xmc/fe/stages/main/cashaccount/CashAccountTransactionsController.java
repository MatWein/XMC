package io.github.matwein.xmc.fe.stages.main.cashaccount;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.cashaccount.CashAccountTransactionImportService;
import io.github.matwein.xmc.be.services.cashaccount.CashAccountTransactionService;
import io.github.matwein.xmc.be.services.category.CategoryService;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep2Populator;
import io.github.matwein.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep3Populator;
import io.github.matwein.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep4Populator;
import io.github.matwein.xmc.fe.stages.main.cashaccount.mapper.CashAccountTransactionEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import io.github.matwein.xmc.fe.ui.wizard.WizardBuilder;

import java.util.Optional;
import java.util.stream.Collectors;

@FxmlController
public class CashAccountTransactionsController implements IAfterInit<CashAccountController> {
    private final CategoryService categoryService;
    private final CashAccountTransactionEditDialogMapper cashAccountTransactionEditDialogMapper;
    private final CashAccountTransactionService cashAccountTransactionService;
    private final AsyncProcessor asyncProcessor;
    private final CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator;
    private final CashAccountTransactionImportStep3Populator cashAccountTransactionImportStep3Populator;
    private final CashAccountTransactionImportStep4Populator cashAccountTransactionImportStep4Populator;
	private final CashAccountTransactionImportService cashAccountTransactionImportService;
	
	@FXML private ExtendedTable<DtoCashAccountTransactionOverview, CashAccountTransactionOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CashAccountController parentController;

    @Autowired
    public CashAccountTransactionsController(
		    CategoryService categoryService,
		    CashAccountTransactionEditDialogMapper cashAccountTransactionEditDialogMapper,
		    CashAccountTransactionService cashAccountTransactionService,
		    AsyncProcessor asyncProcessor,
		    CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator,
		    CashAccountTransactionImportStep3Populator cashAccountTransactionImportStep3Populator,
		    CashAccountTransactionImportStep4Populator cashAccountTransactionImportStep4Populator,
		    CashAccountTransactionImportService cashAccountTransactionImportService) {

        this.categoryService = categoryService;
        this.cashAccountTransactionEditDialogMapper = cashAccountTransactionEditDialogMapper;
        this.cashAccountTransactionService = cashAccountTransactionService;
        this.asyncProcessor = asyncProcessor;
        this.cashAccountTransactionImportStep2Populator = cashAccountTransactionImportStep2Populator;
        this.cashAccountTransactionImportStep3Populator = cashAccountTransactionImportStep3Populator;
        this.cashAccountTransactionImportStep4Populator = cashAccountTransactionImportStep4Populator;
	    this.cashAccountTransactionImportService = cashAccountTransactionImportService;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();

        SimpleBooleanProperty multipleTableItemsSelected = new SimpleBooleanProperty(false);
        tableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<DtoCashAccountTransactionOverview>)
		        change -> multipleTableItemsSelected.set(change.getList().size() > 1));

        editButton.disableProperty().bind(noTableItemSelected.or(multipleTableItemsSelected));
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
        var selectedCashAccountTransactionIds = tableView.getSelectionModel().getSelectedItems()
                .stream()
                .map(DtoCashAccountTransactionOverview::getId)
                .collect(Collectors.toSet());

        if (DialogHelper.showConfirmDialog(MessageKey.CASHACCOUNT_TRANSACTION_CONFIRM_DELETE)) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> cashAccountTransactionService.markAsDeleted(monitor, selectedCashAccountTransactionIds),
                    () -> tableView.reload()
            );
        }
    }

    @FXML
    public void onImportTransactions() {
	    var importData = new DtoImportData<CashAccountTransactionImportColmn>();
	    long cashAccountId = parentController.getSelectedCashAccount().getId();
	
	    boolean allStepsFinished = WizardBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_TITLE)
                .withInput(importData)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP1_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP1)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP2_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP2, cashAccountTransactionImportStep2Populator)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP3_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP3, cashAccountTransactionImportStep3Populator)
                .addStep(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_TITLE, FxmlKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4, cashAccountTransactionImportStep4Populator)
                .buildAndShow();
	
	    if (allStepsFinished) {
		    asyncProcessor.runAsyncVoid(
				    () -> {},
				    monitor -> cashAccountTransactionImportService.importTransactions(monitor, cashAccountId, importData),
				    () -> tableView.reload()
		    );
	    }
    }

    private void createOrEditCashAccountTransaction(DtoCashAccountTransactionOverview input) {
        long cashAccountId = parentController.getSelectedCashAccount().getId();

        Optional<DtoCashAccountTransaction> dtoCashAccountTransaction = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_TITLE)
                .addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CASH_ACCOUNT_TRANSACTION_EDIT)
                .withMapper(cashAccountTransactionEditDialogMapper)
                .withInput(input)
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
