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
        CASH_ACCOUNT_EDIT("/fxml/main/cashaccount/cashaccount-edit.fxml"),
        CASH_ACCOUNTS_OVERVIEW("/fxml/main/cashaccount/cashaccounts-overview.fxml"),
        CASH_ACCOUNT_TRANSACTIONS("/fxml/main/cashaccount/cashaccount-transactions.fxml"),
        BANK_EDIT("/fxml/main/administration/bank-edit.fxml"),
        CATEGORY_EDIT("/fxml/main/administration/category-edit.fxml")
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
