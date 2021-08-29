package io.github.matwein.xmc.be.services.stock.controller;

import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.be.repositories.category.StockCategoryJpaRepository;
import io.github.matwein.xmc.be.repositories.stock.StockJpaRepository;
import io.github.matwein.xmc.be.services.stock.mapper.DtoStockToStockMapper;
import io.github.matwein.xmc.common.stubs.stocks.DtoStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		StockCategory stockCategory = dtoStock.getStockCategory() == null ? null : stockCategoryJpaRepository.getById(dtoStock.getStockCategory().getId());
		
		Stock stock = createOrUpdateStock(dtoStock, stockCategory);
		stockJpaRepository.save(stock);
	}
	
	private Stock createOrUpdateStock(DtoStock dtoStock, StockCategory stockCategory) {
		if (dtoStock.getId() == null) {
			return dtoStockToStockMapper.map(dtoStock, stockCategory);
		} else {
			Stock stock = stockJpaRepository.getById(dtoStock.getId());
			dtoStockToStockMapper.update(stock, dtoStock, stockCategory);
			return stock;
		}
	}
}
