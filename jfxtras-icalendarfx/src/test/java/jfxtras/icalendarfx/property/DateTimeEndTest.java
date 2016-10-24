package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;

public class DateTimeEndTest
{
    @Test
    public void canParseDateTimeEnd1()
    {
        DateTimeEnd property = DateTimeEnd.parse("20160322T174422");
        String expectedContentLine = "DTEND:20160322T174422";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), property.getValue());
    }
    
    @Test
    public void canParseDateTimeEnd2()
    {
        DateTimeEnd property = DateTimeEnd.parse("20160322");
        String expectedContentLine = "DTEND;VALUE=DATE:20160322";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), property.getValue());
    }
    
    @Test
    public void canParseDateTimeEnd3()
    {
        DateTimeEnd property = new DateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        String expectedContentLine = "DTEND;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), property.getValue());
    }
}
