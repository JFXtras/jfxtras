package jfxtras.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.icalendarfx.properties.component.misc.UnknownProperty;

/**
 * Interface for all Date and Date-Time properties
 * 
 * @author David Bal
 *
 * @param <T> - property Temporal value type (LocalDate, LocalDateTime or ZonedDateTime)
 * @see PropBaseDateTime
 * @see UnknownProperty
 */
public interface PropDateTime<T> extends Property<T>
{
    /*
     * default Time Zone methods are overridden by classes that require them
     */
    default TimeZoneIdentifierParameter getTimeZoneIdentifier()
    {
        return null;
    }
    default ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        return null;
    }
    default void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        // do nothing - not implemented
    }
}
