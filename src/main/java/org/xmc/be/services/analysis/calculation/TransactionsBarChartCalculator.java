package org.xmc.be.services.analysis.calculation;

import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.controller.AssetTransactionsLoadingController;
import org.xmc.be.services.analysis.mapper.DtoAssetPointsToDtoChartSeriesMapper;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetPoints;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.LocalDateUtil;
import org.xmc.fe.ui.MessageAdapter;

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
		List<DtoAssetPoints> assetTransactions = assetTransactionsLoadingController.loadAssetDeliveries(assetIds, startDate, endDate);
		List<DtoChartSeries<Number, Number>> dtoChartSeries = dtoAssetPointsToDtoChartSeriesMapper.mapAll(assetTransactions);
		
		return dtoChartSeries.stream()
				.map(dto -> {
					DtoChartSeries<String, Number> result = new DtoChartSeries<>();
					
					result.setColor(dto.getColor());
					result.setName(dto.getName());
					result.setPoints(dto.getPoints().stream()
							.map(point -> {
								DtoChartPoint<String, Number> rp = new DtoChartPoint<>();
								
								rp.setX(MessageAdapter.formatDate(LocalDateUtil.toLocalDate(point.getX())));
								rp.setY(point.getY());
								
								return rp;
							})
							.collect(Collectors.toList()));
					
					return result;
				})
				.collect(Collectors.toList());
	}
}
