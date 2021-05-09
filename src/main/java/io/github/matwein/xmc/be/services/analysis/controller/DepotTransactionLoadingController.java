package io.github.matwein.xmc.be.services.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.utils.LocalDateUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class DepotTransactionLoadingController {
	private final DepotJpaRepository depotJpaRepository;
	private final DepotTransactionJpaRepository depotTransactionJpaRepository;
	
	@Autowired
	public DepotTransactionLoadingController(
			DepotJpaRepository depotJpaRepository,
			DepotTransactionJpaRepository depotTransactionJpaRepository) {
		
		this.depotJpaRepository = depotJpaRepository;
		this.depotTransactionJpaRepository = depotTransactionJpaRepository;
	}
	
	public List<DtoAssetPoints> loadTransactionsForDepots(List<Long> sortedAssetIds, LocalDate startDate, LocalDate endDate) {
		return sortedAssetIds.stream()
				.map(depotId -> loadDeliveriesForDepot(depotId, startDate, endDate))
				.collect(Collectors.toList());
	}
	
	private DtoAssetPoints loadDeliveriesForDepot(long depotId, LocalDate startDate, LocalDate endDate) {
		DtoAssetPoints result = new DtoAssetPoints();
		
		result.setAssetId(depotId);
		result.setAssetType(AssetType.DEPOT);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		result.setAssetName(depot.getName());
		result.setAssetColor(depot.getColor());
		
		result.setPoints(loadTransactionPoints(depot, startDate, endDate));
		
		return result;
	}
	
	private List<DtoChartPoint<Number, Number>> loadTransactionPoints(Depot depot, LocalDate startDate, LocalDate endDate) {
		List<DepotTransaction> transactions = depotTransactionJpaRepository.findByDepotAndDeletionDateIsNull(depot);
		
		AtomicInteger counter = new AtomicInteger(1);
		
		return transactions.stream()
				.filter(transaction -> transaction.getValutaDate().compareTo(startDate) >= 0)
				.filter(transaction -> transaction.getValutaDate().compareTo(endDate) <= 0)
				.map(transaction -> new DtoChartPoint<Number, Number>(
						LocalDateUtil.toMillis(transaction.getValutaDate().atTime(transaction.getCreationDate().toLocalTime()).plusSeconds(counter.getAndIncrement())),
						transaction.getValue().doubleValue(),
						transaction.getDescription()))
				.sorted(Comparator.comparing(point -> point.getX().doubleValue()))
				.collect(Collectors.toList());
	}
}
