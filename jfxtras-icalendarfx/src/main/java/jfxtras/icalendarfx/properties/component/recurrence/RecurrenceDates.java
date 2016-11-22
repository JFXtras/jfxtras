package jfxtras.icalendarfx.properties.component.recurrence;

import java.time.temporal.Temporal;

import javafx.collections.ObservableSet;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;

/**
 * RDATE
 * Recurrence Date-Times
 * RFC 5545 iCalendar 3.8.5.2, page 120.
 * 
 * This property defines the list of DATE-TIME values for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class RecurrenceDates extends PropertyBaseRecurrence<RecurrenceDates>
{       
    @SuppressWarnings("unchecked")
    public RecurrenceDates(Temporal...temporals)
    {
        super(temporals);
    }
    
    public RecurrenceDates(RecurrenceDates source)
    {
        super(source);
    }
    
    public RecurrenceDates(ObservableSet<Temporal> value)
    {
        super(value);
    }
    
    public RecurrenceDates()
    {
        super();
    }

    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static RecurrenceDates parse(String value)
    {
        RecurrenceDates property = new RecurrenceDates();
        property.parseContent(value);
        return property;
    }
    
    /** Parse string with Temporal class Exceptions provided as parameter */
    public static RecurrenceDates parse(Class<? extends Temporal> clazz, String value)
    {
        RecurrenceDates property = new RecurrenceDates();
        property.parseContent(value);
        clazz.cast(property.getValue().iterator().next()); // class check
        return property;
    }
}
