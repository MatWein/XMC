package io.github.matwein.xmc.be.services.cashaccount.controller.importing;

import com.google.common.collect.Maps;
import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.repositories.category.CategoryRepository;
import io.github.matwein.xmc.be.services.importing.controller.IImportRowMapper;
import io.github.matwein.xmc.be.services.importing.parser.BigDecimalParser;
import io.github.matwein.xmc.be.services.importing.parser.LocalDateParser;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class CashAccountTransactionImportLineMapper implements IImportRowMapper<DtoCashAccountTransaction, CashAccountTransactionImportColmn> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportLineMapper.class);
	
	private final LocalDateParser localDateParser;
	private final BigDecimalParser bigDecimalParser;
	private final CategoryRepository categoryRepository;
	
	@Autowired
	public CashAccountTransactionImportLineMapper(
			LocalDateParser localDateParser,
			BigDecimalParser bigDecimalParser,
			CategoryRepository categoryRepository) {
		
		this.localDateParser = localDateParser;
		this.bigDecimalParser = bigDecimalParser;
		this.categoryRepository = categoryRepository;
	}
	
	@Override
	public DtoCashAccountTransaction apply(List<String> line, List<DtoColumnMapping<CashAccountTransactionImportColmn>> columnMappings) {
		var result = new DtoCashAccountTransaction();
		
		QueryResults<DtoCategoryOverview> categories = categoryRepository.loadOverview(new PagingParams<>(0, Integer.MAX_VALUE, CategoryOverviewFields.NAME, Order.ASC, null));
		Map<String, DtoCategoryOverview> categoriesByName = Maps.uniqueIndex(categories.getResults(), DtoCategoryOverview::getName);
		
		for (DtoColumnMapping<CashAccountTransactionImportColmn> columnMapping : columnMappings) {
			try {
				String columnValue = line.get(columnMapping.getColumn() - 1);
				populateValue(result, columnValue, columnMapping.getField(), categoriesByName);
			} catch (IndexOutOfBoundsException e) {
				LOGGER.trace("Could not find column value for mapped index {} ({}).", columnMapping.getColumn(), columnMapping.getField());
			}
		}
		
		return result;
	}
	
	private void populateValue(
			DtoCashAccountTransaction result,
			String columnValue,
			CashAccountTransactionImportColmn field,
			Map<String, DtoCategoryOverview> categoriesByName) {
		
		if (field == null) {
			return;
		}
		
		switch (field) {
			case CATEGORY:
				DtoCategory category = categoriesByName.get(columnValue);
				result.setCategory(category);
				break;
			case USAGE:
				result.setUsage(StringUtils.defaultIfBlank(columnValue, "-"));
				break;
			case DESCRIPTION:
				result.setDescription(columnValue);
				break;
			case VALUTA_DATE:
				LocalDate valutaDate = localDateParser.parseDateNullOnError(columnValue);
				result.setValutaDate(valutaDate);
				break;
			case VALUE:
				BigDecimal value = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setValue(value);
				break;
			case REFERENCE_BANK:
				result.setReferenceBank(columnValue);
				break;
			case REFERENCE_IBAN:
				result.setReferenceIban(columnValue);
				break;
			case REFERENCE:
				result.setReference(columnValue);
				break;
			case CREDITOR_IDENTIFIER:
				result.setCreditorIdentifier(columnValue);
				break;
			case MANDATE:
				result.setMandate(columnValue);
				break;
			default:
				String message = String.format("Could not populate unknown field '%s' with value '%s'.", field, columnValue);
				throw new IllegalArgumentException(message);
		}
	}
}
