package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.change.DateTimeCreated;

public class DateTimeCreatedTest
{
    @Test
    public void canParseDateTimeCreated()
    {
        String expectedContentLine = "CREATED:19960329T133000Z";
        DateTimeCreated property = DateTimeCreated.parse(expectedContentLine);
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(1996, 3, 29, 13, 30), ZoneId.of("Z")), property.getValue());
    }
    
    @Test (expected=ClassCastException.class)
    public void canCatchWrongDateTimeFormat1()
    {
        DateTimeCreated.parse("CREATED:19960329T133000");
    }
    
    @Test (expected=ClassCastException.class)
    public void canCatchWrongDateTimeFormat2()
    {
        DateTimeCreated.parse("CREATED:19960329");
    }
    
    @Test (expected=DateTimeException.class)
    public void canCatchWrongDateTimeFormat3()
    {
        String expectedContentLine = "CREATED:19960329T133000Z";
        DateTimeCreated property = DateTimeCreated.parse(expectedContentLine);
        property.setValue(ZonedDateTime.of(LocalDateTime.of(1996, 3, 29, 13, 30), ZoneId.of("America/Los_Angeles")));
    }
}
