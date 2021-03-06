package io.github.matwein.xmc.fe.ui.validation;

import io.github.matwein.xmc.fe.JavaFxTestBase;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IbanFieldValidatorTest extends JavaFxTestBase {
    private IbanFieldValidator validator;

    @BeforeEach
    void setUp() {
        validator = new IbanFieldValidator();
    }

    @Test
    void testValidate_NoInput() throws Throwable {
        runJavaFxCode(() -> {
            TextField textField = new TextField();
            Assertions.assertEquals(0, validator.validate(textField).size());
        });
    }

    @Test
    void testValidate_ValidIban() throws Throwable {
        runJavaFxCode(() -> {
            TextField textField = new TextField();
            textField.setText("DE75512108001245126199");

            Assertions.assertEquals(0, validator.validate(textField).size());
        });
    }

    @Test
    void testValidate_invalidIban() throws Throwable {
        runJavaFxCode(() -> {
            TextField textField = new TextField();
            textField.setText("DE75512118001245126199");

            Assertions.assertEquals(1, validator.validate(textField).size());
        });
    }
}