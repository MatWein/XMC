package io.github.matwein.xmc.fe.stages.login.validation;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;

@Component
public class UserAlreadyExistsFieldValidator extends CommonUsernameFieldValidator {
    @Override
    protected boolean isError(File userDatabaseDir) {
        return userDatabaseDir.isDirectory();
    }

    @Override
    protected MessageKey getErrorKey() {
        return MessageKey.VALIDATION_USER_ALREADY_EXISTS;
    }
}
