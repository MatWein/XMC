package org.xmc.fe.ui.validation;

import javafx.scene.Parent;

import java.util.Collection;

public interface ICustomFieldValidator<COMPONENT_TYPE extends Parent> {
    Collection<String> validate(COMPONENT_TYPE component);
}
