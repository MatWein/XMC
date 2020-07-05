package org.xmc.fe.ui.components.table;

import com.querydsl.core.QueryResults;
import org.xmc.common.stubs.IPagingField;
import org.xmc.common.stubs.PagingParams;

public interface ITableDataProvider<ITEM_TYPE, SORT_ENUM_TYPE extends Enum<SORT_ENUM_TYPE> & IPagingField> {
    QueryResults<ITEM_TYPE> loadItems(PagingParams<SORT_ENUM_TYPE> pagingParams);
}
