package io.github.matwein.xmc.fe.stages.main.administration.banks.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.bank.DtoBankInformation;
import io.github.matwein.xmc.fe.stages.main.administration.banks.converter.DtoBankInformationBicConverter;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;

import java.util.List;

@Component
public class BicAutoCompleteController extends AutoCompleteByConverterController<DtoBankInformation> {
    @Autowired
    public BicAutoCompleteController(
            List<DtoBankInformation> bankInformation,
            DtoBankInformationBicConverter dtoBankInformationBicConverter) {

        super(bankInformation, dtoBankInformationBicConverter);
    }
}
