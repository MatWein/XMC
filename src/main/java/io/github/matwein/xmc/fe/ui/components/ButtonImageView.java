package io.github.matwein.xmc.fe.ui.components;

import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.styles.jmetro.Style;
import io.github.matwein.xmc.common.utils.ImageUtil;
import io.github.matwein.xmc.fe.ui.SceneBuilder;

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
