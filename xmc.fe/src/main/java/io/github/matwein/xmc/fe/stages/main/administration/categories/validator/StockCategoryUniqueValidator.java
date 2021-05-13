package io.github.matwein.xmc.fe.stages.main.administration.categories.validator;

import io.github.matwein.xmc.common.services.category.IStockCategoryService;
import io.github.matwein.xmc.fe.ui.validation.ICustomFieldValidator;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StockCategoryUniqueValidator implements ICustomFieldValidator<TextField> {
	private final IStockCategoryService stockCategoryService;
	
	@Autowired
	public StockCategoryUniqueValidator(IStockCategoryService stockCategoryService) {
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
