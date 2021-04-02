package org.xmc.be.services.analysis.controller;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.DtoAssetPoints;

import java.time.LocalDate;
import java.util.List;

@Component
public class DepotTransactionLoadingController {
	public List<DtoAssetPoints> loadTransactionsForDepots(List<Long> sortedAssetIds, LocalDate startDate, LocalDate endDate) {
		return null;
	}
}
