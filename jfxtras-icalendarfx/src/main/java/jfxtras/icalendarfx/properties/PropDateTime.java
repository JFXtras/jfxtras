package jfxtras.icalendarfx.properties;

import jfxtras.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.icalendarfx.properties.PropBaseDateTime;
import jfxtras.icalendarfx.properties.VProperty;
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
public interface PropDateTime<T> extends VProperty<T>
{
    /*
     * default Time Zone methods are overridden by classes that require them
     */
    TimeZoneIdentifierParameter getTimeZoneIdentifier();
    void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier);
}
