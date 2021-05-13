package io.github.matwein.xmc.be.repositories.analysis;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class AnalysisFavouriteJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private AnalysisFavouriteJpaRepository repository;
	
	@Test
	void testFindByName() {
		graphGenerator.createAnalysisFavourite("name 1", AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		AnalysisFavourite expectedResult = graphGenerator.createAnalysisFavourite("name 2", AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		graphGenerator.createAnalysisFavourite("name 3", AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		
		flushAndClear();
		
		Optional<AnalysisFavourite> result = repository.findByName("name 2");
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(expectedResult, result.get());
	}
	
	@Test
	void testFindByName_NotFound() {
		graphGenerator.createAnalysisFavourite("name 1", AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		graphGenerator.createAnalysisFavourite("name 2", AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		graphGenerator.createAnalysisFavourite("name 3", AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		
		flushAndClear();
		
		Optional<AnalysisFavourite> result = repository.findByName("Name 2");
		
		Assertions.assertFalse(result.isPresent());
	}
}