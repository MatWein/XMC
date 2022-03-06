package io.github.matwein.xmc.be.services.exporting.mapper;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.common.stubs.Money;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CsvObjectToStringMapper {
	public String[] convertValuesToString(Object[] row) {
		String[] values = new String[row.length];
		
		for (int i = 0; i < row.length; i++) {
			values[i] = convertValueToString(row[i]);
		}
		
		return values;
	}
	
	private String convertValueToString(Object object) {
		if (object == null) {
			return null;
		}
		
		if (object instanceof String string) {
			return string;
		} else if (object instanceof LocalDateTime localDateTime) {
			return MessageAdapter.formatDateTime(localDateTime);
		} else if (object instanceof LocalDate localDate) {
			return MessageAdapter.formatDate(localDate);
		} else if (object instanceof Money money) {
			return String.format("%s %s", MessageAdapter.formatNumber(money.getValue()), money.getCurrency());
		} else if (object instanceof Number number) {
			return MessageAdapter.formatNumber(number);
		} else if (object instanceof DtoCategory dtoCategory) {
			return dtoCategory.getName();
		}
		
		return object.toString();
	}
}
