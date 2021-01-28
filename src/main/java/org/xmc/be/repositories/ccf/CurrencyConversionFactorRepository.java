package org.xmc.be.repositories.ccf;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;

import static org.xmc.be.entities.depot.QCurrencyConversionFactor.currencyConversionFactor;

@Repository
public class CurrencyConversionFactorRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public CurrencyConversionFactorRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoCurrencyConversionFactor> loadOverview(PagingParams<CurrencyConversionFactorOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		BooleanExpression predicate = currencyConversionFactor.currency.likeIgnoreCase(filter);
		
		return queryUtil.createPagedQuery(pagingParams, CurrencyConversionFactorOverviewFields.INPUT_DATE, Order.DESC)
				.select(Projections.constructor(DtoCurrencyConversionFactor.class,
						currencyConversionFactor.id,
						currencyConversionFactor.inputDate,
						currencyConversionFactor.currency,
						currencyConversionFactor.factorToEur))
				.from(currencyConversionFactor)
				.where(predicate)
				.fetchResults();
	}
}
