package io.github.matwein.xmc.common.stubs.category;

import java.io.Serializable;

public class DtoCategory implements Serializable {
    private Long id;
    private String name;
    private byte[] icon;

    public DtoCategory() {
    }

    public DtoCategory(String name, byte[] icon) {
        this.name = name;
        this.icon = icon;
    }
	
	public DtoCategory(Long id, String name, byte[] icon) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }
}
