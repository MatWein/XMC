package io.github.matwein.xmc.fe.stages.main.administration.banks.autocomplete;

import io.github.matwein.xmc.fe.common.dtos.DtoBankInformation;
import io.github.matwein.xmc.fe.stages.main.administration.banks.converter.DtoBankInformationNameConverter;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
