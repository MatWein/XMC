package io.github.matwein.xmc.fe.stages.login.logic;

import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.StageBuilder;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

public class LoginStageFactory {
	public static Stage createLoginStage(Stage primaryStage, Pair<Parent, ?> component) {
		return StageBuilder.getInstance()
				.withDefaultIcon()
				.withTitleKey(MessageKey.LOGIN_TITLE)
				.resizable(false)
				.withSceneComponent(component.getLeft())
				.build(primaryStage);
	}
}
