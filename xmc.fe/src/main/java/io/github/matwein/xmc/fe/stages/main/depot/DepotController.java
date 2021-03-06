package io.github.matwein.xmc.fe.stages.main.depot;

import io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.components.BreadcrumbBar;
import io.github.matwein.xmc.fe.ui.components.BreadcrumbBar.BreadcrumbPathElement;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;

@FxmlController
public class DepotController {
	public static final String DEPOT_CONTENT_CONTAINER_ID = "#depotContentContainer";
	
	@FXML private VBox rootVbox;
	@FXML private BreadcrumbBar<String> breadcrumbBar;
	
	private DtoDepotOverview selectedDepot;
	private DtoDepotDeliveryOverview selectedDelivery;
	
	@FXML
	public void initialize() {
		switchToOverview();
	}
	
	public void switchToOverview() {
		this.selectedDepot = null;
		this.selectedDelivery = null;
		
		breadcrumbBar.getElements().clear();
		
		String text = MessageAdapter.getByKey(MessageKey.MAIN_DEPOT_BREADCRUMB_OVERVIEW);
		breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(text, e -> switchToOverview()));
		
		switchContentComponent(FxmlKey.DEPOTS_OVERVIEW);
	}
	
	public void switchToTransactions(DtoDepotOverview selectedDepot) {
		this.selectedDepot = selectedDepot;
		this.selectedDelivery = null;
		
		String text = MessageAdapter.getByKey(MessageKey.MAIN_DEPOT_BREADCRUMB_TRANSACTIONS, selectedDepot.getName());
		breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(text));
		
		switchContentComponent(FxmlKey.DEPOT_TRANSACTIONS);
	}
	
	public void switchToDeliveries(DtoDepotOverview selectedDepot) {
		this.selectedDepot = selectedDepot;
		this.selectedDelivery = null;
		
		breadcrumbBar.getElements().clear();
		
		String text = MessageAdapter.getByKey(MessageKey.MAIN_DEPOT_BREADCRUMB_OVERVIEW);
		breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(text, e -> switchToOverview()));
		
		text = MessageAdapter.getByKey(MessageKey.MAIN_DEPOT_BREADCRUMB_DELIVERIES, selectedDepot.getName());
		breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(text, e -> switchToDeliveries(selectedDepot)));
		
		switchContentComponent(FxmlKey.DEPOT_DELIVERIES);
	}
	
	public void switchToDepotItems(DtoDepotDeliveryOverview selectedDelivery) {
		this.selectedDelivery = selectedDelivery;
		
		String text = MessageAdapter.getByKey(MessageKey.MAIN_DEPOT_BREADCRUMB_DEPOTITEMS, MessageAdapter.formatDateTime(selectedDelivery.getDeliveryDate()));
		breadcrumbBar.getElements().add(new BreadcrumbPathElement<>(text));
		
		switchContentComponent(FxmlKey.DEPOT_DEPOTITEMS);
	}
	
	private void switchContentComponent(FxmlKey newComponentKey) {
		rootVbox.getChildren().remove(rootVbox.lookup(DEPOT_CONTENT_CONTAINER_ID));
		
		Pair<Parent, ? extends IAfterInit<DepotController>> componentPair = FxmlComponentFactory.load(newComponentKey);
		componentPair.getRight().afterInitialize(this);
		rootVbox.getChildren().add(componentPair.getLeft());
	}
	
	public DtoDepotOverview getSelectedDepot() {
		return selectedDepot;
	}
	
	public DtoDepotDeliveryOverview getSelectedDelivery() {
		return selectedDelivery;
	}
}
