package io.github.matwein.xmc.be.common.mapper;

import io.github.matwein.xmc.common.stubs.QueryResults;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class QueryResultsMapper {
	public <T extends Serializable> QueryResults<T> map(com.querydsl.core.QueryResults<T> queryResults) {
		if (queryResults == null) {
			return null;
		}
		
		return new QueryResults<>(
				queryResults.getResults(),
				queryResults.getLimit(),
				queryResults.getOffset(),
				queryResults.getTotal());
	}
}
