package org.xmc.fe.stages.main.cashaccount.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBankInformation;

import java.util.function.Function;

@Component
public class DtoBankInformationNameConverter implements Function<DtoBankInformation, String> {
    @Override
    public String apply(DtoBankInformation dtoBankInformation) {
        return StringUtils.abbreviate(String.format("%s - (%s / %s / %s / %s)",
                dtoBankInformation.getBankName(),
                dtoBankInformation.getBlz(),
                dtoBankInformation.getBic(),
                dtoBankInformation.getZipCode(),
                dtoBankInformation.getCity()), DtoBankConverter.MAX_WIDTH);
    }
}
