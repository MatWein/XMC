package org.xmc.fe.ui.components;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.xmc.common.utils.ImageUtil;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;
import java.util.Optional;

public class ImageSelectionButton extends Button {
    private Double fitWidth;
    private Double fitHeight;
    private String messageKey; // has to be a string because scene builder cannot render enum parameters

    public ImageSelectionButton() {
        this.setOnAction(event -> {
            Optional<File> selectedFile = DialogHelper.showOpenFileDialog(getScene().getWindow(), DialogHelper.IMAGE_EXTENSION_FILTER);
            if (selectedFile.isPresent()) {
                showImage(ImageUtil.readFromFile$(selectedFile.get()));
            }
        });
    }

    public Image getImage() {
        if (getGraphic() != null) {
            return ((ImageView)getGraphic()).getImage();
        }
        return null;
    }

    public void setImage(byte[] image) {
        setImage(ImageUtil.readFromByteArray$(image));
    }

    public void setImage(Image image) {
        if (image == null) {
            hideImage();
        } else {
            showImage(image);
        }
    }

    private void showImage(Image image) {
        setGraphic(createLogoImageView(image));
        setText(null);
    }

    private void hideImage() {
        setGraphic(null);
        setText(MessageAdapter.getByKey(MessageKey.valueOf(messageKey)));
    }

    private ImageView createLogoImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        return imageView;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Double getFitWidth() {
        return fitWidth;
    }

    public void setFitWidth(Double fitWidth) {
        this.fitWidth = fitWidth;
    }

    public Double getFitHeight() {
        return fitHeight;
    }

    public void setFitHeight(Double fitHeight) {
        this.fitHeight = fitHeight;
    }
}
