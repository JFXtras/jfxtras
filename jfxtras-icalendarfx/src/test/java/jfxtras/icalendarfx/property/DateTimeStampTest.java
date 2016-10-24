package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;

public class DateTimeStampTest
{
    @Test
    public void canParseDateTimeStamp()
    {
        String expectedContentLine = "DTSTAMP:19971210T080000Z";
        DateTimeStamp property = DateTimeStamp.parse(expectedContentLine);
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(1997, 12, 10, 8, 0), ZoneId.of("Z")), property.getValue());
    }
    
    @Test (expected = ClassCastException.class)
    public void canCatchWrongDateTimeStamp()
    {
        String expectedContentLine = "DTSTAMP:19971210";
        DateTimeStamp.parse(expectedContentLine);
    }

}
