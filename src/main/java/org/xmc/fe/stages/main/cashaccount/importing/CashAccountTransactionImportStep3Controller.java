package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.common.stubs.cashaccount.transactions.importing.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationNumberField;

@FxmlController
public class CashAccountTransactionImportStep3Controller {
    private final DtoColumnMappingFactory dtoColumnMappingFactory;

    @FXML private TableView<DtoColumnMapping<CashAccountTransactionImportColmn>> columnMappingTable;
    @FXML private ValidationNumberField startWithLineNumberField;

    @Autowired
    public CashAccountTransactionImportStep3Controller(DtoColumnMappingFactory dtoColumnMappingFactory) {
        this.dtoColumnMappingFactory = dtoColumnMappingFactory;
    }

    @FXML
    public void initialize() {
        columnMappingTable.getItems().addAll(dtoColumnMappingFactory.generateEmptyColumns());
    }

    public TableView<DtoColumnMapping<CashAccountTransactionImportColmn>> getColumnMappingTable() {
        return columnMappingTable;
    }

    public ValidationNumberField getStartWithLineNumberField() {
        return startWithLineNumberField;
    }
}
