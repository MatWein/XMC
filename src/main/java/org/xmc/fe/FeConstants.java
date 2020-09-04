package org.xmc.fe;

import javafx.scene.image.Image;
import javafx.util.Duration;
import org.xmc.common.utils.ImageUtil;

public interface FeConstants {
    String APP_ICON_PATH = "/images/XMC_512.png";
    Image APP_ICON = ImageUtil.readFromClasspath$(APP_ICON_PATH);

    Image CHEVRON_LEFT = ImageUtil.readFromClasspath$("/images/feather/chevron-left.png");
    Image CHEVRONS_LEFT = ImageUtil.readFromClasspath$("/images/feather/chevrons-left.png");
    Image CHEVRON_RIGHT = ImageUtil.readFromClasspath$("/images/feather/chevron-right.png");
    Image CHEVRONS_RIGHT = ImageUtil.readFromClasspath$("/images/feather/chevrons-right.png");

    Image CATEGORY_BRIEFCASE = ImageUtil.readFromClasspath$("/images/categories/briefcase24.png");
    Image CATEGORY_CAR_LEFT = ImageUtil.readFromClasspath$("/images/categories/car_left24.png");
    Image CATEGORY_CREDIT_CARD = ImageUtil.readFromClasspath$("/images/categories/credit-card24.png");
    Image CATEGORY_DOLLAR_SIGN = ImageUtil.readFromClasspath$("/images/categories/dollar-sign24.png");
    Image CATEGORY_HOME = ImageUtil.readFromClasspath$("/images/categories/home24.png");
    Image CATEGORY_PHONE = ImageUtil.readFromClasspath$("/images/categories/phone24.png");
    Image CATEGORY_SHOPPING_CART = ImageUtil.readFromClasspath$("/images/categories/shopping-cart24.png");
    Image CATEGORY_TRENDING_UP = ImageUtil.readFromClasspath$("/images/categories/trending-up24.png");

    String BASE_CSS_PATH = "/css/xmc-base.css";

    Duration DEFAULT_DELAY = Duration.millis(500);


}
