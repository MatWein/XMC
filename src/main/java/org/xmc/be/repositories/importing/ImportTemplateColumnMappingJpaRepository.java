package org.xmc.be.repositories.importing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.importing.ImportTemplateColumnMapping;

public interface ImportTemplateColumnMappingJpaRepository extends JpaRepository<ImportTemplateColumnMapping, Long> {
}
