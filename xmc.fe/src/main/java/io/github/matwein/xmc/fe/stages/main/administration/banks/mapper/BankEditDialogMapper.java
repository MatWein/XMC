package io.github.matwein.xmc.fe.stages.main.administration.banks.mapper;

import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.fe.stages.main.administration.banks.BankEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

@Component
public class BankEditDialogMapper implements IDialogMapper<BankEditController, DtoBank> {
    @Override
    public void accept(BankEditController bankEditController, DtoBank dtoBank) {
        if (dtoBank == null) {
            return;
        }

        bankEditController.setBankId(dtoBank.getId());
        bankEditController.getBankBicAutoComplete().setText(dtoBank.getBic());
        bankEditController.getBankBlzAutoComplete().setText(dtoBank.getBlz());
        bankEditController.getBankNameAutoComplete().setText(dtoBank.getName());
        bankEditController.getLogoButton().setImage(dtoBank.getLogo());
    }

    @Override
    public DtoBank apply(ButtonData buttonData, BankEditController bankEditController) {
        if (buttonData != ButtonData.OK_DONE) {
            return null;
        }

        var dtoBank = new DtoBank();

        dtoBank.setId(bankEditController.getBankId());
        dtoBank.setBic(bankEditController.getBankBicAutoComplete().getTextOrNull());
        dtoBank.setBlz(bankEditController.getBankBlzAutoComplete().getTextOrNull());
        dtoBank.setName(bankEditController.getBankNameAutoComplete().getTextOrNull());
        dtoBank.setLogo(bankEditController.getLogoButton().getImageAsByteArray());

        return dtoBank;
    }
}
