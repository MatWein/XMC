package org.xmc.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberUtils {
    public static double parseDoubleValue(String text) throws ParseException {
        if (StringUtils.isBlank(text)) {
            return 0.0;
        }

        text = StringUtils.trim(text);

        if (!StringUtils.containsOnly(text, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ',')) {
            throw new ParseException("Not a valid number", 0);
        }

        return NumberFormat.getNumberInstance().parse(text).doubleValue();
    }
}
