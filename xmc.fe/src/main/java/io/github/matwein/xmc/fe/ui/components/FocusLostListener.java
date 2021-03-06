package io.github.matwein.xmc.fe.ui.components;

import io.github.matwein.xmc.fe.ui.validation.IValidationComponent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public interface FocusLostListener extends ChangeListener<Boolean> {
    static FocusLostListener getInstance(IValidationComponent component, Runnable runnable) {
        return new FocusLostListener() {
            @Override
            public void onAction() {
                runnable.run();
            }

            @Override
            public boolean isValid() {
                return component.isValid();
            }
        };
    }

    @Override
    default void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldPropertyValue, Boolean newPropertyValue) {
        if (!Boolean.TRUE.equals(newPropertyValue) && isValid()) {
            onAction();
        }
    }

    void onAction();

    default boolean isValid() {
        return true;
    }
}
