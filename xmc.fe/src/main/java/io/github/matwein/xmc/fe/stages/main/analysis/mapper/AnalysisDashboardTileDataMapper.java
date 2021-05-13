package io.github.matwein.xmc.fe.stages.main.analysis.mapper;

import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisDashboardTileData;
import io.github.matwein.xmc.fe.stages.main.analysis.AnalysisDashboardDialogController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class AnalysisDashboardTileDataMapper implements IDialogMapper<AnalysisDashboardDialogController, DtoAnalysisDashboardTileData> {
	@Override
	public void accept(AnalysisDashboardDialogController controller, DtoAnalysisDashboardTileData dtoAnalysisDashboardTileData) {
		throw new NotImplementedException("Tile loading not supported.");
	}
	
	@Override
	public DtoAnalysisDashboardTileData apply(ButtonData buttonData, AnalysisDashboardDialogController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dtoAnalysisDashboardTileData = new DtoAnalysisDashboardTileData();
		
		dtoAnalysisDashboardTileData.setAnalysisFavouriteId(controller.getAnalysisFavouriteComboBox().getValue().getId());
		dtoAnalysisDashboardTileData.setTileWidth((int)controller.getTileWidthNumberField().getValue());
		dtoAnalysisDashboardTileData.setTileHeight((int)controller.getTileHeightNumberField().getValue());
		
		return dtoAnalysisDashboardTileData;
	}
}
