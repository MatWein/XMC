package org.xmc.fe.stages.main.administration.stocks.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.stocks.DtoStock;
import org.xmc.fe.stages.main.administration.stocks.StockEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class StockEditDialogMapper implements IDialogMapper<StockEditController, DtoStock> {
	@Override
	public void accept(StockEditController controller, DtoStock dto) {
		if (dto == null) {
			return;
		}
		
		controller.setStockId(dto.getId());
		controller.getIsinTextfield().setText(dto.getIsin());
		controller.getWknTextfield().setText(dto.getWkn());
		controller.getNameTextfield().setText(dto.getName());
		controller.getStockCategoryComboBox().getSelectionModel().select(dto.getStockCategory());
		controller.getIsinTextfield().setDisable(true);
	}
	
	@Override
	public DtoStock apply(ButtonData buttonData, StockEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dto = new DtoStock();
		
		dto.setId(controller.getStockId());
		dto.setIsin(controller.getIsinTextfield().getText());
		dto.setWkn(controller.getWknTextfield().getText());
		dto.setName(controller.getNameTextfield().getText());
		dto.setStockCategory(controller.getStockCategoryComboBox().getValue());
		
		return dto;
	}
}
