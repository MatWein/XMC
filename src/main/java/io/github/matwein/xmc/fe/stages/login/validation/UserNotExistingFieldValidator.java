package io.github.matwein.xmc.fe.stages.login.validation;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;

@Component
public class UserNotExistingFieldValidator extends CommonUsernameFieldValidator {
    @Override
    protected boolean isError(File userDatabaseDir) {
        return !userDatabaseDir.isDirectory();
    }

    @Override
    protected MessageKey getErrorKey() {
        return MessageKey.VALIDATION_USER_NOT_EXISTING;
    }
}
