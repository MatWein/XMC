package io.github.matwein.xmc.fe.common;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberUtils {
    private static final String VALID_NUMBER_CHARS = "0123456789.,-";

    public static double parseDoubleValue(String text) throws ParseException {
        if (StringUtils.isBlank(text)) {
            return 0.0;
        }

        text = StringUtils.trim(text);

        if (!StringUtils.containsOnly(text, VALID_NUMBER_CHARS)) {
            throw new ParseException("Not a valid number", 0);
        }

        return NumberFormat.getNumberInstance().parse(text).doubleValue();
    }
}
