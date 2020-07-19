package org.xmc.fe.stages.main.cashaccount;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.BreadcrumbBar;
import org.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;

@FxmlController
public class CashAccountController {
    private static final String CASHACCOUNT_CONTENT_CONTAINER_ID = "#cashaccountContentContainer";

    @FXML private VBox rootVbox;
    @FXML private BreadcrumbBar<String> breadcrumbBar;

    @FXML
    public void initialize() {
        switchToOverview();
    }

    public void switchToOverview() {
        breadcrumbBar.getElements().clear();

        BreadcrumbPathElement<String> element = new BreadcrumbPathElement<>(MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW));
        element.setOnAction(actionEvent -> switchToOverview());
        breadcrumbBar.getElements().add(element);

        switchContentComponent(FxmlKey.CASH_ACCOUNTS_OVERVIEW);
    }

    public void switchToTransactions(DtoCashAccountOverview selectedCashAccount) {
        String text = MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_TRANSACTIONS, selectedCashAccount.getName());
        breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(text));

        switchContentComponent(FxmlKey.CASH_ACCOUNT_TRANSACTIONS);
    }

    private void switchContentComponent(FxmlKey newComponentKey) {
        rootVbox.getChildren().remove(rootVbox.lookup(CASHACCOUNT_CONTENT_CONTAINER_ID));

        Pair<Parent, ? extends IAfterInit<CashAccountController>> componentPair = FxmlComponentFactory.load(newComponentKey);
        componentPair.getRight().afterInitialize(this);
        rootVbox.getChildren().add(componentPair.getLeft());
    }
}
