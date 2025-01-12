package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotDeliveryOverviewFieldsMapper implements IPagingFieldMapper<DepotDeliveryOverviewFields> {
	@Override
	public String map(DepotDeliveryOverviewFields pagingField) {
		return switch (pagingField) {
			case DELIVERY_DATE -> "dd.deliveryDate";
			case SALDO -> "dd.saldo";
			case CREATION_DATE -> "dd.creationDate";
		};
	}
}
