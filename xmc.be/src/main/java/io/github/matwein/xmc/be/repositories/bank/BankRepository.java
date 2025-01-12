package io.github.matwein.xmc.be.repositories.bank;

import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface BankRepository extends JpaRepository<Bank, Long> {
    default QueryResults<DtoBankOverview> loadOverview(PagingParams<BankOverviewFields> pagingParams) {
	    return fromPage(loadOverview$(
			    toPageable(pagingParams, BankOverviewFields.BANK_NAME, Order.ASC),
			    StringUtils.defaultString(pagingParams.getFilter())));
    }
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.bank.DtoBankOverview(
			b.id, b.name, b.bic, b.blz, l.rawData, b.creationDate
		)
		from Bank b
		left join b.logo l
		where b.deletionDate is null and (
			b.bic ilike '%' || :filter || '%'
			or b.blz ilike '%' || :filter || '%'
			or b.name ilike '%' || :filter || '%'
		)
	""")
	Page<DtoBankOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
