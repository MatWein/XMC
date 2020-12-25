package org.xmc.fe.ui.components;

import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.styles.jmetro.Style;
import org.xmc.common.utils.ImageUtil;
import org.xmc.fe.ui.SceneBuilder;

public class ButtonImageView extends ImageView {
	public ButtonImageView(@NamedArg("url") String url) {
		Image image = new Image(url);
		
		boolean useDarkTheme = SceneBuilder.getStyle() == Style.DARK;
		if (useDarkTheme) {
			image = ImageUtil.invertColors(image);
		}
		
		setImage(image);
	}
}
