package io.github.matwein.xmc.be.repositories.cashaccount;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface CashAccountRepository extends JpaRepository<CashAccount, Long> {
    default QueryResults<DtoCashAccountOverview> loadOverview(PagingParams<CashAccountOverviewFields> pagingParams) {
	    return fromPage(loadOverview$(
			    toPageable(pagingParams, CashAccountOverviewFields.NAME, Order.ASC),
			    StringUtils.defaultString(pagingParams.getFilter())));
    }
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview(
			ca.id, ca.iban, ca.number, ca.name, ca.currency, ca.color, ca.creationDate, ca.lastSaldo, ca.lastSaldoDate,
			b.id, b.name, b.bic, b.blz, l.rawData
		)
		from CashAccount ca
		inner join ca.bank b
		left join b.logo l
		where ca.deletionDate is null and (
			ca.name ilike '%' || :filter || '%'
			or ca.number ilike '%' || :filter || '%'
			or ca.iban ilike '%' || :filter || '%'
			or b.bic ilike '%' || :filter || '%'
			or b.blz ilike '%' || :filter || '%'
			or b.name ilike '%' || :filter || '%'
		)
	""")
	Page<DtoCashAccountOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
