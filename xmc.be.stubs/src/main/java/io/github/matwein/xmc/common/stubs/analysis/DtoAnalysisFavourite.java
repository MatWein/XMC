package io.github.matwein.xmc.common.stubs.analysis;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DtoAnalysisFavourite implements Serializable {
	private String name;
	private AnalysisType analysisType;
	private TimeRange timeRange;
	
	private Map<AssetType, List<Long>> assetIds;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public AnalysisType getAnalysisType() {
		return analysisType;
	}
	
	public void setAnalysisType(AnalysisType analysisType) {
		this.analysisType = analysisType;
	}
	
	public Map<AssetType, List<Long>> getAssetIds() {
		return assetIds;
	}
	
	public void setAssetIds(Map<AssetType, List<Long>> assetIds) {
		this.assetIds = assetIds;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public TimeRange getTimeRange() {
		return timeRange;
	}
	
	public void setTimeRange(TimeRange timeRange) {
		this.timeRange = timeRange;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", name)
				.append("analysisType", analysisType)
				.append("timeRange", timeRange)
				.append("assetIds", assetIds)
				.append("startDate", startDate)
				.append("endDate", endDate)
				.toString();
	}
}
