package org.xmc.fe.stages.main.cashaccount.mapper;

import javafx.scene.control.ButtonType;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.fe.stages.main.cashaccount.CashAccountEditController;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Component
public class CashAccountEditDialogMapper implements BiFunction<ButtonType, CashAccountEditController, DtoCashAccount>, BiConsumer<CashAccountEditController, DtoCashAccount> {
    @Override
    public DtoCashAccount apply(ButtonType buttonType, CashAccountEditController controller) {
        return null;
    }

    @Override
    public void accept(CashAccountEditController controller, DtoCashAccount dtoCashAccount) {

    }
}
