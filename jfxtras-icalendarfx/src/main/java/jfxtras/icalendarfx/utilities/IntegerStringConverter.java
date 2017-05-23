package jfxtras.icalendarfx.utilities;

import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Integer}
 * (and int primitive) values.</p>
 * Copied JavaFx implementation for use in Java EE
 */
public class IntegerStringConverter implements StringConverter<Integer> {
    /** {@inheritDoc} */
    @Override public Integer fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Integer.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Integer value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return (Integer.toString(((Integer)value).intValue()));
    }
}
