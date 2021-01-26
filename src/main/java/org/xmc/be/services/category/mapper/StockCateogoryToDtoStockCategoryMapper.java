package org.xmc.be.services.category.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.StockCategory;
import org.xmc.common.stubs.category.DtoStockCategory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockCateogoryToDtoStockCategoryMapper {
	public List<DtoStockCategory> mapAll(List<StockCategory> stockCateogies) {
		return stockCateogies.stream()
				.map(this::map)
				.sorted(Comparator.comparing(DtoStockCategory::getName))
				.collect(Collectors.toList());
	}
	
	public DtoStockCategory map(StockCategory stockCateogy) {
		var dto = new DtoStockCategory();
		
		dto.setId(stockCateogy.getId());
		dto.setName(stockCateogy.getName());
		
		return dto;
	}
}
