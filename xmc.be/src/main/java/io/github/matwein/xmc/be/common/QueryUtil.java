package io.github.matwein.xmc.be.common;

import com.querydsl.core.types.*;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.github.matwein.xmc.be.common.mapper.OrderMapper;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.common.pagination.PagingFieldMapperFactory;
import io.github.matwein.xmc.common.stubs.IPagingField;
import io.github.matwein.xmc.common.stubs.PagingParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.querydsl.core.types.OrderSpecifier.NullHandling.NullsLast;

@Component
public class QueryUtil {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private PagingFieldMapperFactory pagingFieldMapperFactory;
	
	@Autowired
	private OrderMapper orderMapper;

    public <RESULT_TYPE, FIELD_ENUM_TYPE extends Enum<FIELD_ENUM_TYPE> & IPagingField> HibernateQuery<RESULT_TYPE>
    createPagedQuery(PagingParams<FIELD_ENUM_TYPE> pagingParams, FIELD_ENUM_TYPE defaultSortBy, Order defaultOrder) {
        HibernateQuery<RESULT_TYPE> query = (HibernateQuery)createQuery()
                .limit(pagingParams.getLimit())
                .offset(pagingParams.getOffset());
	
	    IPagingFieldMapper pagingFieldMapper = pagingFieldMapperFactory.create(defaultSortBy);
	
	    if (pagingParams.getOrder() != null && pagingParams.getSortBy() != null) {
            Expression<?> expression = createOrderByExpression(pagingFieldMapper.map(pagingParams.getSortBy()));
            query = query.orderBy(new OrderSpecifier(orderMapper.map(pagingParams.getOrder()), expression, NullsLast));
        } else {
            Expression<?> expression = createOrderByExpression(pagingFieldMapper.map(defaultSortBy));
            query = query.orderBy(new OrderSpecifier(defaultOrder, expression, NullsLast));
        }

        return query;
    }

    private Expression<?> createOrderByExpression(Expression<?> expression) {
        boolean isStringExpression = String.class.isAssignableFrom(expression.getType());
        if (isStringExpression) {
            return ExpressionUtils.toLower((Expression)expression);
        } else {
            return expression;
        }
    }
	
	public <RESULT_TYPE> HibernateQuery<RESULT_TYPE> createQuery() {
        return new HibernateQuery<>(getSession());
    }

    public HibernateDeleteClause createDeleteClause(EntityPath<?> entityToDelete) {
        return new HibernateDeleteClause(getSession(), entityToDelete);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
