package io.github.matwein.xmc.common;

import java.time.LocalTime;

public interface CommonConstants {
	LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);
	
	int MAX_TEXT_LENGTH = 255;
}
