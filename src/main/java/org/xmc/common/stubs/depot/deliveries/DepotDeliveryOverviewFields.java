package org.xmc.common.stubs.depot.deliveries;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QDepotDelivery.depotDelivery;

public enum DepotDeliveryOverviewFields implements IPagingField {
	DELIVERY_DATE(depotDelivery.deliveryDate),
	SALDO(depotDelivery.saldo),
	CREATION_DATE(depotDelivery.creationDate),
	;
	
	private final Expression<?> expression;
	
	DepotDeliveryOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
