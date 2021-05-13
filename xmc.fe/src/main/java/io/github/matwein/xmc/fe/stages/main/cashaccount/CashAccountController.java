package io.github.matwein.xmc.fe.stages.main.cashaccount;

import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.components.BreadcrumbBar;
import io.github.matwein.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;

@FxmlController
public class CashAccountController {
    private static final String CASHACCOUNT_CONTENT_CONTAINER_ID = "#cashaccountContentContainer";

    @FXML private VBox rootVbox;
    @FXML private BreadcrumbBar<String> breadcrumbBar;

    private DtoCashAccountOverview selectedCashAccount;

    @FXML
    public void initialize() {
        switchToOverview();
    }

    public void switchToOverview() {
        this.selectedCashAccount = null;

        breadcrumbBar.getElements().clear();

        BreadcrumbPathElement<String> element = new BreadcrumbPathElement<>(MessageAdapter.getByKey(MessageKey.MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW));
        element.setOnAction(actionEvent -> switchToOverview());
        breadcrumbBar.getElements().add(element);

        switchContentComponent(FxmlKey.CASH_ACCOUNTS_OVERVIEW);
    }

    public void switchToTransactions(DtoCashAccountOverview selectedCashAccount) {
        this.selectedCashAccount = selectedCashAccount;

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

    public DtoCashAccountOverview getSelectedCashAccount() {
        return selectedCashAccount;
    }
}
