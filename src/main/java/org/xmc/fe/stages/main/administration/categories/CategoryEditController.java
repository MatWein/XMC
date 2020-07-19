package org.xmc.fe.stages.main.administration.categories;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.common.utils.ImageUtil;
import org.xmc.fe.FeConstants;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.ComboBoxIconCellFactory;
import org.xmc.fe.ui.components.ImageSelectionButton;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

import java.util.List;

@FxmlController
public class CategoryEditController {
    private static final List<DtoCategory> PRESET_ITEMS = Lists.newArrayList(
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_WORK), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_BRIEFCASE)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_CAR), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_CAR_LEFT)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_CREDIT), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_CREDIT_CARD)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_BUSINESS), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_DOLLAR_SIGN)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_HOME), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_HOME)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_MEDIA), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_PHONE)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_SHOPPING), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_SHOPPING_CART)),
            new DtoCategory(MessageAdapter.getByKey(MessageKey.CATEGORY_DEPOT), ImageUtil.imageToByteArray$(FeConstants.CATEGORY_TRENDING_UP))
    );

    @FXML private ImageSelectionButton iconButton;
    @FXML private ValidationComboBox<DtoCategory> presetComboBox;
    @FXML private ValidationTextField nameTextfield;

    @FXML
    public void initialize() {
        presetComboBox.getItems().setAll(PRESET_ITEMS);
        presetComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_SELECT_PRESET));
        presetComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoCategory::getName));

        presetComboBox.setCellFactory(new ComboBoxIconCellFactory<>(
                item -> ImageUtil.readFromByteArray$(item.getIcon()),
                DtoCategory::getName));

        presetComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                iconButton.setImage(newValue.getIcon());
                nameTextfield.setText(newValue.getName());
            }
        });
    }
}
