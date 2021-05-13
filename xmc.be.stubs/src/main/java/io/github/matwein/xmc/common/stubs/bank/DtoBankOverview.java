package io.github.matwein.xmc.common.stubs.bank;

import java.time.LocalDateTime;

public class DtoBankOverview extends DtoBank {
    private LocalDateTime creationDate;

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
