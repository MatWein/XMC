package io.github.matwein.xmc.common.stubs.bank;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public class DtoBankOverview extends DtoBank {
    private LocalDateTime creationDate;

    @QueryProjection
    public DtoBankOverview(Long id, String name, String bic, String blz, byte[] logo, LocalDateTime creationDate) {
        super(id, name, bic, blz, logo);
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
