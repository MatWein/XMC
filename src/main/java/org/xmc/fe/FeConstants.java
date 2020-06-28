package org.xmc.fe;

import javafx.scene.image.Image;
import org.xmc.common.utils.ImageUtil;

public interface FeConstants {
    String APP_ICON_PATH = "/images/XMC_512.png";
    Image APP_ICON = ImageUtil.readFromClasspath$(APP_ICON_PATH);

    String BASE_CSS_PATH = "/css/xmc-base.css";
}
