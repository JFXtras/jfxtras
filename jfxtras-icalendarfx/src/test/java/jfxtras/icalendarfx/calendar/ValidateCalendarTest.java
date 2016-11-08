package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;

public class ValidateCalendarTest
{
    @Test
    public void canFindErrorsInEmptyVCalendar()
    {
        VCalendar c = new VCalendar();
        assertEquals(2, c.errors().size());
    }
}
