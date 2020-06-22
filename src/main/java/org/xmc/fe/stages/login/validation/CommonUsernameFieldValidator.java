package org.xmc.fe.stages.login.validation;

import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.ICustomFieldValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CommonUsernameFieldValidator implements ICustomFieldValidator<TextField> {
    @Override
    public Collection<String> validate(TextField component) {
        List<String> errorMessages = new ArrayList<>();

        String username = component.getText();
        if (StringUtils.isBlank(username)) {
            return errorMessages;
        }

        File userDatabaseDir = new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser(username));

        if (isError(userDatabaseDir)) {
            errorMessages.add(MessageAdapter.getByKey(getErrorKey()));
        }

        return errorMessages;
    }

    protected abstract boolean isError(File userDatabaseDir);
    protected abstract MessageKey getErrorKey();
}
