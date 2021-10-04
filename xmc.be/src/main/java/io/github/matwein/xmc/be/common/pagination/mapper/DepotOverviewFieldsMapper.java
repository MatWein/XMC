package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.QBank;
import io.github.matwein.xmc.be.entities.depot.QDepot;
import io.github.matwein.xmc.be.entities.depot.QDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotOverviewFieldsMapper implements IPagingFieldMapper<DepotOverviewFields> {
	@Override
	public Expression<?> map(DepotOverviewFields pagingField) {
		return switch (pagingField) {
			case BANK_NAME -> QBank.bank.name;
			case BANK_BIC -> QBank.bank.bic;
			case BANK_BLZ -> QBank.bank.blz;
			case NAME -> QDepot.depot.name;
			case NUMBER -> QDepot.depot.number;
			case CREATION_DATE -> QDepot.depot.creationDate;
			case LAST_SALDO -> QDepotDelivery.depotDelivery.saldo;
			case LAST_SALDO_DATE -> QDepotDelivery.depotDelivery.deliveryDate;
		};
	}
}
