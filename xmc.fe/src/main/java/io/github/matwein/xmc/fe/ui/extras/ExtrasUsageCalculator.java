package io.github.matwein.xmc.fe.ui.extras;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Set;

@Component
public class ExtrasUsageCalculator {
	private static final Set<Integer> WINTER_MONTHS = Set.of(
			Month.DECEMBER.getValue(),
			Month.JANUARY.getValue(),
			Month.FEBRUARY.getValue()
	);
	
	public boolean calculateUseWinterExtras() {
		return WINTER_MONTHS.contains(LocalDate.now().get(ChronoField.MONTH_OF_YEAR));
	}
}
