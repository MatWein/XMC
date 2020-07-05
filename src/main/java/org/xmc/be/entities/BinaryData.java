package org.xmc.be.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = BinaryData.TABLE_NAME)
public class BinaryData extends PersistentObject {
	public static final String TABLE_NAME = "BINARY_DATA";
	
	@Lob
	@Column(name = "RAW_DATA", nullable = false)
	private byte[] rawData;

	@Column(name = "SIZE", nullable = false)
	private long size;

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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("size", size)
				.append("hash", hash)
				.append("description", description)
				.toString();
	}
}
