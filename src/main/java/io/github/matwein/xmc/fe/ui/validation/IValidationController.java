package io.github.matwein.xmc.fe.ui.validation;

import com.google.common.collect.Multimap;

public interface IValidationController {
    Multimap<IValidationComponent, String> validate();
}
