package org.xmc.fe.stages.main.cashaccount.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBankInformation;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankInformationNameConverter;
import org.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;

import java.util.List;

@Component
public class BankNameAutoCompleteController extends AutoCompleteByConverterController<DtoBankInformation> {
    @Autowired
    public BankNameAutoCompleteController(
            List<DtoBankInformation> bankInformation,
            DtoBankInformationNameConverter dtoBankInformationNameConverter) {

        super(bankInformation, dtoBankInformationNameConverter);
    }
}
