package org.xmc.fe.stages.main.administration.banks.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.bank.DtoBankInformation;
import org.xmc.fe.stages.main.administration.banks.converter.DtoBankInformationBlzConverter;
import org.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;

import java.util.List;

@Component
public class BlzAutoCompleteController extends AutoCompleteByConverterController<DtoBankInformation> {
    @Autowired
    public BlzAutoCompleteController(
            List<DtoBankInformation> bankInformation,
            DtoBankInformationBlzConverter dtoBankInformationBlzConverter) {

        super(bankInformation, dtoBankInformationBlzConverter);
    }
}
