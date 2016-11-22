package jfxtras.icalendarfx.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseDateTime;

/**
 * DTSTART
 * Date-Time Start (for local date only)
 * RFC 5545, 3.8.2.4, page 97
 * 
 * This property specifies when the calendar component begins.
 * 
 * Example:
 * DTSTART;VALUE=DATE:20160307
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VFreeBusy
 */
public class DateTimeStart extends PropBaseDateTime<Temporal, DateTimeStart>
{
   public DateTimeStart(Temporal temporal)
    {
        super(temporal);
    }

//    public DateTimeStart(Class<T> clazz, CharSequence contentLine)
//    {
//        super(clazz, contentLine);
//    }
    
    public DateTimeStart(DateTimeStart source)
    {
        super(source);
    }

    public DateTimeStart()
    {
        super();
    }

    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static DateTimeStart parse(String value)
    {
        DateTimeStart property = new DateTimeStart();
        property.parseContent(value);
        return property;
    }
    
    /** Parse string with Temporal class explicitly provided as parameter */
    public static DateTimeStart parse(Class<? extends Temporal> clazz, String value)
    {
        DateTimeStart property = new DateTimeStart();
        property.parseContent(value);
        clazz.cast(property.getValue()); // class check
        return property;
    }
}
