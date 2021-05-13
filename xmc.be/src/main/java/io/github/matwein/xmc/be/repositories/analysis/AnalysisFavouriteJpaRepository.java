package io.github.matwein.xmc.be.repositories.analysis;

import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalysisFavouriteJpaRepository extends JpaRepository<AnalysisFavourite, Long> {
	Optional<AnalysisFavourite> findByName(String name);
}
