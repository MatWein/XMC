package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.services.importing.controller.IImportRowMapper;
import io.github.matwein.xmc.be.services.importing.parser.BigDecimalParser;
import io.github.matwein.xmc.be.services.importing.parser.CurrencyParser;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
		
		if (result.getValue() == null && result.getAmount() != null && result.getCourse() != null) {
			result.setValue(result.getAmount().multiply(result.getCourse()));
		}
		
		return result;
	}
	
	private void populateValue(DtoDepotItemImportRow result, String columnValue, DepotItemImportColmn field) {
		if (field == null) {
			return;
		}
		
		switch (field) {
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
			case CURRENCY -> {
				String currency = currencyParser.parseCurrencyNullOnError(columnValue);
				result.setCurrency(currency);
			}
		}
	}
}
