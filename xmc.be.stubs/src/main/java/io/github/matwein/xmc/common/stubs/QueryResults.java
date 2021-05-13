package io.github.matwein.xmc.common.stubs;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;

public class QueryResults<T extends Serializable> implements Serializable {
	private static final QueryResults<Serializable> EMPTY = new QueryResults<>(
			ImmutableList.of(), Long.MAX_VALUE, 0L, 0L);
	
	public static <T extends Serializable> QueryResults<T> emptyResults() {
		return (QueryResults<T>) EMPTY;
	};
	
	private final long limit;
	private final long offset;
	private final long total;
	
	private final List<T> results;
	
	public QueryResults(List<T> results, long limit, long offset, long total) {
		this.limit = limit;
		this.offset = offset;
		this.total = total;
		this.results = results;
	}
	
	public long getLimit() {
		return limit;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public long getTotal() {
		return total;
	}
	
	public List<T> getResults() {
		return results;
	}
	
	public boolean isEmpty() {
		return results.isEmpty();
	}
}
