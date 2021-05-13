package io.github.matwein.xmc.fe.stages.main.administration.banks.converter;

import io.github.matwein.xmc.fe.common.dtos.DtoBankInformation;
import io.github.matwein.xmc.fe.ui.converter.DtoBankConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DtoBankInformationBicConverter implements Function<DtoBankInformation, String> {
    @Override
    public String apply(DtoBankInformation dtoBankInformation) {
        return StringUtils.abbreviate(String.format("%s - %s (%s / %s)",
                dtoBankInformation.getBic(),
                dtoBankInformation.getBankName(),
                dtoBankInformation.getZipCode(),
                dtoBankInformation.getCity()), DtoBankConverter.MAX_WIDTH);
    }
}
