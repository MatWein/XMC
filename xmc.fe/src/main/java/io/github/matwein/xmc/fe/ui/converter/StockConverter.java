package io.github.matwein.xmc.fe.ui.converter;

import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StockConverter implements Function<DtoMinimalStock, String> {
	private static final String DELIMITER = " / ";
	
	@Override
	public String apply(DtoMinimalStock dtoMinimalStock) {
		String[] stockMasterData = new String[] {
				dtoMinimalStock.getIsin(),
				dtoMinimalStock.getWkn(),
				dtoMinimalStock.getName()
		};
		
		return Arrays.stream(stockMasterData)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(DELIMITER));
	}
}
