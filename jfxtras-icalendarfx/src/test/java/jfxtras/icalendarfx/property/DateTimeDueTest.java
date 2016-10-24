package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.time.DateTimeDue;

public class DateTimeDueTest
{
    @Test
    public void canParseDateTimeDue1()
    {
        DateTimeDue property = DateTimeDue.parse(LocalDateTime.class, "20160322T174422");
        String expectedContentLine = "DUE:20160322T174422";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), property.getValue());
    }
    
    @Test
    public void canParseDateTimeDue2()
    {
        DateTimeDue property = DateTimeDue.parse(LocalDate.class, "20160322");
        String expectedContentLine = "DUE;VALUE=DATE:20160322";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), property.getValue());
    }
    
    @Test
    public void canParseDateTimeDue3()
    {
        DateTimeDue property = new DateTimeDue(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        String expectedContentLine = "DUE;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), property.getValue());
    }
}
