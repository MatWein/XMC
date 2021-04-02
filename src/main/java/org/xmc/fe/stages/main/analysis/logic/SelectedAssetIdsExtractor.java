package org.xmc.fe.stages.main.analysis.logic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetSelection;

@Component
public class SelectedAssetIdsExtractor {
	public Multimap<AssetType, Long> extractSelectedAssetIds(TreeView<DtoAssetSelection> selectedAssetsTreeView) {
		CheckBoxTreeItem<DtoAssetSelection> root = (CheckBoxTreeItem<DtoAssetSelection>)selectedAssetsTreeView.getRoot();
		
		Multimap<AssetType, Long> result = ArrayListMultimap.create();
		populateSelectedAssets(result, root);
		return result;
	}
	
	private void populateSelectedAssets(Multimap<AssetType, Long> result, CheckBoxTreeItem<DtoAssetSelection> node) {
		if (node == null) {
			return;
		}
		
		if (node.getValue().getId() != null && node.isSelected()) {
			result.put(node.getValue().getAssetType(), node.getValue().getId());
		}
		
		for (TreeItem<DtoAssetSelection> child : node.getChildren()) {
			populateSelectedAssets(result, (CheckBoxTreeItem<DtoAssetSelection>)child);
		}
	}
}
