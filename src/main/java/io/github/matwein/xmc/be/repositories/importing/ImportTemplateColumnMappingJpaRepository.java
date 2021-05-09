package io.github.matwein.xmc.be.repositories.importing;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.importing.ImportTemplateColumnMapping;

public interface ImportTemplateColumnMappingJpaRepository extends JpaRepository<ImportTemplateColumnMapping, Long> {
}
