package io.github.matwein.xmc.common.stubs.depot;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.common.stubs.IPagingField;

import static io.github.matwein.xmc.be.entities.QBank.bank;
import static io.github.matwein.xmc.be.entities.depot.QDepot.depot;
import static io.github.matwein.xmc.be.entities.depot.QDepotDelivery.depotDelivery;

public enum DepotOverviewFields implements IPagingField {
	BANK_NAME(bank.name),
	BANK_BIC(bank.bic),
	BANK_BLZ(bank.blz),
	
	NAME(depot.name),
	NUMBER(depot.number),
	CREATION_DATE(depot.creationDate),
	LAST_SALDO(depotDelivery.saldo),
	LAST_SALDO_DATE(depotDelivery.deliveryDate);
	
	private final Expression<?> expression;
	
	DepotOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
