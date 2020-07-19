package org.xmc.common.stubs.category;

import java.io.Serializable;

public class DtoCategory implements Serializable {
    private String name;
    private byte[] icon;

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
