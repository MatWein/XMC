package org.xmc.fe.ui.validation;

import javafx.scene.Parent;

import java.util.Collection;

public interface ICustomValidator<COMPONENT_TYPE extends Parent> {
    Collection<String> validate(COMPONENT_TYPE component);
}
