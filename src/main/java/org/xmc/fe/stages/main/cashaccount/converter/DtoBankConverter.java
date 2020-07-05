package org.xmc.fe.stages.main.cashaccount.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBank;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class DtoBankConverter implements Function<DtoBank, String> {
    static final int MAX_WIDTH = 100;

    @Override
    public String apply(DtoBank dtoBank) {
        if (dtoBank.getId() == null) {
            return MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_ADD_BANK);
        } else {
            List<String> params = new ArrayList<>(2);
            if (StringUtils.isNotBlank(dtoBank.getBic())) {
                params.add(dtoBank.getBic());
            }
            if (StringUtils.isNotBlank(dtoBank.getBlz())) {
                params.add(dtoBank.getBlz());
            }

            return StringUtils.abbreviate(dtoBank.getName() + " " + params, MAX_WIDTH);
        }
    }
}
