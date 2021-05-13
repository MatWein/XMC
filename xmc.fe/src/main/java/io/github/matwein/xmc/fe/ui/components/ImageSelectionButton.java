package io.github.matwein.xmc.fe.ui.components;

import io.github.matwein.xmc.fe.common.ImageUtilFrontend;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.ExtensionFilterType;
import io.github.matwein.xmc.utils.ImageUtil;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Optional;

public class ImageSelectionButton extends Button {
    private Double fitWidth;
    private Double fitHeight;
    private String messageKey; // has to be a string because scene builder cannot render enum parameters

    public ImageSelectionButton() {
    	getStyleClass().addAll("image-selection-button");
    	
        this.setOnAction(event -> {
            Optional<File> selectedFile = DialogHelper.showOpenFileDialog(getScene().getWindow(), ExtensionFilterType.IMAGES);
            if (selectedFile.isPresent()) {
                showImage(ImageUtilFrontend.readFromFile$(selectedFile.get()));
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
        if (image == null) {
            setImage((Image)null);
        } else {
            setImage(ImageUtilFrontend.readFromByteArray$(image));
        }
    }

    public void setImage(Image image) {
        if (image == null) {
            hideImage();
        } else {
            showImage(image);
        }
    }

    public byte[] getImageAsByteArray() {
        Image image = getImage();
        if (image == null) {
            return null;
        } else {
            return ImageUtilFrontend.imageToByteArray$(image);
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
        Image imageToShow;
        if (fitWidth != null && fitHeight != null) {
            imageToShow = ImageUtilFrontend.readFromByteArray$(ImageUtil.resize$(
            		ImageUtilFrontend.imageToByteArray$(image),
		            fitWidth.intValue(),
		            fitHeight.intValue()));
        } else {
            imageToShow = image;
        }

        ImageView imageView = new ImageView(imageToShow);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        return imageView;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
        hideImage();
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
