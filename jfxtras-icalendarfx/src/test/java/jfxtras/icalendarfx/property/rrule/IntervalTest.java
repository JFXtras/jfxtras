package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class IntervalTest
{
    @Test
    public void canRemoveInterval()
    {
        RecurrenceRule2 r = RecurrenceRule2.parse("FREQ=DAILY;INTERVAL=2");
        r.setInterval((Interval) null);
        String expectedContent = "FREQ=DAILY";
        RecurrenceRule2 expectedVEvent = RecurrenceRule2.parse(expectedContent);
        assertEquals(expectedVEvent, r);
        assertEquals(expectedContent, r.toContent());
    }
    
    @Test
    public void canChangeInterval()
    {
        RecurrenceRule2 r = RecurrenceRule2.parse("FREQ=DAILY;INTERVAL=2");
        r.setInterval(new Interval(3));
        String expectedContent = "FREQ=DAILY;INTERVAL=3";
        assertEquals(expectedContent, r.toContent());
    }
}
