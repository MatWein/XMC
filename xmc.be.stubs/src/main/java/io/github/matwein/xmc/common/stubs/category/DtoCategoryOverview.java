package io.github.matwein.xmc.common.stubs.category;

import java.time.LocalDateTime;

public class DtoCategoryOverview extends DtoCategory {
    private LocalDateTime creationDate;
	
	public DtoCategoryOverview() {
	}
	
	public DtoCategoryOverview(Long id, String name, byte[] icon, LocalDateTime creationDate) {
		super(id, name, icon);
		
		this.creationDate = creationDate;
	}
	
	public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
