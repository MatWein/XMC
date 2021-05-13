package io.github.matwein.xmc.fe.stages.main.administration.stocks.mapper;

import io.github.matwein.xmc.common.stubs.stocks.DtoStock;
import io.github.matwein.xmc.fe.stages.main.administration.stocks.StockEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

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
		dto.setIsin(controller.getIsinTextfield().getTextOrNull());
		dto.setWkn(controller.getWknTextfield().getTextOrNull());
		dto.setName(controller.getNameTextfield().getTextOrNull());
		dto.setStockCategory(controller.getStockCategoryComboBox().getValue());
		
		return dto;
	}
}
