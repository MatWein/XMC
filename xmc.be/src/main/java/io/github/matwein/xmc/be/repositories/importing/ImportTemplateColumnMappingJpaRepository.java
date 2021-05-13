package io.github.matwein.xmc.be.repositories.importing;

import io.github.matwein.xmc.be.entities.importing.ImportTemplateColumnMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportTemplateColumnMappingJpaRepository extends JpaRepository<ImportTemplateColumnMapping, Long> {
}
