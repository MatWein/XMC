package io.github.matwein.xmc.fe.stages.main.administration.stocks;

import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IDialogWithAsyncData;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.fxml.FXML;

import java.util.List;

@FxmlController
public class StockEditController implements IDialogWithAsyncData<List<DtoStockCategory>> {
	@FXML private ValidationTextField isinTextfield;
	@FXML private ValidationTextField wknTextfield;
	@FXML private ValidationTextField nameTextfield;
	@FXML private ValidationComboBox<DtoStockCategory> stockCategoryComboBox;
	
	private Long stockId;
	
	@FXML
	public void initialize() {
		stockCategoryComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoStockCategory::getName));
		stockCategoryComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.STOCK_EDIT_SELECT_CATEGORY));
	}
	
	@Override
	public void acceptAsyncData(List<DtoStockCategory> data) {
		stockCategoryComboBox.getItems().clear();
		stockCategoryComboBox.getItems().addAll(data);
	}
	
	public Long getStockId() {
		return stockId;
	}
	
	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}
	
	public ValidationTextField getIsinTextfield() {
		return isinTextfield;
	}
	
	public ValidationTextField getWknTextfield() {
		return wknTextfield;
	}
	
	public ValidationTextField getNameTextfield() {
		return nameTextfield;
	}
	
	public ValidationComboBox<DtoStockCategory> getStockCategoryComboBox() {
		return stockCategoryComboBox;
	}
}
