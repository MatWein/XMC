package io.github.matwein.xmc.fe.stages.login.validation;

import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import org.springframework.stereotype.Component;

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
