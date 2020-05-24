package org.xmc.fe.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages", Locale.getDefault());

    public static String getByKey(MessageKey key) {
        try {
            return RESOURCE_BUNDLE.getString(key.getKey());
        } catch (MissingResourceException e) {
            LOGGER.warn("Message key not found: {}", key.getKey(), e);
            return key.getKey();
        }
    }

    public enum MessageKey {
        APP_NAME("app.name"),
        LOGIN_TITLE("login.title")
        ;

        private final String key;

        MessageKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
