package io.github.matwein.xmc.fe;

import io.github.matwein.xmc.common.services.IServiceCallLogService;
import io.github.matwein.xmc.common.services.analysis.IAnalysisAssetService;
import io.github.matwein.xmc.common.services.analysis.IAnalysisChartCalculationService;
import io.github.matwein.xmc.common.services.analysis.IAnalysisFavouriteService;
import io.github.matwein.xmc.common.services.analysis.ITimeRangeService;
import io.github.matwein.xmc.common.services.bank.IBankService;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountService;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountTransactionImportService;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountTransactionService;
import io.github.matwein.xmc.common.services.category.ICategoryService;
import io.github.matwein.xmc.common.services.category.IStockCategoryService;
import io.github.matwein.xmc.common.services.ccf.ICurrencyConversionFactorService;
import io.github.matwein.xmc.common.services.depot.*;
import io.github.matwein.xmc.common.services.importing.IImportTemplateService;
import io.github.matwein.xmc.common.services.login.IUserLoginService;
import io.github.matwein.xmc.common.services.login.IUserRegistrationService;
import io.github.matwein.xmc.common.services.settings.ISettingsService;
import io.github.matwein.xmc.common.services.stock.IStockService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfig {
	@Bean
	public IAnalysisAssetService analysisAssetService() {
		return Mockito.mock(IAnalysisAssetService.class);
	}
	
	@Bean
	public IAnalysisChartCalculationService analysisChartCalculationService() {
		return Mockito.mock(IAnalysisChartCalculationService.class);
	}
	
	@Bean
	public IAnalysisFavouriteService analysisFavouriteService() {
		return Mockito.mock(IAnalysisFavouriteService.class);
	}
	
	@Bean
	public ITimeRangeService timeRangeService() {
		return Mockito.mock(ITimeRangeService.class);
	}
	
	@Bean
	public IBankService bankService() {
		return Mockito.mock(IBankService.class);
	}
	
	@Bean
	public ICashAccountService cashAccountService() {
		return Mockito.mock(ICashAccountService.class);
	}
	
	@Bean
	public ICashAccountTransactionImportService cashAccountTransactionImportService() {
		return Mockito.mock(ICashAccountTransactionImportService.class);
	}
	
	@Bean
	public ICashAccountTransactionService cashAccountTransactionService() {
		return Mockito.mock(ICashAccountTransactionService.class);
	}
	
	@Bean
	public ICategoryService categoryService() {
		return Mockito.mock(ICategoryService.class);
	}
	
	@Bean
	public IStockCategoryService stockCategoryService() {
		return Mockito.mock(IStockCategoryService.class);
	}
	
	@Bean
	public ICurrencyConversionFactorService currencyConversionFactorService() {
		return Mockito.mock(ICurrencyConversionFactorService.class);
	}
	
	@Bean
	public IDepotDeliveryImportService depotDeliveryImportService() {
		return Mockito.mock(IDepotDeliveryImportService.class);
	}
	
	@Bean
	public IDepotDeliveryService depotDeliveryService() {
		return Mockito.mock(IDepotDeliveryService.class);
	}
	
	@Bean
	public IDepotItemImportService depotItemImportService() {
		return Mockito.mock(IDepotItemImportService.class);
	}
	
	@Bean
	public IDepotItemService depotItemService() {
		return Mockito.mock(IDepotItemService.class);
	}
	
	@Bean
	public IDepotService depotService() {
		return Mockito.mock(IDepotService.class);
	}
	
	@Bean
	public IDepotTransactionImportService depotTransactionImportService() {
		return Mockito.mock(IDepotTransactionImportService.class);
	}
	
	@Bean
	public IDepotTransactionService depotTransactionService() {
		return Mockito.mock(IDepotTransactionService.class);
	}
	
	@Bean
	public IImportTemplateService importTemplateService() {
		return Mockito.mock(IImportTemplateService.class);
	}
	
	@Bean
	public IUserLoginService userLoginService() {
		return Mockito.mock(IUserLoginService.class);
	}
	
	@Bean
	public IUserRegistrationService userRegistrationService() {
		return Mockito.mock(IUserRegistrationService.class);
	}
	
	@Bean
	public ISettingsService settingsService() {
		return Mockito.mock(ISettingsService.class);
	}
	
	@Bean
	public IStockService stockService() {
		return Mockito.mock(IStockService.class);
	}
	
	@Bean
	public IServiceCallLogService serviceCallLogService() {
		return Mockito.mock(IServiceCallLogService.class);
	}
}
