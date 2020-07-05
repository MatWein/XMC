package org.xmc.fe.stages.main.cashaccount.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBankInformation;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankInformationBlzConverter;
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
