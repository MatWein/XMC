package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.QBank;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class BankOverviewFieldsMapper implements IPagingFieldMapper<BankOverviewFields> {
	@Override
	public Expression<?> map(BankOverviewFields pagingField) {
		return switch (pagingField) {
			case BANK_NAME -> QBank.bank.name;
			case BANK_BIC -> QBank.bank.bic;
			case BANK_BLZ -> QBank.bank.blz;
			case CREATION_DATE -> QBank.bank.creationDate;
		};
	}
}
