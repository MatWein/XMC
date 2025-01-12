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
import org.springframework.stereotype.Component;

@Component
public class PagingFieldMapperFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(PagingFieldMapperFactory.class);
	
	public static IPagingFieldMapper create(IPagingField pagingField) {
		Class<? extends IPagingField> fieldClass = pagingField.getClass();
		
		if (fieldClass.equals(CategoryOverviewFields.class)) {
			return new CategoryOverviewFieldsMapper();
		} else if (fieldClass.equals(CashAccountOverviewFields.class)) {
			return new CashAccountOverviewFieldsMapper();
		} else if (fieldClass.equals(StockCategoryOverviewFields.class)) {
			return new StockCategoryOverviewFieldsMapper();
		} else if (fieldClass.equals(BankOverviewFields.class)) {
			return new BankOverviewFieldsMapper();
		} else if (fieldClass.equals(CashAccountTransactionOverviewFields.class)) {
			return new CashAccountTransactionOverviewFieldsMapper();
		} else if (fieldClass.equals(ServiceCallLogOverviewFields.class)) {
			return new ServiceCallLogOverviewFieldsMapper();
		} else if (fieldClass.equals(CurrencyConversionFactorOverviewFields.class)) {
			return new CurrencyConversionFactorOverviewFieldsMapper();
		} else if (fieldClass.equals(DepotOverviewFields.class)) {
			return new DepotOverviewFieldsMapper();
		} else if (fieldClass.equals(DepotDeliveryOverviewFields.class)) {
			return new DepotDeliveryOverviewFieldsMapper();
		} else if (fieldClass.equals(DepotTransactionOverviewFields.class)) {
			return new DepotTransactionOverviewFieldsMapper();
		} else if (fieldClass.equals(DepotItemOverviewFields.class)) {
			return new DepotItemOverviewFieldsMapper();
		} else if (fieldClass.equals(StockOverviewFields.class)) {
			return new StockOverviewFieldsMapper();
		}
		
		String message = String.format("Could not find mapper for paging field enum of type '%s'.", fieldClass.getName());
		LOGGER.error(message);
		throw new IllegalArgumentException(message);
	}
}
