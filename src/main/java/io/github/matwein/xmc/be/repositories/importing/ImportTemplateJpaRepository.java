package io.github.matwein.xmc.be.repositories.importing;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.importing.ImportTemplate;
import io.github.matwein.xmc.be.entities.importing.ImportTemplateType;

import java.util.List;
import java.util.Optional;

public interface ImportTemplateJpaRepository extends JpaRepository<ImportTemplate, Long> {
	Optional<ImportTemplate> findByTypeAndName(ImportTemplateType type, String name);
	
	List<ImportTemplate> findByType(ImportTemplateType type);
}
