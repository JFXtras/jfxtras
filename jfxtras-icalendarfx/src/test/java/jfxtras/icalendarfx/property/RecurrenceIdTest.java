package jfxtras.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.Range.RangeType;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;

public class RecurrenceIdTest
{
    @Test
    public void canParseRecurrenceId1()
    {
        RecurrenceId property = RecurrenceId.parse(LocalDateTime.class, "20160322T174422");
        String expectedContentLine = "RECURRENCE-ID:20160322T174422";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), property.getValue());
    }
    
    @Test
    public void canParseRecurrenceId2()
    {
        RecurrenceId property = RecurrenceId.parse(LocalDate.class, "20160322");
        String expectedContentLine = "RECURRENCE-ID;VALUE=DATE:20160322";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), property.getValue());
    }
    
    @Test
    public void canParseRecurrenceId3()
    {
        RecurrenceId property = new RecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        String expectedContentLine = "RECURRENCE-ID;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), property.getValue());
    }
    
    @Test
    public void canParseRecurrenceId4()
    {
        RecurrenceId property = new RecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z")))
                .withRange(RangeType.THIS_AND_FUTURE);
        String expectedContentLine = "RECURRENCE-ID;RANGE=THISANDFUTURE:20160306T043000Z";
        String madeContentLine = property.toContent();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z")), property.getValue());
    }
}
