package io.github.matwein.xmc.fe.stages.main.administration.stocks.validator;

import io.github.matwein.xmc.common.services.stock.IStockService;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.validation.ICustomFieldValidator;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StockIsinUniqueValidator implements ICustomFieldValidator<TextField> {
	private final IStockService stockService;
	
	@Autowired
	public StockIsinUniqueValidator(IStockService stockService) {
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
