package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.depot.QDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DepotDeliveryOverviewFieldsMapper implements IPagingFieldMapper<DepotDeliveryOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(DepotDeliveryOverviewFields pagingField) {
		switch (pagingField) {
			case DELIVERY_DATE:
				return QDepotDelivery.depotDelivery.deliveryDate;
			case SALDO:
				return QDepotDelivery.depotDelivery.saldo;
			case CREATION_DATE:
				return QDepotDelivery.depotDelivery.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
