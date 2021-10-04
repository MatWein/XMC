package io.github.matwein.xmc.be.common.mapper;

import com.querydsl.core.types.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
	public Order map(io.github.matwein.xmc.common.stubs.Order order) {
		if (order == null) {
			return null;
		}
		
		return switch (order) {
			case ASC -> Order.ASC;
			case DESC -> Order.DESC;
		};
	}
}
