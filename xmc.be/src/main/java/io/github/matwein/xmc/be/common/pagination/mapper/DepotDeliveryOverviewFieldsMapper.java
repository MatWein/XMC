package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.depot.QDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotDeliveryOverviewFieldsMapper implements IPagingFieldMapper<DepotDeliveryOverviewFields> {
	@Override
	public Expression<?> map(DepotDeliveryOverviewFields pagingField) {
		return switch (pagingField) {
			case DELIVERY_DATE -> QDepotDelivery.depotDelivery.deliveryDate;
			case SALDO -> QDepotDelivery.depotDelivery.saldo;
			case CREATION_DATE -> QDepotDelivery.depotDelivery.creationDate;
		};
	}
}
