package io.github.matwein.xmc.be.common;

import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
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
        return RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages_backend", Locale.getDefault());
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

    public static String getByKey(String key, Object... args) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args);
        } catch (MissingResourceException e) {
            LOGGER.warn("Message key not found: {}", key, e);
            return key;
        }
    }
    
    public enum MessageKey {
	    APP_DATE_FORMAT("app.dateFormat"),
	    APP_DATETIME_FORMAT("app.dateTimeFormat"),
	
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
	    ASYNC_TASK_IMPORTING_DEPOT_DELIVERIES("async.task.importingDepotDeliveries"),
	    ASYNC_TASK_IMPORTING_DEPOT_ITEMS("async.task.importingDepotItems"),
	    ASYNC_TASK_LOAD_SERVICECALLLOGS("async.task.loadingServiceCallLogs"),
	    ASYNC_TASK_CALCULATE_STARTEND_DATE("async.task.calculateStartEndDate"),
	    ASYNC_TASK_IMPORTING_DEPOT_TRANSACTIONS("async.task.importingDepotTransactions"),
	    ASYNC_TASK_LOAD_SELECTABLE_ASSETS_FOR_ANALYSIS("async.task.loadSelectableAssetsForAnalysis"),
	    ASYNC_TASK_CALCULATING_CHART("async.task.calculatingChart"),
	    ASYNC_TASK_SAVE_ANALYSIS_FAVOURITE("async.task.saveAnalysisFavourite"),
	    ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES("async.task.loadAnalysisFavourites"),
	    ASYNC_TASK_DELETE_ANALYSIS_FAVOURITE("async.task.deleteAnalysisFavourite"),
	    ASYNC_TASK_RENAME_ANALYSIS_FAVOURITE("async.task.renameAnalysisFavourite"),
	    ASYNC_TASK_EXPORT_ITEMS_TO_FILE("async.task.exportItemsToFile"),
	
	    VALIDATION_IMPORT_FIELD_ERROR("validation.import.field.error"),
	    
	    CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COMMON_ERROR("cashaccount.transaction.import.dialog.step4.commonError"),
	    CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_FILETYPE_ERROR("cashaccount.transaction.import.dialog.step4.fileTypeError"),
	    CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("cashaccount.transaction.import.dialog.step4.columns", CashAccountTransactionImportColmn.class),
	
	    DEPOT_DELIVERY_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("depot.delivery.import.dialog.step4.columns", DepotDeliveryImportColmn.class),
	    DEPOT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("depot.transaction.import.dialog.step4.columns", DepotTransactionImportColmn.class),
	    DEPOT_ITEM_IMPORT_DIALOG_STEP4_COLUMN_PREFIX("depot.item.import.dialog.step4.columns", DepotItemImportColmn.class),
	    
	    ANALYSIS_CHART_AGGREGATED_SERIES_NAME("analysis.chartPointXY.aggregatedSeriesName"),
	    ANALYSIS_OTHER("analysis.other"),
	    ANALYSIS_SUM_IN_EUR("analysis.sumInEuro"),
	    ANALYSIS_PERCENTAGE("analysis.percentage"),
	    ANALYSIS_DESCRIPTION("analysis.description"),
	    ANALYSIS_DATE("analysis.date"),
	    ANALYSIS_TRANSACTIONS_AGGREGATE_DESCRIPTION("analysis.transactions.aggregateDescription"),
	    ASSET_TYPE("asset.type", AssetType.class),
	    ASSET_TYPE_ALL("asset.type.all"),
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
