package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.depot.QDepotItem;
import io.github.matwein.xmc.be.entities.depot.QStock;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotItemOverviewFieldsMapper implements IPagingFieldMapper<DepotItemOverviewFields> {
	@Override
	public Expression<?> map(DepotItemOverviewFields pagingField) {
		return switch (pagingField) {
			case ISIN -> QDepotItem.depotItem.isin;
			case WKN -> QStock.stock.wkn;
			case NAME -> QStock.stock.name;
			case AMOUNT -> QDepotItem.depotItem.amount;
			case COURSE -> QDepotItem.depotItem.course;
			case VALUE -> QDepotItem.depotItem.value;
			case CURRENCY -> QDepotItem.depotItem.currency;
			case CREATION_DATE -> QDepotItem.depotItem.creationDate;
		};
	}
}
