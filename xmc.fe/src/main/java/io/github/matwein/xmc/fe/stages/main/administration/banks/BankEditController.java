package io.github.matwein.xmc.fe.stages.main.administration.banks;

import io.github.matwein.xmc.fe.common.dtos.DtoBankInformation;
import io.github.matwein.xmc.fe.stages.main.administration.banks.converter.DtoBankInformationBicConverter;
import io.github.matwein.xmc.fe.stages.main.administration.banks.converter.DtoBankInformationBlzConverter;
import io.github.matwein.xmc.fe.stages.main.administration.banks.converter.DtoBankInformationNameConverter;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.ImageSelectionButton;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

@FxmlController
public class BankEditController {
    private final DtoBankInformationBlzConverter dtoBankInformationBlzConverter;
    private final DtoBankInformationBicConverter dtoBankInformationBicConverter;
    private final DtoBankInformationNameConverter dtoBankInformationNameConverter;

    private Long bankId;

    @FXML private ImageSelectionButton logoButton;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankNameAutoComplete;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankBicAutoComplete;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankBlzAutoComplete;

    @Autowired
    public BankEditController(
            DtoBankInformationBlzConverter dtoBankInformationBlzConverter,
            DtoBankInformationBicConverter dtoBankInformationBicConverter,
            DtoBankInformationNameConverter dtoBankInformationNameConverter) {

        this.dtoBankInformationBlzConverter = dtoBankInformationBlzConverter;
        this.dtoBankInformationBicConverter = dtoBankInformationBicConverter;
        this.dtoBankInformationNameConverter = dtoBankInformationNameConverter;
    }

    @FXML
    public void initialize() {
        bankNameAutoComplete.setContextMenuConverter(dtoBankInformationNameConverter);
        bankNameAutoComplete.setConverter(DtoBankInformation::getBankName);
        bankNameAutoComplete.setItemSelectedConsumer(item -> {
            bankBlzAutoComplete.selectItem(item);
            bankBicAutoComplete.selectItem(item);
        });

        bankBlzAutoComplete.setContextMenuConverter(dtoBankInformationBlzConverter);
        bankBlzAutoComplete.setConverter(DtoBankInformation::getBlz);
        bankBlzAutoComplete.setItemSelectedConsumer(item -> {
            bankNameAutoComplete.selectItem(item);
            bankBicAutoComplete.selectItem(item);
        });

        bankBicAutoComplete.setContextMenuConverter(dtoBankInformationBicConverter);
        bankBicAutoComplete.setConverter(DtoBankInformation::getBic);
        bankBicAutoComplete.setItemSelectedConsumer(item -> {
            bankNameAutoComplete.selectItem(item);
            bankBlzAutoComplete.selectItem(item);
        });
    }

    public ImageSelectionButton getLogoButton() {
        return logoButton;
    }

    public ValidationAutoComplete<DtoBankInformation> getBankNameAutoComplete() {
        return bankNameAutoComplete;
    }

    public ValidationAutoComplete<DtoBankInformation> getBankBicAutoComplete() {
        return bankBicAutoComplete;
    }

    public ValidationAutoComplete<DtoBankInformation> getBankBlzAutoComplete() {
        return bankBlzAutoComplete;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
}
