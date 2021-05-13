package io.github.matwein.xmc.fe.ui.validation.components;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.ExtensionFilterType;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import io.github.matwein.xmc.fe.ui.components.IInitialFocus;
import io.github.matwein.xmc.fe.ui.validation.ICustomValidator;
import io.github.matwein.xmc.fe.ui.validation.IEqualTo;
import io.github.matwein.xmc.fe.ui.validation.IRequired;
import io.github.matwein.xmc.fe.ui.validation.IValidationComponent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Optional;

public class ValidationFileChooserField extends HBox implements IValidationComponent, IInitialFocus, IRequired, IEqualTo, ICustomValidator {
    private final TextField editor;
    private final Button chooseFileButton;

    private boolean initialFocus;
    private boolean required;
    private String equalTo;
    private String customValidator;
    private ExtensionFilterType filterType;

    public ValidationFileChooserField() {
        this.setFillHeight(true);
        this.setAlignment(Pos.CENTER_LEFT);

        editor = new TextField();
        this.getChildren().add(editor);

        chooseFileButton = new Button(MessageAdapter.getByKey(MessageKey.FILECHOOSER_SELECT_FILE));
        chooseFileButton.setPrefWidth(120.0);
        chooseFileButton.setOnAction(event -> selectFile(chooseFileButton));
        this.getChildren().add(chooseFileButton);

        HBox.setHgrow(editor, Priority.ALWAYS);
    }

    private void selectFile(Button chooseFileButton) {
        Optional<File> selectedFile = DialogHelper.showOpenFileDialog(chooseFileButton.getScene().getWindow(), filterType);
        if (selectedFile.isPresent()) {
            editor.setText(selectedFile.get().getAbsolutePath());
            SceneUtil.getOrCreateValidationSceneState(editor.getScene()).validate();
        }
    }

    @Override
    public LinkedHashSet<String> validate() {
        LinkedHashSet<String> errors = CommonTextfieldValidator.validate(this, ValidationFileChooserField::getEditor);

        if (StringUtils.isNotBlank(editor.getText()) && !new File(editor.getText()).isFile()) {
            errors.add(MessageAdapter.getByKey(MessageKey.VALIDATION_INVALID_FILE_PATH));
        }

        String extension = FilenameUtils.getExtension(editor.getText());
        if (!filterType.getExtensionFilter().getExtensions().contains("*." + extension)) {
            errors.add(MessageAdapter.getByKey(MessageKey.VALIDATION_INVALID_FILE_EXTENSION));
        }

        return errors;
    }

    @Override
    public void initialize(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(editor, scene);

        if (initialFocus) {
            requestInitialFocus();
        }
    }

    @Override
    public String getCssClassInvalid() {
        return ValidationTextField.CSS_CLASS_INVALID;
    }

    @Override
    public void setTooltip(Tooltip tooltip) {
        editor.setTooltip(tooltip);
    }

    @Override
    public void addStyleClass(String styleClass) {
        editor.getStyleClass().add(styleClass);
    }

    @Override
    public void removeStyleClass(String styleClass) {
        editor.getStyleClass().removeAll(styleClass);
    }

    @Override
    public boolean isInitialFocus() {
        return initialFocus;
    }

    @Override
    public void setInitialFocus(boolean initialFocus) {
        this.initialFocus = initialFocus;
    }

    public TextField getEditor() {
        return editor;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String getEqualTo() {
        return equalTo;
    }

    @Override
    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    @Override
    public String getCustomValidator() {
        return customValidator;
    }

    @Override
    public void setCustomValidator(String customValidator) {
        this.customValidator = customValidator;
    }

    public File getValue() {
        if (isValid()) {
            return new File(editor.getText());
        }

        return null;
    }

    public String getValueAsString() {
        if (isValid()) {
            return editor.getText();
        }

        return null;
    }

    public String getFilterType() {
        return filterType.name();
    }

    public void setFilterType(String filterType) {
        this.filterType = ExtensionFilterType.valueOf(filterType);
    }
}
