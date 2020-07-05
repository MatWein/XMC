package org.xmc.fe.ui.components.table;

import com.querydsl.core.types.Order;
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

        switch (sortType) {
            case ASCENDING:
                return Order.DESC;
            case DESCENDING:
                return Order.ASC;
            default:
                return null;
        }
    }
}
