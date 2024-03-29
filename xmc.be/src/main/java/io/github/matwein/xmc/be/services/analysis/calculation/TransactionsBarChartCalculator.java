package io.github.matwein.xmc.be.services.analysis.calculation;

import io.github.matwein.xmc.be.common.LocalDateUtil;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.services.analysis.controller.AssetTransactionsLoadingController;
import io.github.matwein.xmc.be.services.analysis.mapper.DtoAssetPointsToDtoChartSeriesMapper;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionsBarChartCalculator {
	private final DtoAssetPointsToDtoChartSeriesMapper dtoAssetPointsToDtoChartSeriesMapper;
	private final AssetTransactionsLoadingController assetTransactionsLoadingController;
	private final LocalDateUtil localDateUtil;
	
	@Autowired
	public TransactionsBarChartCalculator(
			DtoAssetPointsToDtoChartSeriesMapper dtoAssetPointsToDtoChartSeriesMapper,
			AssetTransactionsLoadingController assetTransactionsLoadingController,
			LocalDateUtil localDateUtil) {
		
		this.dtoAssetPointsToDtoChartSeriesMapper = dtoAssetPointsToDtoChartSeriesMapper;
		this.assetTransactionsLoadingController = assetTransactionsLoadingController;
		this.localDateUtil = localDateUtil;
	}
	
	public List<DtoChartSeries<String, Number>> calculate(Map<AssetType, List<Long>> assetIds, LocalDate startDate, LocalDate endDate) {
		List<DtoAssetPoints> assetTransactions = assetTransactionsLoadingController.loadAssetTransactions(assetIds, startDate, endDate);
		List<DtoChartSeries<Number, Number>> dtoChartSeries = dtoAssetPointsToDtoChartSeriesMapper.mapAll(assetTransactions, false);
		
		return dtoChartSeries.stream()
				.map(this::mapChartSeries)
				.collect(Collectors.toList());
	}
	
	private DtoChartSeries<String, Number> mapChartSeries(DtoChartSeries<Number, Number> dto) {
		DtoChartSeries<String, Number> result = new DtoChartSeries<>();
		
		result.setColor(dto.getColor());
		result.setName(dto.getName());
		result.setPoints(dto.getPoints().stream()
				.map(this::mapChartPoint)
				.collect(Collectors.toList()));
		
		return result;
	}
	
	private DtoChartPoint<String, Number> mapChartPoint(DtoChartPoint<Number, Number> point) {
		DtoChartPoint<String, Number> rp = new DtoChartPoint<>();
		
		rp.setX(MessageAdapter.formatDateTime(localDateUtil.toLocalDateTime(point.getX())));
		rp.setY(point.getY());
		rp.setDescription(point.getDescription());
		
		return rp;
	}
}
