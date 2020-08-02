package org.xmc.fe.stages.main.administration.banks.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.fe.stages.main.administration.banks.BankEditController;
import org.xmc.fe.ui.IDialogMapper;

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
        dtoBank.setBic(bankEditController.getBankBicAutoComplete().getText());
        dtoBank.setBlz(bankEditController.getBankBlzAutoComplete().getText());
        dtoBank.setName(bankEditController.getBankNameAutoComplete().getText());
        dtoBank.setLogo(bankEditController.getLogoButton().getImageAsByteArray());

        return dtoBank;
    }
}
