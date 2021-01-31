package org.xmc.fe.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.CsvSeparator;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    static ResourceBundle RESOURCE_BUNDLE = initBundle();

    public static ResourceBundle initBundle() {
        return RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages", Locale.getDefault());
    }

    public static String getByKey(MessageKey key, Object... args) {
        if (key == null) {
            return null;
        }

        return getByKey(key.getKey(), args);
    }

    public static String getByKey(MessageKey prefix, Enum<?> enumValue, Object... args) {
        if (prefix == null || enumValue == null) {
            return null;
        }

        String key = prefix.getKey() + "." + enumValue.name();
        return getByKey(key, args);
    }
    
    public static String formatDate(LocalDate date) {
    	if (date == null) {
    		return null;
	    }
    	
    	String dateFormat = getByKey(MessageKey.APP_DATE_FORMAT);
    	return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }
	
	public static String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			return null;
		}
		
		String dateTimeFormat = getByKey(MessageKey.APP_DATETIME_FORMAT);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat));
	}

    private static String getByKey(String key, Object... args) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args);
        } catch (MissingResourceException e) {
            LOGGER.warn("Message key not found: {}", key, e);
            return key;
        }
    }
    
    public enum MessageKey {
        APP_NAME("app.name"),
        APP_DATE_FORMAT("app.dateFormat"),
        APP_DATETIME_FORMAT("app.dateTimeFormat"),
        PASSWORD("password"),
        TABLE_NO_CONTENT("table.noContent"),
        DIALOG_OK("dialog.ok"),
        DIALOG_CLOSE("dialog.close"),
        FILECHOOSER_IMAGES("filechooser.extensionfilter.images"),
        FILECHOOSER_CSV_EXCEL("filechooser.extensionfilter.csvExcel"),
        FILECHOOSER_ALL("filechooser.extensionfilter.all"),
        FILECHOOSER_TITLE("filechooser.title"),
        FILECHOOSER_SELECT_FILE("filechooser.selectFile"),

        VALIDATION_REQUIRED("validation.required"),
        VALIDATION_MIN_LENGTH("validation.minLength"),
        VALIDATION_MAX_LENGTH("validation.maxLength"),
        VALIDATION_MIN("validation.min"),
        VALIDATION_MAX("validation.max"),
        VALIDATION_NUMBER_PARSE_ERROR("validation.numberParseError"),
        VALIDATION_NOT_EQUAL_TO("validation.notEqualTo"),
        VALIDATION_USER_ALREADY_EXISTS("validation.userAlreadyExists"),
        VALIDATION_USER_NOT_EXISTING("validation.userNotExisting"),
        VALIDATION_INVALID_IBAN("validation.invalidIban"),
        VALIDATION_INVALID_CURRENCY("validation.invalidCurrency"),
        VALIDATION_INVALID_DATE("validation.invalidDate"),
        VALIDATION_ZERO_NOT_ALLOWED("validation.zeroNotAllowed"),
        VALIDATION_INVALID_FILE_PATH("validation.invalidFilePath"),
        VALIDATION_INVALID_FILE_EXTENSION("validation.invalidFileExtension"),
	    VALIDATION_INVALID_ENCODING("validation.invalidEncoding"),
	    VALIDATION_STOCK_CATEGORY_ALREADY_EXISTS("validation.stockCategoryAlreadyExists"),
	    VALIDATION_STOCK_ISIN_ALREADY_EXISTS("validation.stockIsinAlreadyExists"),

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
        ASYNC_TASK_SAVE_CATEGORY("async.task.saveCategory"),
        ASYNC_TASK_LOAD_CATEGORY_OVERVIEW("async.task.loadCategoryOverview"),
        ASYNC_TASK_DELETE_CATEGORY("async.task.deleteCategory"),
        ASYNC_TASK_LOAD_ALL_CATEGORIES("async.task.loadAllCategories"),
        ASYNC_TASK_DELETE_CASHACCOUNT_TRANSACTIONS("async.task.deleteCashAccountTransactions"),
        ASYNC_TASK_LOAD_CASHACCOUNT_TRANSACTION_OVERVIEW("async.task.loadCashAccountTransactionOverview"),
        ASYNC_TASK_SAVE_CASHACCOUNT_TRANSACTION("async.task.saveCashAccountTransaction"),
        ASYNC_TASK_DETECT_CASHACCOUNT_TRANSACTION_CATEGORY("async.task.detectCashAccountTransactionCategory"),
        ASYNC_TASK_VALIDATE_IMPORT_FILE("async.task.validateImportFile"),
	    ASYNC_TASK_READ_IMPORT_FILE("async.task.readImportFile"),
	    ASYNC_TASK_MAP_IMPORT_FILE("async.task.mapImportFile"),
	    ASYNC_TASK_SAVE_IMPORT_TEMPLATE("async.task.saveImportTemplate"),
	    ASYNC_TASK_IMPORTING_TRANSACTIONS("async.task.importingTransactions"),
	    ASYNC_TASK_LOAD_IMPORT_TEMPLATES("async.task.loadImportTemplates"),
	    ASYNC_TASK_RENAME_IMPORT_TEMPLATE("async.task.renameImportTemplate"),
	    ASYNC_TASK_DELETE_IMPORT_TEMPLATE("async.task.deleteImportTemplate"),
	    ASYNC_TASK_LOAD_SETTING("async.task.loadSetting"),
	    ASYNC_TASK_SAVE_SETTING("async.task.saveSetting"),
	    ASYNC_TASK_LOAD_ALL_STOCK_CATEGORIES("async.task.loadAllStockCategories"),
	    ASYNC_TASK_DELETE_STOCK("async.task.deleteStock"),
	    ASYNC_TASK_LOAD_STOCK_OVERVIEW("async.task.loadStockOverview"),
	    ASYNC_TASK_SAVE_STOCK("async.task.saveStock"),
	    ASYNC_TASK_DELETE_CURRENCY_CONVERSION_FACTOR("async.task.deleteCurrencyConversionFactor"),
	    ASYNC_TASK_SAVE_CURRENCY_CONVERSION_FACTOR("async.task.saveCurrencyConversionFactor"),
	    ASYNC_TASK_LOAD_CURRENCY_CONVERSION_FACTOR_OVERVIEW("async.task.loadCurrencyConversionFactorOverview"),
	    ASYNC_TASK_LOAD_DEPOT_OVERVIEW("async.task.loadDepotOverview"),
	    ASYNC_TASK_SAVE_DEPOT("async.task.saveDepot"),
	    ASYNC_TASK_DELETE_DEPOT("async.task.deleteDepot"),
	    ASYNC_TASK_DELETE_DEPOT_TRANSACTIONS("async.task.deleteDepotTransactions"),
	    ASYNC_TASK_LOAD_DEPOT_TRANSACTION_OVERVIEW("async.task.loadDepotTransactionOverview"),
	    ASYNC_TASK_SAVE_DEPOT_TRANSACTION("async.task.saveDepotTransaction"),
	    ASYNC_TASK_LOAD_DEPOT_DELIVERY_OVERVIEW("async.task.loadDepotDeliveryOverview"),
	    ASYNC_TASK_DELETE_DEPOT_DELIVERY("async.task.deleteDepotDelivery"),
	    ASYNC_TASK_SAVE_DEPOT_DELIVERY("async.task.saveDepotDelivery"),
	    ASYNC_TASK_LOAD_DEPOT_ITEM_OVERVIEW("async.task.loadDepotItemOverview"),
	    ASYNC_TASK_SAVE_DEPOT_ITEM("async.task.saveDepotItem"),
	    ASYNC_TASK_DELETE_DEPOT_ITEMS("async.task.deleteDepotItems"),

        LOGIN_TITLE("login.title"),

        BOOTSTRAP_STATUS_CREATING_CONTEXT("bootstrap.status.creatingContext"),
        BOOTSTRAP_STATUS_PREPROCESSING("bootstrap.status.preprocessing"),
        BOOTSTRAP_STATUS_LOGIN("bootstrap.status.login"),
        BOOTSTRAP_STATUS_LOGIN_FINISHED("bootstrap.status.loginFinished"),

        MAIN_MEMORY("main.memory"),
        MAIN_DISPLAYNAME("main.displayname"),
        MAIN_CASHACCOUNTS_BREADCRUMB_OVERVIEW("main.cashaccounts.breadcrumb.overview"),
        MAIN_CASHACCOUNTS_BREADCRUMB_TRANSACTIONS("main.cashaccounts.breadcrumb.transactions"),
	    MAIN_DEPOT_BREADCRUMB_OVERVIEW("main.depots.breadcrumb.overview"),
	    MAIN_DEPOT_BREADCRUMB_TRANSACTIONS("main.depots.breadcrumb.transactions"),
	    MAIN_DEPOT_BREADCRUMB_DELIVERIES("main.depots.breadcrumb.deliveries"),
	    MAIN_DEPOT_BREADCRUMB_DEPOTITEMS("main.depots.breadcrumb.depotitems"),
        MAIN_PROCESS_COUNTER("main.processCounter"),
        MAIN_NEW_PROCESS("main.newProcess"),

        ABOUT_TITLE("about.title"),
	    CHANGELOG_TITLE("changelog.title"),
	    SETTINGS_TITLE("settings.title"),

        CASHACCOUNT_EDIT_TITLE("cashaccount.edit.title"),
        CASHACCOUNT_EDIT_SAVE("cashaccount.edit.save"),
        CASHACCOUNT_EDIT_CANCEL("cashaccount.edit.cancel"),
        CASHACCOUNT_EDIT_SELECT_BANK("cashaccount.edit.selectBank"),
        CASHACCOUNT_EDIT_BANK_LOGO("cashaccount.edit.bank.logo"),
        CASHACCOUNT_CONFIRM_DELETE("cashaccount.confirm.delete"),
        CASHACCOUNT_TRANSACTION_EDIT_TITLE("cashaccount.transaction.edit.title"),
        CASHACCOUNT_TRANSACTION_EDIT_CANCEL("cashaccount.transaction.edit.cancel"),
        CASHACCOUNT_TRANSACTION_EDIT_SAVE("cashaccount.transaction.edit.save"),
        CASHACCOUNT_TRANSACTION_EDIT_SELECT_CATEGORY("cashaccount.transaction.edit.selectCategory"),
        CASHACCOUNT_TRANSACTION_CONFIRM_DELETE("cashaccount.transaction.confirm.delete"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_TITLE("cashaccount.transaction.import.dialog.title"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP1_TITLE("cashaccount.transaction.import.dialog.step1.title"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP2_TITLE("cashaccount.transaction.import.dialog.step2.title"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP3_TITLE("cashaccount.transaction.import.dialog.step3.title"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_TITLE("cashaccount.transaction.import.dialog.step4.title"),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("cashaccount.transaction.import.dialog.step4.columns", CashAccountTransactionImportColmn.class),
        CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COMMON_ERROR("cashaccount.transaction.import.dialog.step4.commonError"),
	    CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_FILETYPE_ERROR("cashaccount.transaction.import.dialog.step4.fileTypeError"),
	    CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_FIELD_ERROR("cashaccount.transaction.import.dialog.step4.fieldError"),

	    IMPORTING_CSV_SEPARATOR("importing.csvSeparator", CsvSeparator.class),
	    IMPORTING_TEMPLATE_TYPE("importing.templateType", ImportTemplateType.class),
	    
        BANK_EDIT_TITLE("bank.edit.title"),
        BANK_EDIT_CANCEL("bank.edit.cancel"),
        BANK_EDIT_SAVE("bank.edit.save"),
        BANK_CONFIRM_DELETE("bank.confirm.delete"),

        CATEGORY_EDIT_ICON("category.edit.icon"),
        CATEGORY_EDIT_TITLE("category.edit.title"),
        CATEGORY_EDIT_CANCEL("category.edit.cancel"),
        CATEGORY_EDIT_SAVE("category.edit.save"),
        CASHACCOUNT_EDIT_SELECT_PRESET("category.edit.selectPreset"),

        CATEGORY_WORK("category.work"),
        CATEGORY_CAR("category.car"),
        CATEGORY_CREDIT("category.credit"),
        CATEGORY_BUSINESS("category.business"),
        CATEGORY_HOME("category.home"),
        CATEGORY_MEDIA("category.media"),
        CATEGORY_SHOPPING("category.shopping"),
        CATEGORY_DEPOT("category.depot"),
        CATEGORY_CONFIRM_DELETE("category.confirm.delete"),
	
	    STOCK_CATEGORY_EDIT_TITLE("stockCategory.edit.title"),
	    STOCK_CATEGORY_EDIT_CANCEL("stockCategory.edit.cancel"),
	    STOCK_CATEGORY_EDIT_SAVE("stockCategory.edit.save"),

        WIZARD_PREVIOUS("wizard.previous"),
        WIZARD_NEXT("wizard.next"),
        WIZARD_FINISH("wizard.finish"),
	
	    STOCK_EDIT_TITLE("stock.edit.title"),
	    STOCK_EDIT_CANCEL("stock.edit.cancel"),
	    STOCK_EDIT_SAVE("stock.edit.save"),
	    STOCK_CONFIRM_DELETE("stock.confirm.delete"),
	    STOCK_EDIT_SELECT_CATEGORY("stock.edit.selectCategory"),
	
	    CURRENCY_CONVERSION_FACTOR_EDIT_TITLE("currencyConversionFactor.edit.title"),
	    CURRENCY_CONVERSION_FACTOR_EDIT_CANCEL("currencyConversionFactor.edit.cancel"),
	    CURRENCY_CONVERSION_FACTOR_EDIT_SAVE("currencyConversionFactor.edit.save"),
	    CURRENCY_CONVERSION_FACTOR_CONFIRM_DELETE("currencyConversionFactor.confirmDelete"),
	
	    DEPOT_CONFIRM_DELETE("depot.confirm.delete"),
	    DEPOT_EDIT_TITLE("depot.edit.title"),
	    DEPOT_EDIT_CANCEL("depot.edit.cancel"),
	    DEPOT_EDIT_SAVE("depot.edit.save"),
	    DEPOT_EDIT_SELECT_BANK("depot.edit.selectBank"),
	    DEPOT_TRANSACTION_EDIT_TITLE("depot.transaction.edit.title"),
	    DEPOT_TRANSACTION_EDIT_CANCEL("depot.transaction.edit.cancel"),
	    DEPOT_TRANSACTION_EDIT_SAVE("depot.transaction.edit.save"),
	    DEPOT_TRANSACTION_CONFIRM_DELETE("depot.transaction.confirmDelete"),
	    DEPOT_DELIVERY_EDIT_TITLE("depot.delivery.edit.title"),
	    DEPOT_DELIVERY_EDIT_CANCEL("depot.delivery.edit.cancel"),
	    DEPOT_DELIVERY_EDIT_SAVE("depot.delivery.edit.save"),
	    DEPOT_DELIVERY_CONFIRM_DELETE("depot.delivery.confirmDelete"),
	    DEPOT_ITEM_EDIT_TITLE("depot.item.title"),
	    DEPOT_ITEM_EDIT_CANCEL("depot.item.cancel"),
	    DEPOT_ITEM_EDIT_SAVE("depot.item.save"),
	    DEPOT_ITEM_CONFIRM_DELETE("depot.item.confirmDelete")
        ;

        private final String key;
        private final Class<? extends Enum<?>> enumType;

        MessageKey(String key) {
            this.key = key;
            this.enumType = null;
        }

        MessageKey(String key, Class<? extends Enum<?>> enumType) {
            this.key = key;
            this.enumType = enumType;
        }

        public String getKey() {
            return key;
        }

        public Class<? extends Enum<?>> getEnumType() {
            return enumType;
        }
    }
}
