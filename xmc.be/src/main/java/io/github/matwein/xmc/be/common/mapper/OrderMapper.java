package io.github.matwein.xmc.be.common.mapper;

import com.querydsl.core.types.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderMapper.class);
	
	public Order map(io.github.matwein.xmc.common.stubs.Order order) {
		if (order == null) {
			return null;
		}
		
		switch (order) {
			case ASC:
				return Order.ASC;
			case DESC:
				return Order.DESC;
			default:
				String message = String.format("Could not map unknown enum value '%s' of type '%s' to order.", order, order.getClass().getName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
