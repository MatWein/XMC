package io.github.matwein.xmc.config;

import io.github.matwein.xmc.common.stubs.Money;
import io.github.matwein.xmc.common.stubs.Percentage;
import scalc.SCalcBuilder;
import scalc.interfaces.INumberConverter;

import java.math.BigDecimal;

public class SCalcConfig {
	public static void init() {
		SCalcBuilder.registerGlobalConverter(Money.class, new MoneyConverter());
		SCalcBuilder.registerGlobalConverter(Percentage.class, new PercentageConverter());
	}
	
	private static class MoneyConverter implements INumberConverter<Money> {
		@Override
		public BigDecimal toBigDecimal(Money money) {
			return money.getValue();
		}
		
		@Override
		public Money fromBigDecimal(BigDecimal bigDecimal) {
			Money money = new Money();
			money.setValue(bigDecimal);
			return money;
		}
	}
	
	private static class PercentageConverter implements INumberConverter<Percentage> {
		@Override
		public BigDecimal toBigDecimal(Percentage percentage) {
			return percentage.getValue();
		}
		
		@Override
		public Percentage fromBigDecimal(BigDecimal bigDecimal) {
			return new Percentage(bigDecimal);
		}
	}
}
