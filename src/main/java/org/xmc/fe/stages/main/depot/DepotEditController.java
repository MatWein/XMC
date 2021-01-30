package org.xmc.fe.stages.main.depot;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IDialogWithAsyncData;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.DtoBankConverter;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

import java.util.List;

@FxmlController
public class DepotEditController implements IDialogWithAsyncData<List<DtoBank>> {
	private final DtoBankConverter dtoBankConverter;
	
	private Long depotId;
	
	@FXML private ValidationComboBox<DtoBank> bankComboBox;
	@FXML private ValidationTextField depotNameTextfield;
	@FXML private ValidationTextField depotNumberTextfield;
	
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
}
