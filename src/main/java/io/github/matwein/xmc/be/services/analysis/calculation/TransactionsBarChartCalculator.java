package io.github.matwein.xmc.be.services.analysis.calculation;

import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.services.analysis.controller.AssetTransactionsLoadingController;
import io.github.matwein.xmc.be.services.analysis.mapper.DtoAssetPointsToDtoChartSeriesMapper;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import io.github.matwein.xmc.common.utils.LocalDateUtil;
import io.github.matwein.xmc.fe.ui.MessageAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionsBarChartCalculator {
	private final DtoAssetPointsToDtoChartSeriesMapper dtoAssetPointsToDtoChartSeriesMapper;
	private final AssetTransactionsLoadingController assetTransactionsLoadingController;
	
	@Autowired
	public TransactionsBarChartCalculator(
			DtoAssetPointsToDtoChartSeriesMapper dtoAssetPointsToDtoChartSeriesMapper,
			AssetTransactionsLoadingController assetTransactionsLoadingController) {
		
		this.dtoAssetPointsToDtoChartSeriesMapper = dtoAssetPointsToDtoChartSeriesMapper;
		this.assetTransactionsLoadingController = assetTransactionsLoadingController;
	}
	
	public List<DtoChartSeries<String, Number>> calculate(Multimap<AssetType, Long> assetIds, LocalDate startDate, LocalDate endDate) {
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
		
		rp.setX(MessageAdapter.formatDateTime(LocalDateUtil.toLocalDateTime(point.getX())));
		rp.setY(point.getY());
		rp.setDescription(point.getDescription());
		
		return rp;
	}
}
