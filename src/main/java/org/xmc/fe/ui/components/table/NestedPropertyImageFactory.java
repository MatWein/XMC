package org.xmc.fe.ui.components.table;

import javafx.beans.NamedArg;
import javafx.scene.image.ImageView;
import org.xmc.common.utils.ImageUtil;

public class NestedPropertyImageFactory extends NestedPropertyValueFactory {
    private final Integer fitToWidth;
    private final Integer fitToHeight;

    public NestedPropertyImageFactory(
            @NamedArg("property") String property,
            @NamedArg("fitToWidth") Integer fitToWidth,
            @NamedArg("fitToHeight") Integer fitToHeight) {

        super(property);
        this.fitToWidth = fitToWidth;
        this.fitToHeight = fitToHeight;
    }

    @Override
    protected Object mapValue(Object value) {
        if (value instanceof byte[]) {
            ImageView imageView = new ImageView(ImageUtil.readFromByteArray$((byte[]) value));

            if (fitToWidth != null) {
                imageView.setFitWidth(fitToWidth);
            }
            if (fitToHeight != null) {
                imageView.setFitHeight(fitToHeight);
            }

            return imageView;
        }

        return value;
    }
}
