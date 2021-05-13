package io.github.matwein.xmc.be.entities.settings;

import io.github.matwein.xmc.be.entities.PersistentObject;
import io.github.matwein.xmc.common.stubs.settings.SettingType;

import javax.persistence.*;

@Entity
@Table(name = Setting.TABLE_NAME)
public class Setting extends PersistentObject {
	public static final String TABLE_NAME = "SETTINGS";
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "TYPE", unique = true)
	private SettingType type;
	
	@Lob
	@Column(nullable = false, name = "VALUE")
	private String value;
	
	public SettingType getType() {
		return type;
	}
	
	public void setType(SettingType type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
