package io.github.matwein.xmc.fe.stages.main.administration.banks;

import io.github.matwein.xmc.common.services.bank.IBankService;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.administration.banks.mapper.BankEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@FxmlController
public class BankController {
    private final IBankService bankService;
    private final BankEditDialogMapper bankEditDialogMapper;
    private final AsyncProcessor asyncProcessor;

    @FXML private ExtendedTable<DtoBankOverview, BankOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Autowired
    public BankController(
		    IBankService bankService,
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
        tableView.setDoubleClickConsumer(dtoBankOverview -> onEditBank());
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
