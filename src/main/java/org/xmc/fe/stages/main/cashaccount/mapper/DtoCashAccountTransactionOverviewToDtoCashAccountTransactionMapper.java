package org.xmc.fe.stages.main.cashaccount.mapper;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;

@Component
public class DtoCashAccountTransactionOverviewToDtoCashAccountTransactionMapper {
    public DtoCashAccountTransaction map(DtoCashAccountTransactionOverview input) {
        if (input == null) {
            return null;
        }

        var dto = new DtoCashAccountTransaction();

        dto.setCategory(input.getCategory());
        dto.setCreditorIdentifier(input.getCreditorIdentifier());
        dto.setDescription(input.getDescription());
        dto.setId(input.getId());
        dto.setMandate(input.getMandate());
        dto.setReference(input.getReference());
        dto.setReferenceBank(input.getReferenceBank());
        dto.setReferenceIban(input.getReferenceIban());
        dto.setUsage(input.getUsage());
        dto.setValue(input.getValue().getValue());
        dto.setValutaDate(input.getValutaDate());

        return dto;
    }
}
