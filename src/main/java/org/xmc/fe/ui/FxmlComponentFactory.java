package org.xmc.fe.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.common.utils.ReflectionUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FxmlComponentFactory {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static <COMPONENT_TYPE extends Parent, CONTROLLER_TYPE> Pair<COMPONENT_TYPE, CONTROLLER_TYPE> load(FxmlKey fxmlKey) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setCharset(CHARSET);
            fxmlLoader.setResources(MessageAdapter.RESOURCE_BUNDLE);
            fxmlLoader.setLocation(FxmlComponentFactory.class.getResource(fxmlKey.getFxmlPath()));
            fxmlLoader.setControllerFactory(ReflectionUtil.createNewInstanceFactory());

            COMPONENT_TYPE component = fxmlLoader.load();
            CONTROLLER_TYPE controller = fxmlLoader.getController();
            component.setUserData(controller);

            return ImmutablePair.of(component, controller);
        } catch (Throwable e) {
            String message = String.format("Error on loading fxml file: %s", fxmlKey.getFxmlPath());
            throw new RuntimeException(message, e);
        }
    }

    public enum FxmlKey {
        LOGIN("/fxml/login/login.fxml"),
        LOGIN_REGISTER("/fxml/login/register.fxml"),
        BOOTSTRAP("/fxml/login/bootstrap.fxml"),
        MAIN("/fxml/main/main.fxml"),
        ABOUT("/fxml/main/about.fxml"),
	    CHANGELOG("/fxml/main/changelog.fxml"),
	    PROTOCOL("/fxml/main/protocol.fxml"),
        CASH_ACCOUNT_EDIT("/fxml/main/cashaccount/cashaccount-edit.fxml"),
        CASH_ACCOUNTS_OVERVIEW("/fxml/main/cashaccount/cashaccounts-overview.fxml"),
        CASH_ACCOUNT_TRANSACTIONS("/fxml/main/cashaccount/cashaccount-transactions.fxml"),
        CASH_ACCOUNT_TRANSACTION_EDIT("/fxml/main/cashaccount/cashaccount-transaction-edit.fxml"),
        BANK_EDIT("/fxml/main/administration/bank-edit.fxml"),
        CATEGORY_EDIT("/fxml/main/administration/category-edit.fxml"),
	    STOCK_CATEGORY_EDIT("/fxml/main/administration/stock-category-edit.fxml"),
        WIZARD("/fxml/wizard/wizard.fxml"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP1("/fxml/main/cashaccount/importing/cashaccount-transaction-import-step1.fxml"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP2("/fxml/main/cashaccount/importing/cashaccount-transaction-import-step2.fxml"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP3("/fxml/main/cashaccount/importing/cashaccount-transaction-import-step3.fxml"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4("/fxml/main/cashaccount/importing/cashaccount-transaction-import-step4.fxml"),
        SETTINGS("/fxml/main/settings/settings.fxml"),
        SETTINGS_IMPORT_TEMPLATES("/fxml/main/settings/content/settings-importtemplates.fxml"),
        SETTINGS_EXTRAS("/fxml/main/settings/content/settings-extras.fxml"),
	    STOCK_EDIT("/fxml/main/administration/stock-edit.fxml"),
	    CURRENCY_CONVERSION_FACTOR_EDIT("/fxml/main/administration/currency-conversion-factor-edit.fxml"),
	    DEPOTS_OVERVIEW("/fxml/main/depot/depots-overview.fxml"),
	    DEPOT_TRANSACTIONS("/fxml/main/depot/depot-transactions.fxml"),
	    DEPOT_DELIVERIES("/fxml/main/depot/depot-deliveries.fxml"),
	    DEPOT_DEPOTITEMS("/fxml/main/depot/depot-items.fxml"),
	    DEPOT_EDIT("/fxml/main/depot/depot-edit.fxml"),
	    DEPOT_TRANSACTION_EDIT("/fxml/main/depot/depot-transaction-edit.fxml"),
	    DEPOT_DELIVERY_EDIT("/fxml/main/depot/depot-delivery-edit.fxml"),
	    DEPOT_ITEM_EDIT("/fxml/main/depot/depot-item-edit.fxml"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP1("/fxml/main/depot/importing/deliveries/depot-delivery-import-step1.fxml"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP2("/fxml/main/depot/importing/deliveries/depot-delivery-import-step2.fxml"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP3("/fxml/main/depot/importing/deliveries/depot-delivery-import-step3.fxml"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP4("/fxml/main/depot/importing/deliveries/depot-delivery-import-step4.fxml"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP1("/fxml/main/depot/importing/items/depot-item-import-step1.fxml"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP2("/fxml/main/depot/importing/items/depot-item-import-step2.fxml"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP3("/fxml/main/depot/importing/items/depot-item-import-step3.fxml"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP4("/fxml/main/depot/importing/items/depot-item-import-step4.fxml"),
        ;

        private final String fxmlPath;

        FxmlKey(String fxmlPath) {
            this.fxmlPath = fxmlPath;
        }

        public String getFxmlPath() {
            return fxmlPath;
        }
    }
}
