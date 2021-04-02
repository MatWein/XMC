package org.xmc.be.services.analysis.calculation;

import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.controller.AssetDeliveriesLoadingController;
import org.xmc.be.services.analysis.mapper.DtoAssetPointsToDtoChartSeriesMapper;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetPoints;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;

import java.time.LocalDate;
import java.util.List;

@Component
public class AbsoluteAssetValueLineChartCalculator {
	private final AssetDeliveriesLoadingController assetDeliveriesLoadingController;
	private final DtoAssetPointsToDtoChartSeriesMapper dtoAssetPointsToDtoChartSeriesMapper;
	
	@Autowired
	public AbsoluteAssetValueLineChartCalculator(
			AssetDeliveriesLoadingController assetDeliveriesLoadingController,
			DtoAssetPointsToDtoChartSeriesMapper dtoAssetPointsToDtoChartSeriesMapper) {
		
		this.assetDeliveriesLoadingController = assetDeliveriesLoadingController;
		this.dtoAssetPointsToDtoChartSeriesMapper = dtoAssetPointsToDtoChartSeriesMapper;
	}
	
	public List<DtoChartSeries<Number, Number>> calculate(Multimap<AssetType, Long> assetIds, LocalDate startDate, LocalDate endDate) {
		List<DtoAssetPoints> assetDeliveries = assetDeliveriesLoadingController.loadAssetDeliveries(assetIds, startDate, endDate);
		return dtoAssetPointsToDtoChartSeriesMapper.mapAll(assetDeliveries);
	}
}
