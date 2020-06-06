package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.BreadcrumbBar;
import org.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;

@Component
public class CashAccountController {
    @FXML private BreadcrumbBar<?> breadcrumbBar;

    @FXML
    private void initialize() {
        breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW)));
    }
}
