package org.xmc.fe.stages.main.analysis.mapper;

import javafx.animation.PauseTransition;
import javafx.scene.control.CheckBoxTreeItem;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.fe.FeConstants;

@Component
public class DtoAssetSelectionTreeItemMapper {
	public CheckBoxTreeItem<DtoAssetSelection> map(DtoAssetSelection dtoAssetSelection, Runnable onSelectionChanged) {
		PauseTransition pause = new PauseTransition(FeConstants.DEFAULT_DELAY);
		return map(dtoAssetSelection, onSelectionChanged, pause);
	}
	
	public CheckBoxTreeItem<DtoAssetSelection> map(DtoAssetSelection dtoAssetSelection, Runnable onSelectionChanged, PauseTransition pause) {
		CheckBoxTreeItem treeItem = new CheckBoxTreeItem(dtoAssetSelection);
		treeItem.setExpanded(true);
		treeItem.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), event -> {
			pause.setOnFinished(e -> onSelectionChanged.run());
			pause.playFromStart();
		});
		
		for (DtoAssetSelection child : dtoAssetSelection.getChildren()) {
			treeItem.getChildren().add(map(child, onSelectionChanged, pause));
		}
		
		return treeItem;
	}
}
