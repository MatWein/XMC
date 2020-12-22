package org.xmc.be.services.importing.controller;

import com.google.common.collect.Lists;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.importing.CsvSeparator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class RawImportCsvFileReader {
	public List<List<String>> read(File fileToImport, int startWithLine, CsvSeparator csvSeparator, Charset charset) throws IOException, CsvValidationException {
		CSVParser csvParser = new CSVParserBuilder()
				.withSeparator(csvSeparator.getCharacter())
				.withIgnoreLeadingWhiteSpace(true)
				.withIgnoreQuotations(true)
				.build();
		
		List<List<String>> lines = Lists.newArrayList();
		
		try (var fileReader = new FileReader(fileToImport, charset)) {
			try (var csvReader = new CSVReaderBuilder(fileReader)
					.withSkipLines(startWithLine - 1)
					.withCSVParser(csvParser)
					.build()) {
				
				String[] line;
				while ((line = csvReader.readNext()) != null) {
					lines.add(Lists.newArrayList(line));
				}
			}
		}
		
		if (lines.isEmpty()) {
			throw new IOException(String.format("Error on reading csv file '%s' with encoding '%s'.", fileToImport, charset.name()));
		}
		
		return lines;
	}
}
