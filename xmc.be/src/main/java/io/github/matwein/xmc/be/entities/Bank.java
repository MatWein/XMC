package io.github.matwein.xmc.be.entities;

import jakarta.persistence.*;

@Entity
@Table(name = Bank.TABLE_NAME)
public class Bank extends DeletablePersistentObject {
    public static final String TABLE_NAME = "BANKS";

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "BIC", nullable = true)
    private String bic;
    @Column(name = "BLZ", nullable = true)
    private String blz;

    @ManyToOne(optional = true)
    @JoinColumn(name = "LOGO_ID")
    private BinaryData logo;

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

    public BinaryData getLogo() {
        return logo;
    }

    public void setLogo(BinaryData logo) {
        this.logo = logo;
    }
}
