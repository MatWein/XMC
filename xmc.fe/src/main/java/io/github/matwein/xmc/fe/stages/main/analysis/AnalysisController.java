package io.github.matwein.xmc.fe.stages.main.analysis;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.atomic.AtomicInteger;

@FxmlController
public class AnalysisController {
	private static final AtomicInteger counter = new AtomicInteger(1);
	
	@FXML private TabPane tabPane;
	@FXML private Tab startTab;
	@FXML private Tab plusTab;
	
	@FXML
	public void initialize() {
		startTab.setText(getAndIncrementTabTitle());
	}
	
	@FXML
	public void onTabSelectionChanged() {
		if (tabPane.getSelectionModel().getSelectedItem() == plusTab) {
			int index = tabPane.getSelectionModel().getSelectedIndex();
			
			Pair<Parent, AnalysisContentController> analysisContent = FxmlComponentFactory.load(FxmlKey.ANALYSIS_CONTENT);
			String title = getAndIncrementTabTitle();
			
			Tab newTab = new Tab(title, analysisContent.getLeft());
			
			tabPane.getTabs().add(index, newTab);
			tabPane.getSelectionModel().select(newTab);
		}
	}
	
	private String getAndIncrementTabTitle() {
		return MessageAdapter.getByKey(MessageKey.ANALYSIS_COUNTER, counter.getAndIncrement());
	}
}
