package org.xmc.fe.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.Main;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FxmlComponentFactory {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static <COMPONENT_TYPE extends Parent, C> Pair<COMPONENT_TYPE, C> load(FxmlKey fxmlKey) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setCharset(CHARSET);
            fxmlLoader.setResources(MessageAdapter.RESOURCE_BUNDLE);
            fxmlLoader.setLocation(FxmlComponentFactory.class.getResource(fxmlKey.getFxmlPath()));

            if (Main.applicationContext == null) {
                fxmlLoader.setControllerFactory(FxmlComponentFactory::createNewInstance);
            } else {
                fxmlLoader.setControllerFactory(type -> Main.applicationContext.getBean(type));
            }

            COMPONENT_TYPE component = fxmlLoader.load();
            return ImmutablePair.of(component, fxmlLoader.getController());
        } catch (Throwable e) {
            String message = String.format("Error on loading fxml file: %s", fxmlKey.getFxmlPath());
            throw new RuntimeException(message, e);
        }
    }

    private static Object createNewInstance(Class<?> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            String message = String.format("Error on creating instance of '%s'.", type.getName());
            throw new RuntimeException(message, e);
        }
    }

    public enum FxmlKey {
        LOGIN("/fxml/login/login.fxml"),
        LOGIN_REGISTER("/fxml/login/register.fxml"),
        BOOTSTRAP("/fxml/login/bootstrap.fxml")
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
