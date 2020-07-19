package org.xmc.fe.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    static ResourceBundle RESOURCE_BUNDLE = initBundle();

    static ResourceBundle initBundle() {
        return RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages", Locale.getDefault());
    }

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
        FILECHOOSER_IMAGES("filechooser.extensionfilter.images"),
        FILECHOOSER_TITLE("filechooser.title"),

        VALIDATION_REQUIRED("validation.required"),
        VALIDATION_MIN_LENGTH("validation.minLength"),
        VALIDATION_MAX_LENGTH("validation.maxLength"),
        VALIDATION_NOT_EQUAL_TO("validation.notEqualTo"),
        VALIDATION_USER_ALREADY_EXISTS("validation.userAlreadyExists"),
        VALIDATION_USER_NOT_EXISTING("validation.userNotExisting"),
        VALIDATION_INVALID_IBAN("validation.invalidIban"),
        VALIDATION_INVALID_CURRENCY("validation.invalidCurrency"),

        PAGING_FIRST_PAGE("paging.firstPage"),
        PAGING_BACK("paging.back"),
        PAGING_NEXT("paging.next"),
        PAGING_LAST_PAGE("paging.lastPage"),
        PAGING_COUNT("paging.count"),
        PAGING_FILTER_PROMPT("paging.filterPrompt"),

        ASYNC_TASK_LOAD_CASHACCOUNT_OVERVIEW("async.task.loadCashAccountOverview"),
        ASYNC_TASK_SAVE_CASHACCOUNT("async.task.saveCashAccount"),
        ASYNC_TASK_LOAD_BANK_OVERVIEW("async.task.loadBankOverview"),
        ASYNC_TASK_SAVE_BANK("async.task.saveBank"),
        ASYNC_TASK_LOAD_ALL_BANKS("async.task.loadAllBanks"),
        ASYNC_TASK_DELETE_BANK("async.task.deleteBank"),
        ASYNC_TASK_DELETE_CASHACCOUNT("async.task.deleteCashAccount"),

        LOGIN_TITLE("login.title"),

        BOOTSTRAP_STATUS_CREATING_CONTEXT("bootstrap.status.creatingContext"),
        BOOTSTRAP_STATUS_PREPROCESSING("bootstrap.status.preprocessing"),
        BOOTSTRAP_STATUS_LOGIN("bootstrap.status.login"),
        BOOTSTRAP_STATUS_LOGIN_FINISHED("bootstrap.status.loginFinished"),

        MAIN_MEMORY("main.memory"),
        MAIN_DISPLAYNAME("main.displayname"),
        MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW("main.cashaccounts.breadcrumb.overview"),
        MAIN_PROCESS_COUNTER("main.processCounter"),
        MAIN_NEW_PROCESS("main.newProcess"),

        ABOUT_TITLE("about.title"),

        CASHACCOUNT_EDIT_TITLE("cashaccount.edit.title"),
        CASHACCOUNT_EDIT_SAVE("cashaccount.edit.save"),
        CASHACCOUNT_EDIT_CANCEL("cashaccount.edit.cancel"),
        CASHACCOUNT_EDIT_SELECT_BANK("cashaccount.edit.selectBank"),
        CASHACCOUNT_EDIT_BANK_LOGO("cashaccount.edit.bank.logo"),
        CASHACCOUNT_CONFIRM_DELETE("cashaccount.confirm.delete"),

        BANK_EDIT_TITLE("bank.edit.title"),
        BANK_EDIT_CANCEL("bank.edit.cancel"),
        BANK_EDIT_SAVE("bank.edit.save"),
        BANK_CONFIRM_DELETE("bank.confirm.delete"),
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
