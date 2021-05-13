package io.github.matwein.xmc.be.services.analysis.mapper;

import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnalyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper {
	public List<DtoImportAnalyseFavouriteOverview> mapAll(List<AnalysisFavourite> analysisFavourites) {
		return analysisFavourites.stream()
				.map(this::map)
				.sorted(Comparator.comparing(DtoImportAnalyseFavouriteOverview::getName))
				.collect(Collectors.toList());
	}
	
	private DtoImportAnalyseFavouriteOverview map(AnalysisFavourite analysisFavourite) {
		var dtoImportAnalyseFavouriteOverview = new DtoImportAnalyseFavouriteOverview();
		
		dtoImportAnalyseFavouriteOverview.setId(analysisFavourite.getId());
		dtoImportAnalyseFavouriteOverview.setName(analysisFavourite.getName());
		
		return dtoImportAnalyseFavouriteOverview;
	}
}
