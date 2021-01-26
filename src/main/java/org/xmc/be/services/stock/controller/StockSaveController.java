package org.xmc.be.services.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Stock;
import org.xmc.be.entities.depot.StockCategory;
import org.xmc.be.repositories.category.StockCategoryJpaRepository;
import org.xmc.be.repositories.stock.StockJpaRepository;
import org.xmc.be.services.stock.mapper.DtoStockToStockMapper;
import org.xmc.common.stubs.stocks.DtoStock;

@Component
public class StockSaveController {
	private final StockJpaRepository stockJpaRepository;
	private final DtoStockToStockMapper dtoStockToStockMapper;
	private final StockCategoryJpaRepository stockCategoryJpaRepository;
	
	@Autowired
	public StockSaveController(
			StockJpaRepository stockJpaRepository,
			DtoStockToStockMapper dtoStockToStockMapper,
			StockCategoryJpaRepository stockCategoryJpaRepository) {
		
		this.stockJpaRepository = stockJpaRepository;
		this.dtoStockToStockMapper = dtoStockToStockMapper;
		this.stockCategoryJpaRepository = stockCategoryJpaRepository;
	}
	
	public void saveOrUpdate(DtoStock dtoStock) {
		StockCategory stockCategory = dtoStock.getStockCategory() == null ? null : stockCategoryJpaRepository.getOne(dtoStock.getStockCategory().getId());
		
		Stock stock = createOrUpdateStock(dtoStock, stockCategory);
		stockJpaRepository.save(stock);
	}
	
	private Stock createOrUpdateStock(DtoStock dtoStock, StockCategory stockCategory) {
		if (dtoStock.getId() == null) {
			return dtoStockToStockMapper.map(dtoStock, stockCategory);
		} else {
			Stock stock = stockJpaRepository.getOne(dtoStock.getId());
			dtoStockToStockMapper.update(stock, dtoStock, stockCategory);
			return stock;
		}
	}
}
