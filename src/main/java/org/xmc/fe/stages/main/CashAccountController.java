package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.BreadcrumbBar;
import org.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;
import org.xmc.fe.ui.components.TableViewEx;

@Component
public class CashAccountController {
    @FXML private BreadcrumbBar<?> breadcrumbBar;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private TableViewEx tableView;

    @FXML
    public void initialize() {
        breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW)));

        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }
}
