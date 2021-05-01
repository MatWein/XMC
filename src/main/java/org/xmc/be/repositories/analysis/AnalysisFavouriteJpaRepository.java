package org.xmc.be.repositories.analysis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.analysis.AnalysisFavourite;

import java.util.Optional;

public interface AnalysisFavouriteJpaRepository extends JpaRepository<AnalysisFavourite, Long> {
	Optional<AnalysisFavourite> findByName(String name);
}
