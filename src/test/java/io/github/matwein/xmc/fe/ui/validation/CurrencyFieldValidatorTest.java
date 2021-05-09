package io.github.matwein.xmc.fe.ui.validation;

import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.JUnitTestBase;

class CurrencyFieldValidatorTest extends JUnitTestBase {
    private CurrencyFieldValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CurrencyFieldValidator();
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
            textField.setText("EUR");

            Assertions.assertEquals(0, validator.validate(textField).size());
        });
    }

    @Test
    void testValidate_invalidIban() throws Throwable {
        runJavaFxCode(() -> {
            TextField textField = new TextField();
            textField.setText("EURO");

            Assertions.assertEquals(1, validator.validate(textField).size());
        });
    }
}