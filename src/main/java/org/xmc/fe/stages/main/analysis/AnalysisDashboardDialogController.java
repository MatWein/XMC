package org.xmc.fe.stages.main.analysis;

import javafx.fxml.FXML;
import org.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IDialogWithAsyncData;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationNumberField;

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
