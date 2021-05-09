package io.github.matwein.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;

import java.util.List;
import java.util.Optional;

@Component
public class DuplicatedChartPointsReducer {
	public <X, Y> List<DtoChartPoint<X, Y>> reduce(List<DtoChartPoint<X, Y>> input) {
		List<DtoChartPoint<X, Y>> result = Lists.newArrayList(input);
		List<DtoChartPoint<X, Y>> pointsToRemove = Lists.newArrayListWithCapacity(input.size());
		
		for (int i = 0; i < input.size(); i++) {
			Optional<DtoChartPoint<X, Y>> pointBefore = findPoint(input, i - 1);
			DtoChartPoint<X, Y> point = input.get(i);
			Optional<DtoChartPoint<X, Y>> pointAfter = findPoint(input, i + 1);
			
			if (pointBefore.isEmpty() || pointAfter.isEmpty()) {
				continue;
			}
			
			if (pointBefore.get().getY().equals(point.getY()) && pointAfter.get().getY().equals(point.getY())) {
				pointsToRemove.add(point);
			}
		}
		
		result.removeAll(pointsToRemove);
		return result;
	}
	
	private <X, Y> Optional<DtoChartPoint<X, Y>> findPoint(List<DtoChartPoint<X, Y>> input, int index) {
		if (index < 0 || index >= input.size()) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(input.get(index));
	}
}
