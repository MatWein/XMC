package io.github.matwein.xmc.be.common.pagination;

import io.github.matwein.xmc.common.stubs.IPagingField;

public interface IPagingFieldMapper<T extends IPagingField> {
	String map(T pagingField);
}
