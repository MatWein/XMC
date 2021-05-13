package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.common.stubs.IPagingField;

public interface IPagingFieldMapper<T extends IPagingField> {
	Expression<?> map(T pagingField);
}
