package com.waylayplugins.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * Tries to parse an object to double
     *
     * @param obj the obect to parse
     * @return the double value or null if the input was null
     * @throws java.lang.NumberFormatException if parsing to double fails
     */
    public static double getDouble(Object obj) throws NumberFormatException {
        if (obj == null) {
            throw new NumberFormatException("Can't parse null to double");
        } else if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        } else {
            return Double.parseDouble(String.valueOf(obj));
        }
    }

}