package io.github.matwein.xmc.fe.ui.dashboard.tiles;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.fe.FeConstants;

import java.util.List;

public class TransactionViewPane extends ScrollPane {
	private final VBox vBox;
	
	public TransactionViewPane() {
		vBox = new VBox();
		
		getStylesheets().add(FeConstants.DASHBOARD_CSS_PATH);
		getStyleClass().add("transaction-view-pane");
		
		setFitToWidth(true);
		setContent(vBox);
	}
	
	public void applyData(List<DtoMostRecentTransaction> transactions) {
		vBox.getChildren().clear();
		
		for (DtoMostRecentTransaction transaction : transactions) {
			vBox.getChildren().add(new TransactionView(transaction));
		}
	}
}
