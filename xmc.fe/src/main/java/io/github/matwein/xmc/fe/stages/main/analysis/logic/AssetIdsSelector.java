package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import com.google.common.collect.Multimap;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetSelection;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.springframework.stereotype.Component;

@Component
public class AssetIdsSelector {
	public void selectAssetsById(TreeView<DtoAssetSelection> selectedAssetsTreeView, Multimap<AssetType, Long> assetIds) {
		CheckBoxTreeItem<DtoAssetSelection> root = (CheckBoxTreeItem<DtoAssetSelection>)selectedAssetsTreeView.getRoot();
		populateSelectedAssets(assetIds, root);
	}
	
	private void populateSelectedAssets(Multimap<AssetType, Long> result, CheckBoxTreeItem<DtoAssetSelection> node) {
		node.setSelected(false);
		
		if (result.get(node.getValue().getAssetType()).contains(node.getValue().getId())) {
			node.setSelected(true);
		}
		
		for (TreeItem<DtoAssetSelection> child : node.getChildren()) {
			populateSelectedAssets(result, (CheckBoxTreeItem<DtoAssetSelection>)child);
		}
	}
}
