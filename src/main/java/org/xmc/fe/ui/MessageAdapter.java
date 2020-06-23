package org.xmc.fe.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages", Locale.getDefault());

    public static String getByKey(MessageKey key, Object... args) {
        if (key == null) {
            return null;
        }

        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key.getKey()), args);
        } catch (MissingResourceException e) {
            LOGGER.warn("Message key not found: {}", key.getKey(), e);
            return key.getKey();
        }
    }

    public enum MessageKey {
        APP_NAME("app.name"),
        PASSWORD("password"),
        TABLE_NO_CONTENT("table.noContent"),
        DIALOG_OK("dialog.ok"),

        VALIDATION_REQUIRED("validation.required"),
        VALIDATION_MIN_LENGTH("validation.minLength"),
        VALIDATION_MAX_LENGTH("validation.maxLength"),
        VALIDATION_NOT_EQUAL_TO("validation.notEqualTo"),
        VALIDATION_USER_ALREADY_EXISTS("validation.userAlreadyExists"),
        VALIDATION_USER_NOT_EXISTING("validation.userNotExisting"),

        LOGIN_TITLE("login.title"),

        BOOTSTRAP_STATUS_CREATING_CONTEXT("bootstrap.status.creatingContext"),
        BOOTSTRAP_STATUS_PREPROCESSING("bootstrap.status.preprocessing"),
        BOOTSTRAP_STATUS_LOGIN("bootstrap.status.login"),

        MAIN_MEMORY("main.memory"),
        MAIN_DISPLAYNAME("main.displayname"),
        MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW("main.cashaccounts.breadcrumb.overview"),

        ABOUT_TITLE("about.title"),

        CASHACCOUNT_EDIT_TITLE("cashaccount.edit.title"),
        CASHACCOUNT_EDIT_SAVE("cashaccount.edit.save"),
        CASHACCOUNT_EDIT_CANCEL("cashaccount.edit.cancel"),
        CASHACCOUNT_EDIT_ADD_BANK("cashaccount.edit.addBank"),
        CASHACCOUNT_EDIT_BANK_LOGO("cashaccount.edit.bank.logo")
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
