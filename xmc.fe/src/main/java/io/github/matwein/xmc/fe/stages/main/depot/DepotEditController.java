package io.github.matwein.xmc.fe.stages.main.depot;

import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IDialogWithAsyncData;
import io.github.matwein.xmc.fe.ui.converter.DtoBankConverter;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationColorPicker;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@FxmlController
public class DepotEditController implements IDialogWithAsyncData<List<DtoBank>> {
	private final DtoBankConverter dtoBankConverter;
	
	private Long depotId;
	
	@FXML private ValidationComboBox<DtoBank> bankComboBox;
	@FXML private ValidationTextField depotNameTextfield;
	@FXML private ValidationTextField depotNumberTextfield;
	@FXML private ValidationColorPicker colorPicker;
	
	@Autowired
	public DepotEditController(DtoBankConverter dtoBankConverter) {
		this.dtoBankConverter = dtoBankConverter;
	}
	
	@FXML
	public void initialize() {
		bankComboBox.setConverter(GenericItemToStringConverter.getInstance(dtoBankConverter));
		bankComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.DEPOT_EDIT_SELECT_BANK));
	}
	
	@Override
	public void acceptAsyncData(List<DtoBank> data) {
		bankComboBox.getItems().addAll(data);
	}
	
	public ValidationComboBox<DtoBank> getBankComboBox() {
		return bankComboBox;
	}
	
	public ValidationTextField getDepotNameTextfield() {
		return depotNameTextfield;
	}
	
	public ValidationTextField getDepotNumberTextfield() {
		return depotNumberTextfield;
	}
	
	public Long getDepotId() {
		return depotId;
	}
	
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	
	public ValidationColorPicker getColorPicker() {
		return colorPicker;
	}
}
