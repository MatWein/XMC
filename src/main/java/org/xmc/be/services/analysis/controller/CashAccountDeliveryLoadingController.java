package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.DtoAssetDeliveries;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class CashAccountDeliveryLoadingController {
	public List<DtoAssetDeliveries> loadDeliveriesForCashAccounts(Collection<Long> cashAccountIds, LocalDate startDate, LocalDate endDate) {
		return Lists.newArrayList();
	}
}
