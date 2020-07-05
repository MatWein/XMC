package org.xmc.common.stubs;

import com.querydsl.core.types.Expression;

public interface IPagingField {
    Expression<?> getExpression();
}
