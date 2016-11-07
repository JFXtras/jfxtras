package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.CalendarUser.CalendarUserType;

public class CalendarUserTest
{
    @Test // tests String as value
    public void canParseCalendarUser()
    {
        CalendarUser parameter = CalendarUser.parse("INDIVIDUAL");
        String expectedContent = "CUTYPE=INDIVIDUAL";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests String as value
    public void canParseNonStandardCalendarUser()
    {
        CalendarUser parameter = CalendarUser.parse("X-CLAN");
        String expectedContent = "CUTYPE=X-CLAN";
        assertEquals(expectedContent, parameter.toContent());
        assertEquals(CalendarUserType.UNKNOWN, parameter.getValue());
    }
}
