package io.github.matwein.xmc.be.repositories.importing;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.importing.ImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class ImportTemplateJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private ImportTemplateJpaRepository repository;
	
	@Test
	void findByTypeAndName() {
		graphGenerator.createImportTemplate();
		ImportTemplate expectedResult = graphGenerator.createImportTemplate(ImportTemplateType.CASH_ACCOUNT_TRANSACTION, "some name");
		graphGenerator.createImportTemplate();
		
		flushAndClear();
		
		Optional<ImportTemplate> result = repository.findByTypeAndName(ImportTemplateType.CASH_ACCOUNT_TRANSACTION, "some name");
		
		Assertions.assertEquals(expectedResult, result.get());
	}
	
	@Test
	void findByTypeAndName_NotFound() {
		graphGenerator.createImportTemplate();
		graphGenerator.createImportTemplate(ImportTemplateType.CASH_ACCOUNT_TRANSACTION, "some name");
		graphGenerator.createImportTemplate();
		
		flushAndClear();
		
		Optional<ImportTemplate> result = repository.findByTypeAndName(ImportTemplateType.CASH_ACCOUNT_TRANSACTION, "some name 2");
		
		Assertions.assertEquals(Optional.empty(), result);
	}
	
	@Test
	void testFindByType() {
		ImportTemplate importTemplate1 = graphGenerator.createImportTemplate();
		ImportTemplate importTemplate2 = graphGenerator.createImportTemplate(ImportTemplateType.CASH_ACCOUNT_TRANSACTION, "some name");
		ImportTemplate importTemplate3 = graphGenerator.createImportTemplate();
		
		flushAndClear();
		
		List<ImportTemplate> result = repository.findByType(ImportTemplateType.CASH_ACCOUNT_TRANSACTION);
		
		Assertions.assertEquals(Set.of(importTemplate1, importTemplate2, importTemplate3), new HashSet<>(result));
	}
	
	@Test
	void testFindByType_NotFound() {
		List<ImportTemplate> result = repository.findByType(ImportTemplateType.CASH_ACCOUNT_TRANSACTION);
		
		Assertions.assertEquals(new ArrayList<>(), result);
	}
}