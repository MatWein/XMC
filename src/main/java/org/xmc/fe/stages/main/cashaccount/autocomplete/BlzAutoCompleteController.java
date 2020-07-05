package org.xmc.fe.stages.main.cashaccount.autocomplete;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBankInformation;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankInformationBlzConverter;
import org.xmc.fe.ui.validation.components.IAutoCompleteController;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlzAutoCompleteController implements IAutoCompleteController<DtoBankInformation> {
    private final List<DtoBankInformation> bankInformation;
    private final DtoBankInformationBlzConverter dtoBankInformationBlzConverter;

    @Autowired
    public BlzAutoCompleteController(
            List<DtoBankInformation> bankInformation,
            DtoBankInformationBlzConverter dtoBankInformationBlzConverter) {

        this.bankInformation = bankInformation;
        this.dtoBankInformationBlzConverter = dtoBankInformationBlzConverter;
    }

    @Override
    public List<DtoBankInformation> search(String searchValue, int limit) {
        return bankInformation.stream()
                .filter(dto -> matches(searchValue, dto))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean matches(String searchValue, DtoBankInformation dto) {
        String text = dtoBankInformationBlzConverter.apply(dto);
        return StringUtils.containsIgnoreCase(text, searchValue);
    }
}
