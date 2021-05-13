package io.github.matwein.xmc.fe.stages.login.validation;

import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.springframework.stereotype.Component;

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
