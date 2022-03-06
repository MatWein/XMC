package io.github.matwein.xmc.be.services.exporting.mapper;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.services.exporting.dto.DtoExportData;
import io.github.matwein.xmc.common.annotations.ExportField;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoExportDataMapper {
	public <T extends Serializable> DtoExportData map(
			Class<T> type,
			List<T> itemsToExport) throws IntrospectionException, IllegalAccessException {
		
		List<Field> sortedRelevantFields = Arrays.stream(FieldUtils.getAllFields(type))
				.filter(field -> field.getAnnotation(ExportField.class) != null)
				.peek(field -> field.setAccessible(true))
				.sorted(Comparator.comparing(field -> field.getAnnotation(ExportField.class).index()))
				.collect(Collectors.toList());
		
		var dtoExportData = new DtoExportData();
		
		dtoExportData.setHeaders(generateHeaders(sortedRelevantFields));
		dtoExportData.setRows(generateRows(sortedRelevantFields, itemsToExport));
		
		return dtoExportData;
	}
	
	private String[] generateHeaders(List<Field> sortedRelevantFields) {
		String[] headers = new String[sortedRelevantFields.size()];
		
		int index = 0;
		for (Field field : sortedRelevantFields) {
			String messageKey = field.getAnnotation(ExportField.class).messageKey();
			headers[index++] = MessageAdapter.getByKey(messageKey);
		}
		
		return headers;
	}
	
	private <T extends Serializable> List<Object[]> generateRows(
			List<Field> sortedRelevantFields,
			List<T> itemsToExport) throws IllegalAccessException {
		
		List<Object[]> rows = new ArrayList<>(itemsToExport.size());
		
		for (T item : itemsToExport) {
			Object[] fieldValus = extractFieldValues(item, sortedRelevantFields);
			rows.add(fieldValus);
		}
		
		return rows;
	}
	
	private <T extends Serializable> Object[] extractFieldValues(T item, List<Field> sortedRelevantFields) throws IllegalAccessException {
		Object[] values = new Object[sortedRelevantFields.size()];
		
		int index = 0;
		for (Field field : sortedRelevantFields) {
			Object value = field.get(item);
			values[index++] = value;
		}
		
		return values;
	}
}
