package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.services.importing.controller.IImportRowMapper;
import io.github.matwein.xmc.be.services.importing.parser.BigDecimalParser;
import io.github.matwein.xmc.be.services.importing.parser.CurrencyParser;
import io.github.matwein.xmc.be.services.importing.parser.LocalDateParser;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
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
			case VALUTA_DATE -> {
				LocalDate valutaDate = localDateParser.parseDateNullOnError(columnValue);
				result.setValutaDate(valutaDate);
			}
			case ISIN -> result.setIsin(columnValue);
			case AMOUNT -> {
				BigDecimal amount = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setAmount(amount);
			}
			case COURSE -> {
				BigDecimal course = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setCourse(course);
			}
			case VALUE -> {
				BigDecimal value = bigDecimalParser.parseBigDecimalNullOnError(columnValue);
				result.setValue(value);
			}
			case DESCRIPTION -> result.setDescription(columnValue);
			case CURRENCY -> {
				String currency = currencyParser.parseCurrencyNullOnError(columnValue);
				result.setCurrency(currency);
			}
		}
	}
}
