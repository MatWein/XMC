package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetSelection;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SelectedAssetIdsExtractor {
	public Map<AssetType, List<Long>> extractSelectedAssetIds(TreeView<DtoAssetSelection> selectedAssetsTreeView) {
		CheckBoxTreeItem<DtoAssetSelection> root = (CheckBoxTreeItem<DtoAssetSelection>)selectedAssetsTreeView.getRoot();
		
		Map<AssetType, List<Long>> result = new HashMap<>();
		populateSelectedAssets(result, root);
		return result;
	}
	
	private void populateSelectedAssets(Map<AssetType, List<Long>> result, CheckBoxTreeItem<DtoAssetSelection> node) {
		if (node == null) {
			return;
		}
		
		if (node.getValue().getId() != null && node.isSelected()) {
			AssetType assetType = node.getValue().getAssetType();
			
			List<Long> ids = result.getOrDefault(assetType, new ArrayList<>());
			ids.add(node.getValue().getId());
			result.put(assetType, ids);
		}
		
		for (TreeItem<DtoAssetSelection> child : node.getChildren()) {
			populateSelectedAssets(result, (CheckBoxTreeItem<DtoAssetSelection>)child);
		}
	}
}
