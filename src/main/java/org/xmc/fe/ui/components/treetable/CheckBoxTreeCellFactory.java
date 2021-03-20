package org.xmc.fe.ui.components.treetable;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmc.common.stubs.analysis.DtoAssetSelection;

import java.util.Objects;

public class CheckBoxTreeCellFactory<S, T> implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckBoxTreeCellFactory.class);
	
	private String textProperty;
	
	@Override
	public TreeTableCell<S, T> call(TreeTableColumn<S, T> stTreeTableColumn) {
		return new TreeTableCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item instanceof DtoAssetSelection) {
					if (StringUtils.isNotBlank(textProperty)) {
						try {
							Object value = PropertyUtils.getNestedProperty(item, textProperty);
							super.setText(Objects.toString(value, ""));
						} catch (Throwable e) {
							LOGGER.error("Error on getting property '{}' from {}.", textProperty, item, e);
						}
					}
					
					CheckBox checkBox = createCheckBox();
					super.setGraphic(checkBox);
				}
			}
			
			private CheckBox createCheckBox() {
				CheckBox checkBox = new CheckBox();
				checkBox.setPadding(new Insets(0.0, 10.0, 0.0, 0.0));
				((TreeItemWithParams)getTreeTableRow().getTreeItem()).setParam(checkBox);
				
				checkBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
					((BaseTreeTable)getTreeTableView()).onTreeItemSelectionChanged();
					
					for (TreeItem<S> child : getTreeTableRow().getTreeItem().getChildren()) {
						((TreeItemWithParams<S, CheckBox>)child).getParam().setSelected(newValue);
					}
				});
				
				return checkBox;
			}
		};
	}
	
	public String getTextProperty() {
		return textProperty;
	}
	
	public void setTextProperty(String textProperty) {
		this.textProperty = textProperty;
	}
}
