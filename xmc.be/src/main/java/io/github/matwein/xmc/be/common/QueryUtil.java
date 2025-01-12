package io.github.matwein.xmc.be.common;

import io.github.matwein.xmc.be.common.pagination.PagingFieldMapperFactory;
import io.github.matwein.xmc.common.stubs.IPagingField;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class QueryUtil {
	public static <T extends Serializable> QueryResults<T> fromPage(Page<T> page) {
		return new QueryResults<>(
				page.stream().toList(),
				page.getPageable().getPageSize(),
				page.getPageable().getOffset(),
				page.getTotalElements());
	}
	
	public static <FIELD_ENUM_TYPE extends Enum<FIELD_ENUM_TYPE> & IPagingField> Pageable toPageable(
			PagingParams<FIELD_ENUM_TYPE> pagingParams,
			FIELD_ENUM_TYPE defaultSortBy,
			Order defaultOrder) {
		
		int pageNumber = pagingParams.getOffset() / pagingParams.getLimit();
		
		if (pagingParams.getOrder() != null && pagingParams.getSortBy() != null) {
			var pagingFieldMapper = PagingFieldMapperFactory.create(pagingParams.getSortBy());
			var sortField = pagingFieldMapper.map(pagingParams.getSortBy());
			
			return PageRequest.of(
					pageNumber,
					pagingParams.getLimit(),
					toDirection(pagingParams.getOrder()),
					sortField);
		} else {
			var pagingFieldMapper = PagingFieldMapperFactory.create(defaultSortBy);
			var sortField = pagingFieldMapper.map(defaultSortBy);
			
			return PageRequest.of(
					pageNumber,
					pagingParams.getLimit(),
					toDirection(defaultOrder),
					sortField);
		}
	}
	
	private static Sort.Direction toDirection(Order order) {
		return switch (order) {
			case ASC -> Sort.Direction.ASC;
			case DESC -> Sort.Direction.DESC;
		};
	}
}
