package org.xmc.fe.ui.components;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;

public interface ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE> {
    QueryResults<ITEM_TYPE> loadItems(int offset, int limit, SORT_ENUM_TYPE sortBy, Order sortOrder);
}
