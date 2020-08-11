package org.xmc.fe.stages.main.cashaccount;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.category.CategoryService;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.TableViewEx;

import java.util.Optional;

@FxmlController
public class CashAccountTransactionsController implements IAfterInit<CashAccountController> {
    private final CategoryService categoryService;

    @FXML private TableViewEx<DtoCashAccountTransactionOverview, CashAccountTransactionOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CashAccountController parentController;

    @Autowired
    public CashAccountTransactionsController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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
        createOrEditCashAccountTransaction(null);
    }

    @FXML
    public void onEditTransaction() {
        DtoCashAccountTransactionOverview selectedCashAccountTransaction = tableView.getSelectionModel().getSelectedItem();
        createOrEditCashAccountTransaction(selectedCashAccountTransaction);
    }

    @FXML
    public void onDeleteTransaction() {
    }

    private void createOrEditCashAccountTransaction(DtoCashAccountTransactionOverview input) {
        Optional<DtoCashAccountTransaction> dtoCashAccountTransaction = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_TITLE)
                .addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CASH_ACCOUNT_TRANSACTION_EDIT)
//                .withMapper(cashAccountEditDialogMapper)
                .withInput(input)
                .withAsyncDataLoading(categoryService::loadAllCategories)
                .build()
                .showAndWait();

        if (dtoCashAccountTransaction.isPresent()) {
//            asyncProcessor.runAsyncVoid(
//                    () -> {},
//                    monitor -> cashAccountService.saveOrUpdate(monitor, dtoCashAccount.get()),
//                    () -> tableView.reload()
//            );
        }
    }
}
