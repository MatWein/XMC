package io.github.matwein.xmc.be.services.exporting.controller;

import com.opencsv.CSVWriter;
import io.github.matwein.xmc.be.services.exporting.dto.DtoExportData;
import io.github.matwein.xmc.be.services.exporting.mapper.CsvObjectToStringMapper;
import io.github.matwein.xmc.be.services.exporting.mapper.DtoExportDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.List;

import static com.opencsv.ICSVWriter.*;

@Component
public class BeanToCsvExportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanToCsvExportController.class);
	
	private static final char SEPARATOR = ';';
	
	private final DtoExportDataMapper dtoExportDataMapper;
	private final CsvObjectToStringMapper csvObjectToStringMapper;
	
	@Autowired
	public BeanToCsvExportController(
			DtoExportDataMapper dtoExportDataMapper,
			CsvObjectToStringMapper csvObjectToStringMapper) {
		
		this.dtoExportDataMapper = dtoExportDataMapper;
		this.csvObjectToStringMapper = csvObjectToStringMapper;
	}
	
	public <T extends Serializable> byte[] export(Class<T> type, List<T> itemsToExport) {
		try (var stream = new ByteArrayOutputStream();
		     var streamWriter = new OutputStreamWriter(stream);
		     var csvWriter = new CSVWriter(streamWriter, SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END)) {
			
			DtoExportData dtoExportData = dtoExportDataMapper.map(type, itemsToExport);
			writeDataToStream(dtoExportData, csvWriter);
			return stream.toByteArray();
			
		} catch (Exception e) {
			String message = String.format("Error on exporting %s items to byte array.", itemsToExport.size());
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}
	
	private void writeDataToStream(DtoExportData dtoExportData, CSVWriter csvWriter) throws IOException {
		csvWriter.writeNext(dtoExportData.getHeaders());
		
		for (Object[] row : dtoExportData.getRows()) {
			String[] values = csvObjectToStringMapper.convertValuesToString(row);
			csvWriter.writeNext(values);
		}
		
		csvWriter.flush();
	}
}
