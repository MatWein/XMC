package io.github.matwein.xmc.fe.stages.main.administration.stocks.validator;

import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.services.stock.StockService;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.validation.ICustomFieldValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StockIsinUniqueValidator implements ICustomFieldValidator<TextField> {
	private final StockService stockService;
	
	@Autowired
	public StockIsinUniqueValidator(StockService stockService) {
		this.stockService = stockService;
	}
	
	@Override
	public Collection<String> validate(TextField component) {
		List<String> errors = new ArrayList<>();
		
		if (component.isDisable() || StringUtils.isBlank(component.getText())) {
			return errors;
		}
		
		List<String> existingStockIsins = stockService.loadAllStockIsins();
		if (existingStockIsins.contains(component.getText().toUpperCase().trim())) {
			errors.add(MessageAdapter.getByKey(MessageKey.VALIDATION_STOCK_ISIN_ALREADY_EXISTS));
		}
		
		return errors;
	}
}
