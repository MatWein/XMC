package io.github.matwein.xmc.fe.ui.autocomplete;

import io.github.matwein.xmc.common.services.stock.IStockService;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.IAutoCompleteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IsinAutoCompleteController implements IAutoCompleteController<DtoMinimalStock> {
	private final IStockService stockService;
	
	@Autowired
	public IsinAutoCompleteController(IStockService stockService) {
		this.stockService = stockService;
	}
	
	@Override
	public List<DtoMinimalStock> search(String searchValue, int limit) {
		return stockService.loadAllStocks(searchValue, limit);
	}
}
