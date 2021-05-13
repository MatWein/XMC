package io.github.matwein.xmc.be.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class DeletablePersistentObject extends PersistentObject {
	@Column(name = "DELETION_DATE", nullable = true)
	protected LocalDateTime deletionDate;

	public LocalDateTime getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(LocalDateTime deletionDate) {
		this.deletionDate = deletionDate;
	}
}
