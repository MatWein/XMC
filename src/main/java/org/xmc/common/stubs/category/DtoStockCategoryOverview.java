package org.xmc.common.stubs.category;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public class DtoStockCategoryOverview extends DtoStockCategory {
    private LocalDateTime creationDate;

    @QueryProjection
    public DtoStockCategoryOverview(Long id, String name, LocalDateTime creationDate) {
        super(id, name);
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
