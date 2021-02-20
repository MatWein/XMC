package org.xmc.be.services.depot.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.importing.controller.IImportRowMapper;
import org.xmc.be.services.importing.parser.BigDecimalParser;
import org.xmc.be.services.importing.parser.CurrencyParser;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Component
public class DepotItemImportLineMapper implements IImportRowMapper<DtoDepotItemImportRow, DepotItemImportColmn> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotItemImportLineMapper.class);
	
	private final BigDecimalParser bigDecimalParser;
	private final CurrencyParser currencyParser;
	
	@Autowired
	public DepotItemImportLineMapper(
			BigDecimalParser bigDecimalParser,
			CurrencyParser currencyParser) {
		
		this.bigDecimalParser = bigDecimalParser;
		this.currencyParser = currencyParser;
	}
	
	@Override
	public DtoDepotItemImportRow apply(List<String> line, List<DtoColumnMapping<DepotItemImportColmn>> columnMappings) {
		var result = new DtoDepotItemImportRow();
		
		for (DtoColumnMapping<DepotItemImportColmn> columnMapping : columnMappings) {
			try {
				String columnValue = line.get(columnMapping.getColumn() - 1);
				populateValue(result, columnValue, columnMapping.getField());
			} catch (IndexOutOfBoundsException e) {
				LOGGER.trace("Could not find column value for mapped index {} ({}).", columnMapping.getColumn(), columnMapping.getField());
			}
		}
		
		return result;
	}
	
	private void populateValue(DtoDepotItemImportRow result, String columnValue, DepotItemImportColmn field) {
		if (field == null) {
			return;
		}
		
		switch (field) {
			case ISIN:
				result.setIsin(columnValue);
				break;
			case AMOUNT:
				BigDecimal amount = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setAmount(amount);
				break;
			case COURSE:
				BigDecimal course = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setCourse(course);
				break;
			case VALUE:
				BigDecimal value = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setValue(value);
				break;
			case CURRENCY:
				Currency currency = currencyParser.parseCurrencyNullOnError(columnValue);
				result.setCurrency(currency);
				break;
			default:
				String message = String.format("Could not populate unknown field '%s' with value '%s'.", field, columnValue);
				throw new IllegalArgumentException(message);
		}
	}
}
