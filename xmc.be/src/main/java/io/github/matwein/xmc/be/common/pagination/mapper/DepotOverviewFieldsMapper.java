package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotOverviewFieldsMapper implements IPagingFieldMapper<DepotOverviewFields> {
	@Override
	public String map(DepotOverviewFields pagingField) {
		return switch (pagingField) {
			case BANK_NAME -> "b.name";
			case BANK_BIC -> "b.bic";
			case BANK_BLZ -> "b.blz";
			case NAME -> "d.name";
			case NUMBER -> "d.number";
			case CREATION_DATE -> "d.creationDate";
			case LAST_SALDO -> "ld.saldo";
			case LAST_SALDO_DATE -> "ld.deliveryDate";
		};
	}
}
