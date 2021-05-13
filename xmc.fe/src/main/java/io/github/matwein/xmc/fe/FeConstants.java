package io.github.matwein.xmc.fe;

import io.github.matwein.xmc.fe.common.ImageUtilFrontend;
import javafx.scene.image.Image;
import javafx.util.Duration;

public interface FeConstants {
    String APP_ICON_PATH = "/images/XMC_512.png";
    Image APP_ICON = ImageUtilFrontend.readFromClasspath$(APP_ICON_PATH);

    Image CHEVRON_LEFT = ImageUtilFrontend.readFromClasspath$("/images/feather/chevron-left.png");
    Image CHEVRONS_LEFT = ImageUtilFrontend.readFromClasspath$("/images/feather/chevrons-left.png");
    Image CHEVRON_RIGHT = ImageUtilFrontend.readFromClasspath$("/images/feather/chevron-right.png");
    Image CHEVRONS_RIGHT = ImageUtilFrontend.readFromClasspath$("/images/feather/chevrons-right.png");

    Image CATEGORY_BRIEFCASE = ImageUtilFrontend.readFromClasspath$("/images/categories/briefcase24.png");
    Image CATEGORY_CAR_LEFT = ImageUtilFrontend.readFromClasspath$("/images/categories/car_left24.png");
    Image CATEGORY_CREDIT_CARD = ImageUtilFrontend.readFromClasspath$("/images/categories/credit-card24.png");
    Image CATEGORY_DOLLAR_SIGN = ImageUtilFrontend.readFromClasspath$("/images/categories/dollar-sign24.png");
    Image CATEGORY_HOME = ImageUtilFrontend.readFromClasspath$("/images/categories/home24.png");
    Image CATEGORY_PHONE = ImageUtilFrontend.readFromClasspath$("/images/categories/phone24.png");
    Image CATEGORY_SHOPPING_CART = ImageUtilFrontend.readFromClasspath$("/images/categories/shopping-cart24.png");
    Image CATEGORY_TRENDING_UP = ImageUtilFrontend.readFromClasspath$("/images/categories/trending-up24.png");
    
    Image IMAGE_MOVE = ImageUtilFrontend.readFromClasspath$("/images/feather/move.png");
    Image IMAGE_MINUS_SQUARE = ImageUtilFrontend.readFromClasspath$("/images/feather/minus-square.png");

    String BASE_CSS_PATH = "/css/xmc-base.css";
    String DASHBOARD_CSS_PATH = "/css/main/dashboard/dashboard.css";

    Duration DEFAULT_DELAY = Duration.millis(500);
}
