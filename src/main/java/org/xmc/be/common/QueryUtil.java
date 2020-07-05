package org.xmc.be.common;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.IPagingField;
import org.xmc.common.stubs.PagingParams;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class QueryUtil {
    @PersistenceContext
    private EntityManager entityManager;

    public <RESULT_TYPE, FIELD_ENUM_TYPE extends Enum<FIELD_ENUM_TYPE> & IPagingField> HibernateQuery<RESULT_TYPE>
    createPagedQuery(PagingParams<FIELD_ENUM_TYPE> pagingParams) {
        HibernateQuery<Object> query = new HibernateQuery<>(entityManager.unwrap(Session.class))
                .limit(pagingParams.getLimit())
                .offset(pagingParams.getOffset());

        if (pagingParams.getOrder() != null && pagingParams.getSortBy() != null) {
            query = query.orderBy(new OrderSpecifier(pagingParams.getOrder(), pagingParams.getSortBy().getExpression()));
        }

        return (HibernateQuery)query;
    }
}
