package org.xmc.fe.stages.main.administration.banks;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.bank.BankService;
import org.xmc.common.stubs.bank.BankOverviewFields;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.common.stubs.bank.DtoBankOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.administration.banks.mapper.BankEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.TableViewEx;

import java.util.Optional;

@FxmlController
public class BankController {
    private final BankService bankService;
    private final BankEditDialogMapper bankEditDialogMapper;
    private final AsyncProcessor asyncProcessor;

    @FXML private TableViewEx<DtoBankOverview, BankOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Autowired
    public BankController(
            BankService bankService,
            BankEditDialogMapper bankEditDialogMapper,
            AsyncProcessor asyncProcessor) {

        this.bankService = bankService;
        this.bankEditDialogMapper = bankEditDialogMapper;
        this.asyncProcessor = asyncProcessor;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);
        tableView.setDataProvider(bankService::loadOverview);
    }

    @FXML
    public void onNewBank() {
        createOrEditBank(null);
    }

    @FXML
    public void onEditBank() {
        DtoBankOverview selectedBank = tableView.getSelectionModel().getSelectedItem();
        createOrEditBank(selectedBank);
    }

    @FXML
    public void onDeleteBank() {
        DtoBankOverview selectedBank = tableView.getSelectionModel().getSelectedItem();

        if (DialogHelper.showConfirmDialog(MessageKey.BANK_CONFIRM_DELETE, selectedBank.getName())) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> bankService.markAsDeleted(monitor, selectedBank.getId()),
                    () -> tableView.reload()
            );
        }
    }

    private void createOrEditBank(DtoBankOverview input) {
        Optional<DtoBank> dtoBank = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.BANK_EDIT_TITLE)
                .addButton(MessageKey.BANK_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.BANK_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.BANK_EDIT)
                .withMapper(bankEditDialogMapper)
                .withInput(input)
                .build()
                .showAndWait();

        if (dtoBank.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> bankService.saveOrUpdate(monitor, dtoBank.get()),
                    () -> tableView.reload()
            );
        }
    }
}
