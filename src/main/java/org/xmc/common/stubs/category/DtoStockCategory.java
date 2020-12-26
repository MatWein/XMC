package org.xmc.common.stubs.category;

import com.querydsl.core.annotations.QueryProjection;

import java.io.Serializable;

public class DtoStockCategory implements Serializable {
    private Long id;
    private String name;

    public DtoStockCategory() {
    }

    public DtoStockCategory(String name) {
        this.name = name;
    }

    @QueryProjection
    public DtoStockCategory(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
