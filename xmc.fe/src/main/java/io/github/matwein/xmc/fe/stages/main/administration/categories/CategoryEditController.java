package io.github.matwein.xmc.fe.stages.main.administration.categories;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.common.ImageUtilFrontend;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.ComboBoxIconCellFactory;
import io.github.matwein.xmc.fe.ui.components.ImageSelectionButton;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.fxml.FXML;

import java.util.List;

@FxmlController
public class CategoryEditController {
    private static final List<DtoCategory> PRESET_ITEMS = Lists.newArrayList(
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_WORK), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_BRIEFCASE)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_CAR), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_CAR_LEFT)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_CREDIT), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_CREDIT_CARD)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_BUSINESS), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_DOLLAR_SIGN)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_HOME), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_HOME)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_MEDIA), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_PHONE)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_SHOPPING), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_SHOPPING_CART)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_DEPOT), ImageUtilFrontend.imageToByteArray$(FeConstants.CATEGORY_TRENDING_UP))
    );

    private Long categoryId;

    @FXML private ImageSelectionButton iconButton;
    @FXML private ValidationComboBox<DtoCategory> presetComboBox;
    @FXML private ValidationTextField nameTextfield;

    @FXML
    public void initialize() {
        presetComboBox.getItems().setAll(PRESET_ITEMS);
        presetComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_SELECT_PRESET));
        presetComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoCategory::getName));

        presetComboBox.setCellFactory(new ComboBoxIconCellFactory<>(
                item -> ImageUtilFrontend.readFromByteArray$(item.getIcon()),
                DtoCategory::getName));

        presetComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                iconButton.setImage(newValue.getIcon());
                nameTextfield.setText(newValue.getName());
            }
        });
    }

    public ImageSelectionButton getIconButton() {
        return iconButton;
    }

    public ValidationTextField getNameTextfield() {
        return nameTextfield;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
