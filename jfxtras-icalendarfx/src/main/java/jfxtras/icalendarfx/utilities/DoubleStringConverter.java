package jfxtras.icalendarfx.utilities;

import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * <p>{@link StringConverter} implementation for {@link Double}
 * (and double primitive) values.</p>
 * Copied JavaFx implementation for use in Java EE
 */
public class DoubleStringConverter implements StringConverter<Double> {
    /** {@inheritDoc} */
    @Override public Double fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Double.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return Double.toString(value.doubleValue());
    }
}
