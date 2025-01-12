package io.github.matwein.xmc.common.stubs.bank;

import io.github.matwein.xmc.common.annotations.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class DtoBank implements Serializable {
    private Long id;

    private String name;
    private String bic;
    private String blz;

    @JsonIgnore
    private byte[] logo;
	
	public DtoBank() {
	}
	
	public DtoBank(Long id, String name, String bic, String blz, byte[] logo) {
		this.id = id;
		this.name = name;
		this.bic = bic;
		this.blz = blz;
		this.logo = logo;
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

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBlz() {
        return blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("bic", bic)
                .append("blz", blz)
                .toString();
    }
}
