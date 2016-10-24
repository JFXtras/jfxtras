package jfxtras.icalendarfx.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseDateTime;

/**
 * DUE
 * Date-Time Due (for local-date)
 * RFC 5545, 3.8.2.3, page 96
 * 
 * This property defines the date and time that a to-do is expected to be completed.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DUE;VALUE=DATE:19980704
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VTodo
 */
public class DateTimeDue extends PropBaseDateTime<Temporal, DateTimeDue>
{    
   public DateTimeDue(Temporal temporal)
    {
        super(temporal);
    }

//    public DateTimeDue(Class<T> clazz, CharSequence contentLine)
//    {
//        super(clazz, contentLine);
//    }
    
    public DateTimeDue(DateTimeDue source)
    {
        super(source);
    }
    
    public DateTimeDue()
    {
        super();
    }

    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static DateTimeDue parse(String value)
    {
        DateTimeDue property = new DateTimeDue();
        property.parseContent(value);
        return property;
    }
    
    /** Parse string with Temporal class explicitly provided as parameter */
    public static DateTimeDue parse(Class<? extends Temporal> clazz, String value)
    {
        DateTimeDue property = new DateTimeDue();
        property.parseContent(value);
        clazz.cast(property.getValue()); // class check
        return property;
    }
}
