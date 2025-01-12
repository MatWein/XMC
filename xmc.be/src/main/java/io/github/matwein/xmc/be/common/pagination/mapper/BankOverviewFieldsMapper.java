package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class BankOverviewFieldsMapper implements IPagingFieldMapper<BankOverviewFields> {
	@Override
	public String map(BankOverviewFields pagingField) {
		return switch (pagingField) {
			case BANK_NAME -> "b.name";
			case BANK_BIC -> "b.bic";
			case BANK_BLZ -> "b.blz";
			case CREATION_DATE -> "b.creationDate";
		};
	}
}
