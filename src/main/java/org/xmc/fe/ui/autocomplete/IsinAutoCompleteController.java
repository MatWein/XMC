package org.xmc.fe.ui.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.stock.StockService;
import org.xmc.common.stubs.stocks.DtoMinimalStock;
import org.xmc.fe.ui.validation.components.autocomplete.IAutoCompleteController;

import java.util.List;

@Component
public class IsinAutoCompleteController implements IAutoCompleteController<DtoMinimalStock> {
	private final StockService stockService;
	
	@Autowired
	public IsinAutoCompleteController(StockService stockService) {
		this.stockService = stockService;
	}
	
	@Override
	public List<DtoMinimalStock> search(String searchValue, int limit) {
		return stockService.loadAllStocks(searchValue, limit);
	}
}
