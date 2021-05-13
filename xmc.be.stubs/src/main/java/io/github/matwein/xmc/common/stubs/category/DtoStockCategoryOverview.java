package io.github.matwein.xmc.common.stubs.category;

import java.time.LocalDateTime;

public class DtoStockCategoryOverview extends DtoStockCategory {
    private LocalDateTime creationDate;

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
