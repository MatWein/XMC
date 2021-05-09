package io.github.matwein.xmc.be.services.stock.mapper;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.common.stubs.stocks.DtoStock;

@Component
public class DtoStockToStockMapper {
	public Stock map(DtoStock dtoStock, StockCategory stockCategory) {
		var stock = new Stock();
		
		stock.setIsin(dtoStock.getIsin());
		
		update(stock, dtoStock, stockCategory);
		
		return stock;
	}
	
	public void update(Stock stock, DtoStock dtoStock, StockCategory stockCategory) {
		stock.setWkn(dtoStock.getWkn());
		stock.setName(dtoStock.getName());
		stock.setStockCategory(stockCategory);
	}
}
