package io.github.matwein.xmc.be.common.pagination;

import io.github.matwein.xmc.be.common.pagination.mapper.*;
import io.github.matwein.xmc.common.stubs.IPagingField;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagingFieldMapperFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(PagingFieldMapperFactory.class);
	
	private final CategoryOverviewFieldsMapper categoryOverviewFieldsMapper;
	private final CashAccountOverviewFieldsMapper cashAccountOverviewFieldsMapper;
	private final StockCategoryOverviewFieldsMapper stockCategoryOverviewFieldsMapper;
	private final BankOverviewFieldsMapper bankOverviewFieldsMapper;
	private final CashAccountTransactionOverviewFieldsMapper cashAccountTransactionOverviewFieldsMapper;
	private final ServiceCallLogOverviewFieldsMapper serviceCallLogOverviewFieldsMapper;
	private final CurrencyConversionFactorOverviewFieldsMapper currencyConversionFactorOverviewFieldsMapper;
	private final DepotOverviewFieldsMapper depotOverviewFieldsMapper;
	private final DepotDeliveryOverviewFieldsMapper depotDeliveryOverviewFieldsMapper;
	private final DepotTransactionOverviewFieldsMapper depotTransactionOverviewFieldsMapper;
	private final DepotItemOverviewFieldsMapper depotItemOverviewFieldsMapper;
	private final StockOverviewFieldsMapper stockOverviewFieldsMapper;
	
	@Autowired
	public PagingFieldMapperFactory(
			CategoryOverviewFieldsMapper categoryOverviewFieldsMapper,
			CashAccountOverviewFieldsMapper cashAccountOverviewFieldsMapper,
			StockCategoryOverviewFieldsMapper stockCategoryOverviewFieldsMapper,
			BankOverviewFieldsMapper bankOverviewFieldsMapper,
			CashAccountTransactionOverviewFieldsMapper cashAccountTransactionOverviewFieldsMapper,
			ServiceCallLogOverviewFieldsMapper serviceCallLogOverviewFieldsMapper,
			CurrencyConversionFactorOverviewFieldsMapper currencyConversionFactorOverviewFieldsMapper,
			DepotOverviewFieldsMapper depotOverviewFieldsMapper,
			DepotDeliveryOverviewFieldsMapper depotDeliveryOverviewFieldsMapper,
			DepotTransactionOverviewFieldsMapper depotTransactionOverviewFieldsMapper,
			DepotItemOverviewFieldsMapper depotItemOverviewFieldsMapper,
			StockOverviewFieldsMapper stockOverviewFieldsMapper) {
		
		this.categoryOverviewFieldsMapper = categoryOverviewFieldsMapper;
		this.cashAccountOverviewFieldsMapper = cashAccountOverviewFieldsMapper;
		this.stockCategoryOverviewFieldsMapper = stockCategoryOverviewFieldsMapper;
		this.bankOverviewFieldsMapper = bankOverviewFieldsMapper;
		this.cashAccountTransactionOverviewFieldsMapper = cashAccountTransactionOverviewFieldsMapper;
		this.serviceCallLogOverviewFieldsMapper = serviceCallLogOverviewFieldsMapper;
		this.currencyConversionFactorOverviewFieldsMapper = currencyConversionFactorOverviewFieldsMapper;
		this.depotOverviewFieldsMapper = depotOverviewFieldsMapper;
		this.depotDeliveryOverviewFieldsMapper = depotDeliveryOverviewFieldsMapper;
		this.depotTransactionOverviewFieldsMapper = depotTransactionOverviewFieldsMapper;
		this.depotItemOverviewFieldsMapper = depotItemOverviewFieldsMapper;
		this.stockOverviewFieldsMapper = stockOverviewFieldsMapper;
	}
	
	public IPagingFieldMapper create(IPagingField pagingField) {
		Class<? extends IPagingField> fieldClass = pagingField.getClass();
		
		if (fieldClass.equals(CategoryOverviewFields.class)) {
			return categoryOverviewFieldsMapper;
		} else if (fieldClass.equals(CashAccountOverviewFields.class)) {
			return cashAccountOverviewFieldsMapper;
		} else if (fieldClass.equals(StockCategoryOverviewFields.class)) {
			return stockCategoryOverviewFieldsMapper;
		} else if (fieldClass.equals(BankOverviewFields.class)) {
			return bankOverviewFieldsMapper;
		} else if (fieldClass.equals(CashAccountTransactionOverviewFields.class)) {
			return cashAccountTransactionOverviewFieldsMapper;
		} else if (fieldClass.equals(ServiceCallLogOverviewFields.class)) {
			return serviceCallLogOverviewFieldsMapper;
		} else if (fieldClass.equals(CurrencyConversionFactorOverviewFields.class)) {
			return currencyConversionFactorOverviewFieldsMapper;
		} else if (fieldClass.equals(DepotOverviewFields.class)) {
			return depotOverviewFieldsMapper;
		} else if (fieldClass.equals(DepotDeliveryOverviewFields.class)) {
			return depotDeliveryOverviewFieldsMapper;
		} else if (fieldClass.equals(DepotTransactionOverviewFields.class)) {
			return depotTransactionOverviewFieldsMapper;
		} else if (fieldClass.equals(DepotItemOverviewFields.class)) {
			return depotItemOverviewFieldsMapper;
		} else if (fieldClass.equals(StockOverviewFields.class)) {
			return stockOverviewFieldsMapper;
		}
		
		String message = String.format("Could not find mapper for paging field enum of type '%s'.", fieldClass.getName());
		LOGGER.error(message);
		throw new IllegalArgumentException(message);
	}
}
