package org.xmc.fe.stages.login.validation;

import org.springframework.stereotype.Component;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;

@Component
public class UserAlreadyExistsValidator extends CommonUsernameValidator {
    @Override
    protected boolean isError(File userDatabaseDir) {
        return userDatabaseDir.isDirectory();
    }

    @Override
    protected MessageKey getErrorKey() {
        return MessageKey.VALIDATION_USER_ALREADY_EXISTS;
    }
}
