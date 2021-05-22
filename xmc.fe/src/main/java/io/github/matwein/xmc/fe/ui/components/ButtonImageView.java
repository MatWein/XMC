package io.github.matwein.xmc.fe.ui.components;

import io.github.matwein.xmc.fe.common.ImageUtil;
import io.github.matwein.xmc.fe.ui.SceneBuilder;
import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.styles.jmetro.Style;

public class ButtonImageView extends ImageView {
	public ButtonImageView(@NamedArg("url") String url) {
		this(new Image(url));
	}
	
	public ButtonImageView(Image image) {
		boolean useDarkTheme = SceneBuilder.getStyle() == Style.DARK;
		if (useDarkTheme) {
			image = ImageUtil.invertColors(image);
		}
		
		setImage(image);
	}
}
