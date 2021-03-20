package org.xmc.fe.stages.main.analysis.mapper;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.fe.ui.components.treetable.TreeItemWithParams;

@Component
public class DtoAssetSelectionTreeItemMapper {
	public TreeItemWithParams<DtoAssetSelection, ?> map(DtoAssetSelection dtoAssetSelection) {
		TreeItemWithParams<DtoAssetSelection, ?> treeItem = new TreeItemWithParams<>(dtoAssetSelection);
		treeItem.setExpanded(true);
		
		for (DtoAssetSelection child : dtoAssetSelection.getChildren()) {
			treeItem.getChildren().add(map(child));
		}
		
		return treeItem;
	}
}
