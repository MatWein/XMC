package org.xmc.be.entities.cashaccount;

import org.xmc.be.entities.BinaryData;
import org.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;

@Entity
@Table(name = Category.TABLE_NAME)
public class Category extends DeletablePersistentObject {
    public static final String TABLE_NAME = "CATEGORIES";

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ICON_ID")
    private BinaryData icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BinaryData getIcon() {
        return icon;
    }

    public void setIcon(BinaryData icon) {
        this.icon = icon;
    }
}
