package io.github.matwein.xmc.fe.ui.dashboard.tiles;

import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.utils.MessageAdapter;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;

public class TransactionView extends AnchorPane {
	public TransactionView(DtoMostRecentTransaction transaction) {
		getStylesheets().add(FeConstants.DASHBOARD_CSS_PATH);
		getStyleClass().add("transaction-view");
		
		Pane colorPane = new Pane();
		colorPane.setPrefWidth(5.0);
		colorPane.setStyle("-fx-background-color: " + transaction.getAssetColor());
		
		AnchorPane.setTopAnchor(colorPane, 0.0);
		AnchorPane.setBottomAnchor(colorPane, 0.0);
		AnchorPane.setLeftAnchor(colorPane, 0.0);
		
		getChildren().add(colorPane);
		
		String valueWithCurrency = calculateValueText(transaction);
		
		Label valueLabel = new Label(valueWithCurrency);
		
		if (transaction.isPositive()) {
			valueLabel.getStyleClass().add("positive-value");
		} else {
			valueLabel.getStyleClass().add("negative-value");
		}
		
		AnchorPane.setTopAnchor(valueLabel, 5.0);
		AnchorPane.setLeftAnchor(valueLabel, 20.0);
		
		getChildren().add(valueLabel);
		
		Label dateLabel = new Label(MessageAdapter.formatDate(transaction.getDate()));
		
		AnchorPane.setTopAnchor(dateLabel, 5.0);
		AnchorPane.setRightAnchor(dateLabel, 20.0);
		
		getChildren().add(dateLabel);
		
		String description = String.format(
				"%s\n%s",
				StringUtils.defaultString(transaction.getDescription()),
				transaction.getAssetName()
		);
		
		Label descriptionLabel = new Label(description);
		descriptionLabel.setWrapText(false);
		
		AnchorPane.setBottomAnchor(descriptionLabel, 5.0);
		AnchorPane.setLeftAnchor(descriptionLabel, 20.0);
		AnchorPane.setRightAnchor(descriptionLabel, 20.0);
		
		getChildren().add(descriptionLabel);
	}
	
	private String calculateValueText(DtoMostRecentTransaction transaction) {
		StringBuilder valueWithCurrency = new StringBuilder();
		
		if (transaction.isPositive()) {
			valueWithCurrency.append("+");
		}
		
		valueWithCurrency.append(MessageAdapter.formatNumber(transaction.getValue()));
		valueWithCurrency.append(" ");
		valueWithCurrency.append(transaction.getCurrency().getCurrencyCode());
		
		return valueWithCurrency.toString();
	}
}
