package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.QBank;
import io.github.matwein.xmc.be.entities.depot.QDepot;
import io.github.matwein.xmc.be.entities.depot.QDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DepotOverviewFieldsMapper implements IPagingFieldMapper<DepotOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(DepotOverviewFields pagingField) {
		switch (pagingField) {
			case BANK_NAME:
				return QBank.bank.name;
			case BANK_BIC:
				return QBank.bank.bic;
			case BANK_BLZ:
				return QBank.bank.blz;
			case NAME:
				return QDepot.depot.name;
			case NUMBER:
				return QDepot.depot.number;
			case CREATION_DATE:
				return QDepot.depot.creationDate;
			case LAST_SALDO:
				return QDepotDelivery.depotDelivery.saldo;
			case LAST_SALDO_DATE:
				return QDepotDelivery.depotDelivery.deliveryDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
