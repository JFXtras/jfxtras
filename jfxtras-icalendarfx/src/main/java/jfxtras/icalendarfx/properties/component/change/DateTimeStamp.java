package jfxtras.icalendarfx.properties.component.change;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseUTC;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * DTSTAMP
 * Date-Time Stamp
 * RFC 5545, 3.8.7.2, page 137
 * 
 * In the case of an iCalendar object that specifies a
 * "METHOD" property, this property specifies the date and time that
 * the instance of the iCalendar object was created.  In the case of
 * an iCalendar object that doesn't specify a "METHOD" property, this
 * property specifies the date and time that the information
 * associated with the calendar component was last revised in the
 * calendar store.
 * 
 * In the case of an iCalendar object that doesn't specify a "METHOD"
 * property, this property is equivalent to the "LAST-MODIFIED" property.
 * 
 * The value MUST be specified as a date with UTC time.
 * 
 * Example:
 * DTSTAMP:19971210T080000Z
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class DateTimeStamp extends PropBaseUTC<DateTimeStamp>
{    
    public DateTimeStamp(ZonedDateTime temporal)
    {
        super(temporal);
    }
    
    public DateTimeStamp(DateTimeStamp source)
    {
        super(source);
    }
    
    public static DateTimeStamp parse(String value)
    {
        Temporal t = DateTimeUtilities.temporalFromString(value);
        return new DateTimeStamp((ZonedDateTime) t);
    }
}
