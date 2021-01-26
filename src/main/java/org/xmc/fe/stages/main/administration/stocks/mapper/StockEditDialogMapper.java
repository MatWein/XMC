package org.xmc.fe.stages.main.administration.stocks.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.stocks.DtoStock;
import org.xmc.fe.stages.main.administration.stocks.StockEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class StockEditDialogMapper implements IDialogMapper<StockEditController, DtoStock> {
	@Override
	public void accept(StockEditController controller, DtoStock dtoStock) {
	
	}
	
	@Override
	public DtoStock apply(ButtonData buttonData, StockEditController controller) {
		return null;
	}
}
