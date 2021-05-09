package io.github.matwein.xmc.be.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public class PersistentObject {
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(name = "CREATION_DATE", nullable = false)
	protected LocalDateTime creationDate = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	@Override
	public String toString() {
		return String.format("%s [id=%s]", this.name(), id);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !PersistentObject.class.isAssignableFrom(o.getClass())) return false;
		PersistentObject that = (PersistentObject) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
	
	protected String name() {
		return this.getClass().getSimpleName();
	}
}
