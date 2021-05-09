package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.Node;
import org.springframework.stereotype.Component;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.stages.main.analysis.AnalysisContentController;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnalysisControllerFinder {
	public Set<AnalysisContentController> findAllAnalysisController() {
		Set<Node> buttons = MainController.mainWindow.getScene().getRoot().lookupAll("#favouriteMenuButton");
		
		return buttons.stream()
				.map(button -> (AnalysisContentController)button.getUserData())
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}
}
