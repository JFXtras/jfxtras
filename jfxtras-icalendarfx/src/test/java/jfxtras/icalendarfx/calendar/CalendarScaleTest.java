package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.CalendarScale.CalendarScaleType;

public class CalendarScaleTest
{
    @Test
    public void canParseCalendarScale()
    {
        CalendarScale property = new CalendarScale(CalendarScaleType.GREGORIAN);
        String expectedContent = "CALSCALE:GREGORIAN";
        assertEquals(expectedContent, property.toContent());
        CalendarScale property2 = CalendarScale.parse(expectedContent);
        assertEquals(property, property2);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canCatchInvlidCalendarScale()
    {
        CalendarScale.parse("INVALID");;
    }
}
