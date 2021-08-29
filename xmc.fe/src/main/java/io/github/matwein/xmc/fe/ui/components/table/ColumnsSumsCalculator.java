package io.github.matwein.xmc.fe.ui.components.table;

import io.github.matwein.xmc.common.stubs.Money;
import io.github.matwein.xmc.common.stubs.Percentage;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import javafx.scene.text.Text;
import scalc.SCalcBuilder;

import java.util.ArrayList;
import java.util.List;

public class ColumnsSumsCalculator {
	public static String calculateColumnsSums(ExtendedTable<?, ?> extendedTable) {
		List<String> sums = new ArrayList<>();
		
		populateSumForAlLRows(extendedTable, sums);
		populateSumForSelectedRows(extendedTable, sums);
		
		return String.join(" | ", sums);
	}
	
	private static void populateSumForAlLRows(ExtendedTable<?, ?> extendedTable, List<String> sums) {
		populateSumForRows(extendedTable, sums, false, "%s: %s");
	}
	
	private static void populateSumForSelectedRows(ExtendedTable<?, ?> extendedTable, List<String> sums) {
		String selectedText = MessageAdapter.getByKey(MessageKey.TABLE_SELECTED);
		populateSumForRows(extendedTable, sums, true, "%s (" + selectedText + "): %s");
	}
	
	private static void populateSumForRows(
			ExtendedTable<?, ?> extendedTable,
			List<String> sums,
			boolean onlyIncludeSelected,
			String pattern) {
		
		for (ExtendedTableColumn<?, ?> column : extendedTable.getColumns()) {
			if (column.isShowSum()) {
				List<Object> values = collectColumnValues(extendedTable, column, onlyIncludeSelected);
				
				if (!values.isEmpty()) {
					double sum = SCalcBuilder.doubleInstance()
							.sumExpression()
							.build()
							.paramsAsCollection(values)
							.calc();
					
					sums.add(String.format(pattern, column.getText(), MessageAdapter.formatNumber(sum)));
				}
			}
		}
	}
	
	private static List<Object> collectColumnValues(
			ExtendedTable<?, ?> extendedTable,
			ExtendedTableColumn<?, ?> column,
			boolean onlyIncludeSelected) {
		
		if (onlyIncludeSelected && extendedTable.getSelectionModel().getSelectedItems().size() < 2) {
			return new ArrayList<>();
		}
		
		List<Object> values = new ArrayList<>();
		
		for (int i = 0; i < extendedTable.getItems().size(); i++) {
			Object rowValue = column.getCellData(i);
			boolean selected = extendedTable.getSelectionModel().isSelected(i);
			
			if (onlyIncludeSelected && !selected) {
				continue;
			}
			
			if (isNumber(rowValue)) {
				values.add(rowValue);
			} else if (rowValue instanceof Text) {
				Object userData = ((Text) rowValue).getUserData();
				if (isNumber(userData)) {
					values.add(userData);
				}
			}
		}
		
		return values;
	}
	
	private static boolean isNumber(Object rowValue) {
		return rowValue instanceof Number || rowValue instanceof Money || rowValue instanceof Percentage;
	}
}
