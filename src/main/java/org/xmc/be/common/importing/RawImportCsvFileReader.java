package org.xmc.be.common.importing;

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
import java.util.List;

@Component
public class RawImportCsvFileReader {
	public List<List<String>> read(File fileToImport, int startWithLine, CsvSeparator csvSeparator) throws IOException, CsvValidationException {
		CSVParser csvParser = new CSVParserBuilder()
				.withSeparator(csvSeparator.getCharacter())
				.withIgnoreLeadingWhiteSpace(true)
				.withIgnoreQuotations(true)
				.build();
		
		List<List<String>> lines = Lists.newArrayList();
		
		try (var fileReader = new FileReader(fileToImport)) {
			try (var csvReader = new CSVReaderBuilder(fileReader)
					.withSkipLines(startWithLine)
					.withCSVParser(csvParser)
					.build()) {
				
				String[] line;
				while ((line = csvReader.readNext()) != null) {
					lines.add(Lists.newArrayList(line));
				}
			}
		}
		
		return lines;
	}
}
