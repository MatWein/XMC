package org.xmc.be.services.depot.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.importing.controller.IImportRowMapper;
import org.xmc.be.services.importing.parser.BigDecimalParser;
import org.xmc.be.services.importing.parser.CurrencyParser;
import org.xmc.be.services.importing.parser.LocalDateParser;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Component
public class DepotTransactionImportLineMapper implements IImportRowMapper<DtoDepotTransactionImportRow, DepotTransactionImportColmn> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotTransactionImportLineMapper.class);
	
	private final BigDecimalParser bigDecimalParser;
	private final LocalDateParser localDateParser;
	private final CurrencyParser currencyParser;
	
	@Autowired
	public DepotTransactionImportLineMapper(
			BigDecimalParser bigDecimalParser,
			LocalDateParser localDateParser,
			CurrencyParser currencyParser) {
		
		this.bigDecimalParser = bigDecimalParser;
		this.localDateParser = localDateParser;
		this.currencyParser = currencyParser;
	}
	
	@Override
	public DtoDepotTransactionImportRow apply(List<String> line, List<DtoColumnMapping<DepotTransactionImportColmn>> columnMappings) {
		var result = new DtoDepotTransactionImportRow();
		
		for (DtoColumnMapping<DepotTransactionImportColmn> columnMapping : columnMappings) {
			try {
				String columnValue = line.get(columnMapping.getColumn() - 1);
				populateValue(result, columnValue, columnMapping.getField());
			} catch (IndexOutOfBoundsException e) {
				LOGGER.trace("Could not find column value for mapped index {} ({}).", columnMapping.getColumn(), columnMapping.getField());
			}
		}
		
		if (result.getValue() == null && result.getAmount() != null && result.getCourse() != null) {
			result.setValue(result.getAmount().multiply(result.getCourse()));
		}
		
		return result;
	}
	
	private void populateValue(DtoDepotTransactionImportRow result, String columnValue, DepotTransactionImportColmn field) {
		if (field == null) {
			return;
		}
		
		switch (field) {
			case VALUTA_DATE:
				LocalDate valutaDate = localDateParser.parseDateNullOnError(columnValue);
				result.setValutaDate(valutaDate);
				break;
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
			case DESCRIPTION:
				result.setDescription(columnValue);
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
