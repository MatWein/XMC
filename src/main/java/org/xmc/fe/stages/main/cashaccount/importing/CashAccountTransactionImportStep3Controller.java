package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationNumberField;

@FxmlController
public class CashAccountTransactionImportStep3Controller {
    @FXML private TableView<DtoColumnMapping<CashAccountTransactionImportColmn>> columnMappingTable;
    @FXML private ValidationNumberField startWithLineNumberField;

    public TableView<DtoColumnMapping<CashAccountTransactionImportColmn>> getColumnMappingTable() {
        return columnMappingTable;
    }

    public ValidationNumberField getStartWithLineNumberField() {
        return startWithLineNumberField;
    }
}
