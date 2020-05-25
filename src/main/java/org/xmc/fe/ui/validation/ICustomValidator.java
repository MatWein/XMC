package org.xmc.fe.ui.validation;

import javafx.scene.Parent;

import java.util.Collection;

public interface ICustomValidator<T extends Parent> {
    Collection<String> validate(T component);
}
