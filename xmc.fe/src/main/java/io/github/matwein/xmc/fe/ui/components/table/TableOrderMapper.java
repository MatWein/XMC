package io.github.matwein.xmc.fe.ui.components.table;

import io.github.matwein.xmc.common.stubs.Order;
import javafx.scene.control.TableColumn.SortType;
import org.springframework.stereotype.Component;

@Component
public class TableOrderMapper {
    /*
        Reverted order because column icon is also reverted.
     */
    public Order mapOrder(SortType sortType) {
        if (sortType == null) {
            return null;
        }
	
	    return switch (sortType) {
		    case ASCENDING -> Order.DESC;
		    case DESCENDING -> Order.ASC;
	    };
    }
}
