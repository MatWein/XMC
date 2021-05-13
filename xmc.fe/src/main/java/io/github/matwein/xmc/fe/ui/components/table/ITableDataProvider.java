package io.github.matwein.xmc.fe.ui.components.table;

import io.github.matwein.xmc.common.stubs.IPagingField;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.fe.async.AsyncMonitor;

import java.io.Serializable;

public interface ITableDataProvider<ITEM_TYPE extends Serializable, SORT_ENUM_TYPE extends Enum<SORT_ENUM_TYPE> & IPagingField> {
    QueryResults<ITEM_TYPE> loadItems(AsyncMonitor monitor, PagingParams<SORT_ENUM_TYPE> pagingParams);
}
