package jfxtras.icalendarfx.properties.component.recurrence;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

/**
 * RRULE
 * Recurrence Rule
 * RFC 5545 iCalendar 3.8.5.3, page 122.
 * 
 * This property defines a rule or repeating pattern for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * Produces a stream of start date/times after applying all modification rules.
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class RecurrenceRule extends PropertyBase<RecurrenceRule2, RecurrenceRule>
{
    public RecurrenceRule(RecurrenceRule2 value)
    {
        super(value);
    }
    
    public RecurrenceRule()
    {
        super();
    }

    public RecurrenceRule(RecurrenceRule source)
    {
        super(source);
    }

    public static RecurrenceRule parse(String propertyContent)
    {
        RecurrenceRule property = new RecurrenceRule();
        property.parseContent(propertyContent);
        return property;
    }
    
    @Override
    protected RecurrenceRule2 copyValue(RecurrenceRule2 source)
    {
        return new RecurrenceRule2(source);
    }
}
