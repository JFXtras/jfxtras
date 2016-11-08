package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ParseDateTest
{
    @Test
    public void canParseDate1()
    {
        String value = "DTSTART;TZID=America/Los_Angeles:20160228T070000";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 2, 28, 7, 0), ZoneId.of("America/Los_Angeles")), t);
    }
    
    @Test
    public void canParseDate2()
    {
        String value = "TZID=Etc/GMT:20160306T080000Z";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 8, 0), ZoneId.of("Z")), t);
    }
    
    @Test
    public void canParseDate3()
    {
        String value = "20160306T080000Z";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 8, 0), ZoneId.of("Z")), t);
    }
    
    @Test
    public void canParseDate4()
    {
        String value = "DTSTART;VALUE=DATE:20160307";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(LocalDate.of(2016, 3, 7), t);
    }    
   

}


