package io.github.matwein.xmc.fe.ui.converter;

import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;

import java.util.function.Function;

@Component
public class StockConverter implements Function<DtoMinimalStock, String> {
	@Override
	public String apply(DtoMinimalStock dtoMinimalStock) {
		return Joiner.on(" / ").skipNulls().join(
				dtoMinimalStock.getIsin(),
				dtoMinimalStock.getWkn(),
				dtoMinimalStock.getName());
	}
}
