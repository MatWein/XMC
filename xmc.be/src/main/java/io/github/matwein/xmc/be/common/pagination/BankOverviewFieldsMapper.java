package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.QBank;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BankOverviewFieldsMapper implements IPagingFieldMapper<BankOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BankOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(BankOverviewFields pagingField) {
		switch (pagingField) {
			case BANK_NAME:
				return QBank.bank.name;
			case BANK_BIC:
				return QBank.bank.bic;
			case BANK_BLZ:
				return QBank.bank.blz;
			case CREATION_DATE:
				return QBank.bank.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
