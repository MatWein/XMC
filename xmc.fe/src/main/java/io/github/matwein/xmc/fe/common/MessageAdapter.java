package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.importing.CsvSeparator;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    public static ResourceBundle RESOURCE_BUNDLE = initBundle();

    public static ResourceBundle initBundle() {
        return RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages_frontend", Locale.getDefault());
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
	
	public static String formatNumber(Number number) {
    	if (number == null) {
    		return null;
	    }
		
		NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.getDefault());
		numberInstance.setMinimumFractionDigits(2);
		numberInstance.setMaximumFractionDigits(2);
		return numberInstance.format(number);
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

	    STATUS_READY("main.status.ready"),
	    STATUS_ERROR("main.status.error"),
	    
        PAGING_FIRST_PAGE("paging.firstPage"),
        PAGING_BACK("paging.back"),
        PAGING_NEXT("paging.next"),
        PAGING_LAST_PAGE("paging.lastPage"),
        PAGING_COUNT("paging.count"),
        PAGING_FILTER_PROMPT("paging.filterPrompt"),

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
	    PROTOCOL_TITLE("protocol.title"),

	    LOGS_TITLE("logs.title"),
	    LOGS_OPEN_FOLDER("logs.openFolder"),

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
	    DEPOT_ITEM_CONFIRM_DELETE("depot.item.confirmDelete"),
	    
	    DEPOT_DELIVERY_IMPORT_DIALOG_TITLE("depot.delivery.import.dialog.title"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP1_TITLE("depot.delivery.import.dialog.step1.title"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP2_TITLE("depot.delivery.import.dialog.step2.title"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP3_TITLE("depot.delivery.import.dialog.step3.title"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP4_TITLE("depot.delivery.import.dialog.step4.title"),
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("depot.delivery.import.dialog.step4.columns", DepotDeliveryImportColmn.class),
	
	    DEPOT_ITEM_IMPORT_DIALOG_TITLE("depot.item.import.dialog.title"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP1_TITLE("depot.item.import.dialog.step1.title"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP2_TITLE("depot.item.import.dialog.step2.title"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP3_TITLE("depot.item.import.dialog.step3.title"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP4_TITLE("depot.item.import.dialog.step4.title"),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("depot.item.import.dialog.step4.columns", DepotItemImportColmn.class),
	
	    DEPOT_TRANSACTION_IMPORT_DIALOG_TITLE("depot.transaction.import.dialog.title"),
	    DEPOT_TRANSACTION_IMPORT_DIALOG_STEP1_TITLE("depot.transaction.import.dialog.step1.title"),
	    DEPOT_TRANSACTION_IMPORT_DIALOG_STEP2_TITLE("depot.transaction.import.dialog.step2.title"),
	    DEPOT_TRANSACTION_IMPORT_DIALOG_STEP3_TITLE("depot.transaction.import.dialog.step3.title"),
	    DEPOT_TRANSACTION_IMPORT_DIALOG_STEP4_TITLE("depot.transaction.import.dialog.step4.title"),
	    DEPOT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("depot.transaction.import.dialog.step4.columns", DepotTransactionImportColmn.class),
	    
	    DASHBOARD_REMOVE_TILE("dashboard.removeTile"),
	    
	    ANALYSIS_TYPE("analysis.type", AnalysisType.class),
	    TIME_RANGE_TYPE("timerange.type",TimeRange .class),
	    
	    ANALYSIS_NO_CALCULATION_RESULT("analysis.noCalculationResult"),
	    ANALYSIS_HINT("analysis.hint"),
	    ANALYSIS_AXIS_DATE("analysis.axis.date"),
	    ANALYSIS_AXIS_VALUE_IN_EUR("analysis.axis.valueInEur"),
	    ANALYSIS_CHART_POINT_XY_HOVER("analysis.chartPointXY.hover"),
	    ANALYSIS_COUNTER("main.tabs.analysis.counter"),
	    ANALYSIS_SAVE_FAVOURITE_NAME("analysis.save.favouriteName"),
	    ANALYSIS_DASHBOARD_DIALOG_TITLE("analysis.dashboard.dialog.title"),
	    ANALYSIS_DASHBOARD_DIALOG_CANCEL("analysis.dashboard.dialog.cancel"),
	    ANALYSIS_DASHBOARD_DIALOG_ADD("analysis.dashboard.dialog.add")
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
