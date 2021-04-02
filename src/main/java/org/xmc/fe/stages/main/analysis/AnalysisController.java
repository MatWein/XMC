package org.xmc.fe.stages.main.analysis;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

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
			
			Pair<Parent, Object> analysisContent = FxmlComponentFactory.load(FxmlKey.ANALYSIS_CONTENT);
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
