package org.xmc.be.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = BinaryData.TABLE_NAME)
public class BinaryData extends PersistentObject {
	public static final String TABLE_NAME = "binary_data";
	
	@Lob
	@Column(name = "RAW_DATA", nullable = false)
	private byte[] rawData;

	@Column(name = "HASH", nullable = false)
	private String hash;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"rawData of size: " + (rawData == null ? 0 : rawData.length) +
				", id=" + id +
				'}';
	}
}
