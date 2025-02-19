package io.github.matwein.xmc.be.services.importing.controller;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportLineMapper;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportLineValidator;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.importing.CsvSeparator;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
class ImportPreparationControllerTest extends IntegrationTest {
	@Autowired
	private ImportPreparationController controller;
	
	@Autowired private CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper;
	@Autowired private CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator;
	
	@Mock
	private IAsyncMonitor asyncMonitor;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		Locale.setDefault(Locale.GERMANY);
		MessageAdapter.initBundle();
	}
	
	@Test
	void readAndValidateImportFile_Excel() {
		File fileToImport = new File(getClass().getResource("/importing/Umsatzanzeige_DE11100111171110921111_20200828.xlsx").getFile());
		
		prepareCategories();
		
		DtoImportData<CashAccountTransactionImportColmn> importData = new DtoImportData<>();
		importData.setFileToImport(fileToImport);
		importData.setStartWithLine(16);
		importData.setColmuns(createColumnMapping());
		
		DtoImportFileValidationResult<DtoCashAccountTransaction> result = controller.readAndValidateImportFile(
				asyncMonitor,
				importData,
				cashAccountTransactionImportLineMapper,
				cashAccountTransactionImportLineValidator);
		
		Assertions.assertEquals(new ArrayList<>(), result.getErrors());
		Assertions.assertEquals(0, result.getInvalidTransactionCount());
		Assertions.assertEquals(234, result.getValidTransactionCount());
		Assertions.assertEquals(234, result.getSuccessfullyReadLines().size());
		
		DtoCashAccountTransaction first = result.getSuccessfullyReadLines().getFirst();
		Assertions.assertEquals("Einnahmen", first.getCategory().getName());
		Assertions.assertNotNull(first.getCategory().getId());
		Assertions.assertEquals("Gutschrift", first.getDescription());
		Assertions.assertEquals("Hans Wurst", first.getReference());
		Assertions.assertEquals("Winterraeder", first.getUsage());
		Assertions.assertEquals(350.0, first.getValue().doubleValue(), 0);
		Assertions.assertEquals(LocalDate.of(2020, Month.AUGUST, 27), first.getValutaDate());
		
		DtoCashAccountTransaction next = result.getSuccessfullyReadLines().get(233);
		Assertions.assertEquals("Sparen und Anlegen", next.getCategory().getName());
		Assertions.assertNotNull(next.getCategory().getId());
		Assertions.assertEquals("Lastschrift", next.getDescription());
		Assertions.assertEquals("Fränkische BSK Hamburch", next.getReference());
		Assertions.assertEquals("Fränkische BSK Hamburch Bausparvertrag 6452290601 S parrate 100,00 EUR", next.getUsage());
		Assertions.assertEquals(-100.0, next.getValue().doubleValue(), 0);
		Assertions.assertEquals(LocalDate.of(2019, Month.AUGUST, 30), next.getValutaDate());
	}
	
	@Test
	void readAndValidateImportFile_Csv() {
		File fileToImport = new File(getClass().getResource("/importing/Umsatzanzeige_DE11100111171110921111_20200828.csv").getFile());
		
		prepareCategories();
		
		DtoImportData<CashAccountTransactionImportColmn> importData = new DtoImportData<>();
		importData.setFileToImport(fileToImport);
		importData.setStartWithLine(16);
		importData.setCsvSeparator(CsvSeparator.SEMICOLON);
		importData.setColmuns(createColumnMapping());
		
		DtoImportFileValidationResult<DtoCashAccountTransaction> result = controller.readAndValidateImportFile(
				asyncMonitor,
				importData,
				cashAccountTransactionImportLineMapper,
				cashAccountTransactionImportLineValidator);
		
		Assertions.assertEquals(new ArrayList<>(), result.getErrors());
		Assertions.assertEquals(0, result.getInvalidTransactionCount());
		Assertions.assertEquals(234, result.getValidTransactionCount());
		Assertions.assertEquals(234, result.getSuccessfullyReadLines().size());
		
		Assertions.assertEquals("Einnahmen", result.getSuccessfullyReadLines().get(2).getCategory().getName());
		Assertions.assertNotNull(result.getSuccessfullyReadLines().get(2).getCategory().getId());
		Assertions.assertEquals("Gutschrift", result.getSuccessfullyReadLines().get(2).getDescription());
		Assertions.assertEquals("SomeCompany GmbH", result.getSuccessfullyReadLines().get(2).getReference());
		Assertions.assertEquals("Gehalt August 2020", result.getSuccessfullyReadLines().get(2).getUsage());
		Assertions.assertEquals(1478.42, result.getSuccessfullyReadLines().get(2).getValue().doubleValue(), 0);
		Assertions.assertEquals(LocalDate.of(2020, Month.AUGUST, 26), result.getSuccessfullyReadLines().get(2).getValutaDate());
		
		Assertions.assertEquals("Sparen und Anlegen", result.getSuccessfullyReadLines().get(233).getCategory().getName());
		Assertions.assertNotNull(result.getSuccessfullyReadLines().get(233).getCategory().getId());
		Assertions.assertEquals("Lastschrift", result.getSuccessfullyReadLines().get(233).getDescription());
		Assertions.assertEquals("Fränkische BSK Hamburch", result.getSuccessfullyReadLines().get(233).getReference());
		Assertions.assertEquals("Fränkische BSK Hamburch Bausparvertrag 6452290601 S parrate 100,00 EUR", result.getSuccessfullyReadLines().get(233).getUsage());
		Assertions.assertEquals(-100.0, result.getSuccessfullyReadLines().get(233).getValue().doubleValue(), 0);
		Assertions.assertEquals(LocalDate.of(2019, Month.AUGUST, 30), result.getSuccessfullyReadLines().get(233).getValutaDate());
	}
	
	@Test
	void readAndValidateImportFile_Csv_Errors() {
		File fileToImport = new File(getClass().getResource("/importing/Umsatzanzeige_DE11100111171110921111_20200828_errors.csv").getFile());
		
		prepareCategories();
		
		DtoImportData<CashAccountTransactionImportColmn> importData = new DtoImportData<>();
		importData.setFileToImport(fileToImport);
		importData.setStartWithLine(16);
		importData.setCsvSeparator(CsvSeparator.SEMICOLON);
		importData.setColmuns(createColumnMapping());
		
		DtoImportFileValidationResult<DtoCashAccountTransaction> result = controller.readAndValidateImportFile(
				asyncMonitor,
				importData,
				cashAccountTransactionImportLineMapper,
				cashAccountTransactionImportLineValidator);
		
		Assertions.assertEquals(2, result.getErrors().size());
		Assertions.assertEquals(2, result.getInvalidTransactionCount());
		Assertions.assertEquals(232, result.getValidTransactionCount());
		Assertions.assertEquals(232, result.getSuccessfullyReadLines().size());
		
		Assertions.assertEquals("Einnahmen", result.getSuccessfullyReadLines().get(2).getCategory().getName());
		Assertions.assertNotNull(result.getSuccessfullyReadLines().get(2).getCategory().getId());
		Assertions.assertEquals("Gutschrift", result.getSuccessfullyReadLines().get(2).getDescription());
		Assertions.assertEquals("SomeCompany GmbH", result.getSuccessfullyReadLines().get(2).getReference());
		Assertions.assertEquals("Gehalt August 2020", result.getSuccessfullyReadLines().get(2).getUsage());
		Assertions.assertEquals(1478.42, result.getSuccessfullyReadLines().get(2).getValue().doubleValue(), 0);
		Assertions.assertEquals(LocalDate.of(2020, Month.AUGUST, 26), result.getSuccessfullyReadLines().get(2).getValutaDate());
		
		Assertions.assertEquals("Sparen und Anlegen", result.getSuccessfullyReadLines().get(231).getCategory().getName());
		Assertions.assertNotNull(result.getSuccessfullyReadLines().get(231).getCategory().getId());
		Assertions.assertEquals("Lastschrift", result.getSuccessfullyReadLines().get(231).getDescription());
		Assertions.assertEquals("Fränkische BSK Hamburch", result.getSuccessfullyReadLines().get(231).getReference());
		Assertions.assertEquals("Fränkische BSK Hamburch Bausparvertrag 6452290601 S parrate 100,00 EUR", result.getSuccessfullyReadLines().get(231).getUsage());
		Assertions.assertEquals(-100.0, result.getSuccessfullyReadLines().get(231).getValue().doubleValue(), 0);
		Assertions.assertEquals(LocalDate.of(2019, Month.AUGUST, 30), result.getSuccessfullyReadLines().get(231).getValutaDate());
		
		Assertions.assertEquals(10, result.getErrors().get(0).getLine());
		Assertions.assertEquals("Wert in Spalte 'Valuta-Datum' darf nicht leer/ungültig sein.", result.getErrors().get(0).getDescription());
		
		Assertions.assertEquals(16, result.getErrors().get(1).getLine());
		Assertions.assertEquals("Wert in Spalte 'Wert' darf nicht leer/ungültig sein.", result.getErrors().get(1).getDescription());
	}
	
	private void prepareCategories() {
		graphGenerator.createCategory("Einnahmen");
		graphGenerator.createCategory("Banking und Bargeld");
		graphGenerator.createCategory("Lebensmittel und Haushalt");
		graphGenerator.createCategory("Shopping und Media");
		graphGenerator.createCategory("Sparen und Anlegen");
		
		flush();
	}
	
	private List<DtoColumnMapping<CashAccountTransactionImportColmn>> createColumnMapping() {
		return List.of(
				new DtoColumnMapping<>(2, CashAccountTransactionImportColmn.VALUTA_DATE),
				new DtoColumnMapping<>(3, CashAccountTransactionImportColmn.REFERENCE),
				new DtoColumnMapping<>(4, CashAccountTransactionImportColmn.DESCRIPTION),
				new DtoColumnMapping<>(5, CashAccountTransactionImportColmn.CATEGORY),
				new DtoColumnMapping<>(6, CashAccountTransactionImportColmn.USAGE),
				new DtoColumnMapping<>(9, CashAccountTransactionImportColmn.VALUE)
		);
	}
}