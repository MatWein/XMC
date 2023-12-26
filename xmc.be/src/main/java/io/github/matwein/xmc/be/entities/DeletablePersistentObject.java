package io.github.matwein.xmc.be.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

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
