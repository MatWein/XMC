package io.github.matwein.xmc.common.stubs;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class PagingParams<T extends Enum<T> & IPagingField> implements Serializable {
    private int offset = 0;
    private int limit = 10;
    private T sortBy;
    private Order order;
    private String filter;

    public PagingParams() {
    }

    public PagingParams(int offset, int limit, T sortBy, Order order, String filter) {
        this.offset = offset;
        this.limit = limit;
        this.sortBy = sortBy;
        this.order = order;
        this.filter = filter;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public T getSortBy() {
        return sortBy;
    }

    public void setSortBy(T sortBy) {
        this.sortBy = sortBy;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("offset", offset)
                .append("limit", limit)
                .append("sortBy", sortBy)
                .append("order", order)
                .append("filter", filter)
                .toString();
    }
}
