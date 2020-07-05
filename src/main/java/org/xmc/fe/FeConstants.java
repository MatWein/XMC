package org.xmc.fe;

import javafx.scene.image.Image;
import org.xmc.common.utils.ImageUtil;

public interface FeConstants {
    String APP_ICON_PATH = "/images/XMC_512.png";
    Image APP_ICON = ImageUtil.readFromClasspath$(APP_ICON_PATH);

    Image CHEVRON_LEFT = ImageUtil.readFromClasspath$("/images/feather/chevron-left.png");
    Image CHEVRONS_LEFT = ImageUtil.readFromClasspath$("/images/feather/chevrons-left.png");
    Image CHEVRON_RIGHT = ImageUtil.readFromClasspath$("/images/feather/chevron-right.png");
    Image CHEVRONS_RIGHT = ImageUtil.readFromClasspath$("/images/feather/chevrons-right.png");

    String BASE_CSS_PATH = "/css/xmc-base.css";
}
