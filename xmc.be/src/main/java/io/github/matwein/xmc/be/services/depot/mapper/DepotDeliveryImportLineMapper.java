package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.services.importing.controller.IImportRowMapper;
import io.github.matwein.xmc.be.services.importing.parser.BigDecimalParser;
import io.github.matwein.xmc.be.services.importing.parser.CurrencyParser;
import io.github.matwein.xmc.be.services.importing.parser.LocalDateTimeParser;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DepotDeliveryImportLineMapper implements IImportRowMapper<DtoDepotDeliveryImportRow, DepotDeliveryImportColmn> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryImportLineMapper.class);
	
	private final LocalDateTimeParser localDateTimeParser;
	private final BigDecimalParser bigDecimalParser;
	private final CurrencyParser currencyParser;
	
	@Autowired
	public DepotDeliveryImportLineMapper(
			LocalDateTimeParser localDateTimeParser,
			BigDecimalParser bigDecimalParser,
			CurrencyParser currencyParser) {
		
		this.localDateTimeParser = localDateTimeParser;
		this.bigDecimalParser = bigDecimalParser;
		this.currencyParser = currencyParser;
	}
	
	@Override
	public DtoDepotDeliveryImportRow apply(List<String> line, List<DtoColumnMapping<DepotDeliveryImportColmn>> columnMappings) {
		var result = new DtoDepotDeliveryImportRow();
		
		for (DtoColumnMapping<DepotDeliveryImportColmn> columnMapping : columnMappings) {
			try {
				String columnValue = line.get(columnMapping.getColumn() - 1);
				populateValue(result, columnValue, columnMapping.getField());
			} catch (IndexOutOfBoundsException e) {
				LOGGER.trace("Could not find column value for mapped index {} ({}).", columnMapping.getColumn(), columnMapping.getField());
			}
		}
		
		return result;
	}
	
	private void populateValue(DtoDepotDeliveryImportRow result, String columnValue, DepotDeliveryImportColmn field) {
		if (field == null) {
			return;
		}
		
		switch (field) {
			case DELIVERY_DATE -> {
				LocalDateTime deliveryDate = localDateTimeParser.parseDateTimeNullOnError(columnValue);
				result.setDeliveryDate(deliveryDate);
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
			case CURRENCY -> {
				String currency = currencyParser.parseCurrencyNullOnError(columnValue);
				result.setCurrency(currency);
			}
		}
	}
}
