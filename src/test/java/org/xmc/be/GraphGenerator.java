package org.xmc.be;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.BinaryData;
import org.xmc.be.entities.analysis.AnalysisFavourite;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.entities.cashaccount.Category;
import org.xmc.be.entities.depot.*;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateColumnMapping;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.entities.settings.Setting;
import org.xmc.be.entities.settings.SettingType;
import org.xmc.be.entities.user.ServiceCallLog;
import org.xmc.be.entities.user.User;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.ImportType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Component
public class GraphGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    protected Session session() {
        return entityManager.unwrap(Session.class);
    }

    public User createUser() {
        return createUser(UUID.randomUUID().toString());
    }

    public User createUser(String userName) {
        var user = new User();

        user.setUsername(userName);
        user.setDisplayName(userName);

        session().persist(user);

        return user;
    }

    public BinaryData createBinaryData() {
        return createBinaryData("some data".getBytes());
    }

    public BinaryData createBinaryData(Bank bank) {
        var binaryData = createBinaryData();

        bank.setLogo(binaryData);
        session().saveOrUpdate(bank);

        return binaryData;
    }

    public BinaryData createBinaryData(byte[] data) {
        var binaryData = new BinaryData();

        binaryData.setDescription(UUID.randomUUID().toString());
        binaryData.setHash(UUID.randomUUID().toString());
        binaryData.setRawData(data);
        binaryData.setSize(data.length);

        session().persist(binaryData);

        return binaryData;
    }

    public Bank createBank() {
        var bank = new Bank();

        bank.setBic("bic");
        bank.setBlz("blz");
        bank.setLogo(createBinaryData());
        bank.setName("name");

        session().persist(bank);

        return bank;
    }

    public CashAccount createCashAccount() {
        return createCashAccount(createBank());
    }

    public CashAccount createCashAccount(Bank bank) {
        var cashAccount = new CashAccount();

        cashAccount.setBank(bank);
        cashAccount.setCurrency(Currency.getInstance("EUR"));
        cashAccount.setIban("iban");
        cashAccount.setName("name");
        cashAccount.setNumber("number");

        session().persist(cashAccount);

        return cashAccount;
    }

    public ServiceCallLog createServiceCallLog() {
        var serviceCallLog = new ServiceCallLog();

        serviceCallLog.setCallDuration(100);
        serviceCallLog.setServiceClass("test");
        serviceCallLog.setServiceMethod("method");

        session().persist(serviceCallLog);

        return serviceCallLog;
    }

    public Category createCategory() {
        return createCategory(createBinaryData());
    }
	
	public Category createCategory(String name) {
    	return createCategory(name, createBinaryData());
	}
	
	public Category createCategory(BinaryData icon) {
    	return createCategory(UUID.randomUUID().toString(), icon);
	}

    public Category createCategory(String name, BinaryData icon) {
        var category = new Category();

        category.setName(name);
        category.setIcon(icon);

        session().persist(category);

        return category;
    }

    public CashAccountTransaction createCashAccountTransaction() {
        return createCashAccountTransaction(createCashAccount());
    }

    public CashAccountTransaction createCashAccountTransaction(CashAccount cashAccount) {
        return createCashAccountTransaction(cashAccount, createCategory());
    }

    public CashAccountTransaction createCashAccountTransaction(CashAccount cashAccount, Category category) {
        var cashAccountTransaction = new CashAccountTransaction();

        cashAccountTransaction.setCashAccount(cashAccount);
        cashAccountTransaction.setCategory(category);
        cashAccountTransaction.setSaldoAfter(BigDecimal.valueOf(0.0));
        cashAccountTransaction.setSaldoBefore(BigDecimal.valueOf(0.0));
        cashAccountTransaction.setUsage("test");
        cashAccountTransaction.setValue(BigDecimal.valueOf(0.0));
        cashAccountTransaction.setValutaDate(LocalDate.now());

        session().persist(cashAccountTransaction);

        cashAccount.getTransactions().add(cashAccountTransaction);
        session().saveOrUpdate(cashAccount);

        return cashAccountTransaction;
    }
	
	public ImportTemplate createImportTemplate() {
    	return createImportTemplate(UUID.randomUUID().toString());
	}
    
    public ImportTemplate createImportTemplate(String name) {
    	return createImportTemplate(ImportTemplateType.CASH_ACCOUNT_TRANSACTION, name);
    }
    
    public ImportTemplate createImportTemplate(ImportTemplateType type, String name) {
	    var importTemplate = new ImportTemplate();
	
	    importTemplate.setName(name);
	    importTemplate.setCsvSeparator(CsvSeparator.SEMICOLON);
	    importTemplate.setImportType(ImportType.ADD_ONLY);
	    importTemplate.setType(type);
	    importTemplate.setStartWithLine(1);
	    importTemplate.setEncoding(StandardCharsets.UTF_8.name());
	    
	    session().persist(importTemplate);
	    
	    return importTemplate;
    }
	
	public ImportTemplateColumnMapping createImportTemplateColumnMapping() {
    	return createImportTemplateColumnMapping(createImportTemplate());
	}
 
	public ImportTemplateColumnMapping createImportTemplateColumnMapping(ImportTemplate importTemplate) {
		return createImportTemplateColumnMapping(importTemplate, 1, CashAccountTransactionImportColmn.VALUTA_DATE);
	}
 
	public ImportTemplateColumnMapping createImportTemplateColumnMapping(
			ImportTemplate importTemplate,
			int columnIndex,
			Enum<?> columnType) {
    	
    	return createImportTemplateColumnMapping(importTemplate, columnIndex, columnType.name());
	}
    
    public ImportTemplateColumnMapping createImportTemplateColumnMapping(
    		ImportTemplate importTemplate,
		    int columnIndex,
		    String columnType) {
    	
    	var importTemplateColumnMapping = new ImportTemplateColumnMapping();
	
	    importTemplateColumnMapping.setImportTemplate(importTemplate);
	    importTemplateColumnMapping.setColumnIndex(columnIndex);
	    importTemplateColumnMapping.setColumnType(columnType);
	    
	    session().persist(importTemplateColumnMapping);
	
	    importTemplate.getColumnMappings().add(importTemplateColumnMapping);
	    session().saveOrUpdate(importTemplate);
    	
    	return importTemplateColumnMapping;
    }
	
	public Setting createSetting() {
    	return createSetting(SettingType.EXTRAS_SHOW_SNOW, Boolean.TRUE.toString());
	}
    
    public Setting createSetting(SettingType type, String value) {
	    var setting = new Setting();
	
	    setting.setType(type);
	    setting.setValue(value);
	    
	    session().persist(setting);
	    
	    return setting;
    }
    
    public StockCategory createStockCategory() {
    	return createStockCategory(UUID.randomUUID().toString());
    }
    
    public StockCategory createStockCategory(String name) {
	    var stockCategory = new StockCategory();
	
	    stockCategory.setName(name);
	    
	    session().persist(stockCategory);
	    
	    return stockCategory;
    }
	
	public Stock createStock() {
    	return createStock(UUID.randomUUID().toString()) ;
	}
	
	public Stock createStock(String isin) {
    	return createStock(isin, UUID.randomUUID().toString(), UUID.randomUUID().toString());
	}
	
	public Stock createStock(String isin, String wkn, String name) {
    	return createStock(isin, wkn, name, createStockCategory());
	}
	
	public Stock createStock(String isin, String wkn, String name, StockCategory stockCategory) {
		var stock = new Stock();
		
		stock.setIsin(StringUtils.abbreviate(isin, 15));
		stock.setWkn(StringUtils.abbreviate(wkn, 10));
		stock.setName(name);
		stock.setStockCategory(stockCategory);
		
		session().persist(stock);
		
		return stock;
	}
 
	public Depot createDepot() {
    	return createDepot(createBank());
	}
    
    public Depot createDepot(Bank bank) {
	    var depot = new Depot();
	
	    depot.setBank(bank);
	    depot.setName(UUID.randomUUID().toString());
	    
	    session().persist(depot);
	    
	    return depot;
    }
    
    public DepotTransaction createDepotTransaction() {
    	return createDepotTransaction(createDepot());
    }
    
	public DepotTransaction createDepotTransaction(Depot depot) {
    	var depotTransaction = new DepotTransaction();
	
	    depotTransaction.setDepot(depot);
	    depotTransaction.setIsin(StringUtils.abbreviate(UUID.randomUUID().toString(), 15));
	    depotTransaction.setAmount(BigDecimal.valueOf(100.0));
	    depotTransaction.setCourse(BigDecimal.valueOf(10.0));
	    depotTransaction.setValue(BigDecimal.valueOf(1000.0));
	    depotTransaction.setValutaDate(LocalDate.now());
		depotTransaction.setCurrency("EUR");
    	
	    session().persist(depotTransaction);
		
		depot.getTransactions().add(depotTransaction);
	    session().saveOrUpdate(depot);
	    
    	return depotTransaction;
    }
    
    public DepotDelivery createDepotDelivery() {
	    return createDepotDelivery(createDepot());
    }
	
	public DepotDelivery createDepotDelivery(Depot depot) {
    	return createDepotDelivery(depot, LocalDateTime.now(), BigDecimal.valueOf(100.0));
	}
    
    public DepotDelivery createDepotDelivery(Depot depot, LocalDateTime deliveryDate, BigDecimal saldo) {
	    var depotDelivery = new DepotDelivery();
	
	    depotDelivery.setDepot(depot);
	    depotDelivery.setDeliveryDate(deliveryDate);
	    depotDelivery.setSaldo(saldo);
	    
	    session().persist(depotDelivery);
	    
	    depot.getDeliveries().add(depotDelivery);
	    session().saveOrUpdate(depot);
	    
	    return depotDelivery;
    }
    
    public DepotItem createDepotItem() {
    	return createDepotItem(createDepotDelivery());
    }
	
    public DepotItem createDepotItem(DepotDelivery depotDelivery) {
	    var depotItem = new DepotItem();
	
	    depotItem.setDelivery(depotDelivery);
	    depotItem.setIsin(StringUtils.abbreviate(UUID.randomUUID().toString(), 15));
	    depotItem.setAmount(BigDecimal.valueOf(20.0));
	    depotItem.setCourse(BigDecimal.valueOf(200.0));
	    depotItem.setValue(BigDecimal.valueOf(2000.0));
	    depotItem.setCurrency("EUR");
	    
	    session().persist(depotItem);
	    
	    depotDelivery.getDepotItems().add(depotItem);
	    session().saveOrUpdate(depotDelivery);
	    
	    return depotItem;
    }
    
    public CurrencyConversionFactor createCurrencyConversionFactor() {
    	return createCurrencyConversionFactor("USD", LocalDateTime.now(), BigDecimal.valueOf(0.82));
    }
    
    public CurrencyConversionFactor createCurrencyConversionFactor(String currency, LocalDateTime inputDate, BigDecimal factorToEur) {
	    var currencyConversionFactor = new CurrencyConversionFactor();
	
	    currencyConversionFactor.setCurrency(currency);
	    currencyConversionFactor.setInputDate(inputDate);
	    currencyConversionFactor.setFactorToEur(factorToEur);
	    
	    session().persist(currencyConversionFactor);
	    
	    return currencyConversionFactor;
    }
    
    public AnalysisFavourite createAnalysisFavourite() {
    	return createAnalysisFavourite(UUID.randomUUID().toString(), AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
    }
    
    public AnalysisFavourite createAnalysisFavourite(String name, AnalysisType type) {
	    var analysisFavourite = new AnalysisFavourite();
	
	    analysisFavourite.setType(type);
	    analysisFavourite.setName(name);
	    analysisFavourite.setStartDate(LocalDate.now());
	    analysisFavourite.setEndDate(LocalDate.now());
	    
	    session().persist(analysisFavourite);
	    
	    return analysisFavourite;
    }
}
