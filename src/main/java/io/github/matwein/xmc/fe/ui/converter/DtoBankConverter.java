package io.github.matwein.xmc.fe.ui.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class DtoBankConverter implements Function<DtoBank, String> {
    public static final int MAX_WIDTH = 100;

    @Override
    public String apply(DtoBank dtoBank) {
        List<String> params = new ArrayList<>(2);
        if (StringUtils.isNotBlank(dtoBank.getBic())) {
            params.add(dtoBank.getBic());
        }
        if (StringUtils.isNotBlank(dtoBank.getBlz())) {
            params.add(dtoBank.getBlz());
        }

        String result = dtoBank.getName();
        if (!params.isEmpty()) {
            result += " " + params;
        }
        return StringUtils.abbreviate(result, MAX_WIDTH);
    }
}
