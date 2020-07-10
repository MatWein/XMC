package org.xmc.fe.stages.main.administration.banks.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.bank.DtoBankInformation;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankConverter;

import java.util.function.Function;

@Component
public class DtoBankInformationBlzConverter implements Function<DtoBankInformation, String> {
    @Override
    public String apply(DtoBankInformation dtoBankInformation) {
        return StringUtils.abbreviate(String.format("%s - %s (%s / %s)",
                dtoBankInformation.getBlz(),
                dtoBankInformation.getBankName(),
                dtoBankInformation.getZipCode(),
                dtoBankInformation.getCity()), DtoBankConverter.MAX_WIDTH);
    }
}