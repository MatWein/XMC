package io.github.matwein.xmc.fe.stages.main.analysis;

import javafx.fxml.FXML;
import io.github.matwein.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IDialogWithAsyncData;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationNumberField;

import java.util.List;

@FxmlController
public class AnalysisDashboardDialogController implements IDialogWithAsyncData<List<DtoImportAnalyseFavouriteOverview>> {
	@FXML private ValidationComboBox<DtoImportAnalyseFavouriteOverview> analysisFavouriteComboBox;
	@FXML private ValidationNumberField tileWidthNumberField;
	@FXML private ValidationNumberField tileHeightNumberField;
	
	@FXML
	public void initialize() {
		analysisFavouriteComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoImportAnalyseFavouriteOverview::getName));
	}
	
	@Override
	public void acceptAsyncData(List<DtoImportAnalyseFavouriteOverview> data) {
		analysisFavouriteComboBox.getItems().addAll(data);
	}
	
	public ValidationComboBox<DtoImportAnalyseFavouriteOverview> getAnalysisFavouriteComboBox() {
		return analysisFavouriteComboBox;
	}
	
	public ValidationNumberField getTileWidthNumberField() {
		return tileWidthNumberField;
	}
	
	public ValidationNumberField getTileHeightNumberField() {
		return tileHeightNumberField;
	}
}
