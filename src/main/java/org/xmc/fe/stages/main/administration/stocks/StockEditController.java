package org.xmc.fe.stages.main.administration.stocks;

import javafx.fxml.FXML;
import org.xmc.common.stubs.category.DtoStockCategory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IDialogWithAsyncData;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

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
