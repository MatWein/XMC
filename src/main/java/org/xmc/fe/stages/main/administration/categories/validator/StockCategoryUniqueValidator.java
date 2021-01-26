package org.xmc.fe.stages.main.administration.categories.validator;

import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.category.StockCategoryService;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.ICustomFieldValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StockCategoryUniqueValidator implements ICustomFieldValidator<TextField> {
	private final StockCategoryService stockCategoryService;
	
	@Autowired
	public StockCategoryUniqueValidator(StockCategoryService stockCategoryService) {
		this.stockCategoryService = stockCategoryService;
	}
	
	@Override
	public Collection<String> validate(TextField component) {
		List<String> errors = new ArrayList<>();
		
		if (StringUtils.isBlank(component.getText())) {
			return errors;
		}
		
		List<String> existingStockCategoryNames = stockCategoryService.loadAllStockCategoryNames();
		if (existingStockCategoryNames.contains(component.getText().trim())) {
			errors.add(MessageAdapter.getByKey(MessageKey.VALIDATION_STOCK_CATEGORY_ALREADY_EXISTS));
		}
		
		return errors;
	}
}
