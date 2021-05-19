package io.github.matwein.xmc.be.repositories.importing;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.importing.ImportTemplateColumnMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class ImportTemplateColumnMappingJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private ImportTemplateColumnMappingJpaRepository repository;
	
	@Test
	void testFindById() {
		graphGenerator.createImportTemplateColumnMapping();
		ImportTemplateColumnMapping mapping2 = graphGenerator.createImportTemplateColumnMapping();
		graphGenerator.createImportTemplateColumnMapping();
		
		flushAndClear();
		
		Optional<ImportTemplateColumnMapping> result = repository.findById(mapping2.getId());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(mapping2, result.get());
	}
	
	@Test
	void testFindById_NotFound() {
		graphGenerator.createImportTemplateColumnMapping();
		graphGenerator.createImportTemplateColumnMapping();
		graphGenerator.createImportTemplateColumnMapping();
		
		flushAndClear();
		
		Optional<ImportTemplateColumnMapping> result = repository.findById(10000L);
		
		Assertions.assertTrue(result.isEmpty());
	}
}