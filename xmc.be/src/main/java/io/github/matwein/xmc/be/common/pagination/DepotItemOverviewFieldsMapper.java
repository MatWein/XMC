package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.depot.QDepotItem;
import io.github.matwein.xmc.be.entities.depot.QStock;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DepotItemOverviewFieldsMapper implements IPagingFieldMapper<DepotItemOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotItemOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(DepotItemOverviewFields pagingField) {
		switch (pagingField) {
			case ISIN:
				return QDepotItem.depotItem.isin;
			case WKN:
				return QStock.stock.wkn;
			case NAME:
				return QStock.stock.name;
			case AMOUNT:
				return QDepotItem.depotItem.amount;
			case COURSE:
				return QDepotItem.depotItem.course;
			case VALUE:
				return QDepotItem.depotItem.value;
			case CURRENCY:
				return QDepotItem.depotItem.currency;
			case CREATION_DATE:
				return QDepotItem.depotItem.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
