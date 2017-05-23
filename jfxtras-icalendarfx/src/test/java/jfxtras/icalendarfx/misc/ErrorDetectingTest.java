package jfxtras.icalendarfx.misc;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

public class ErrorDetectingTest
{
    @Test
    public void canCatchListErrorProperty()
    {
        ExceptionDates e = ExceptionDates.parse("20160228T093000");
        e.getValue().add(LocalDateTime.of(2016, 4, 25, 1, 0));
        e.getValue().add(LocalDate.of(2016, 4, 25));
        assertEquals(1, e.errors().size());
        String expectedMessage = "Recurrences DateTimeType \"" +
        		DateTimeType.DATE + "\" doesn't match previous recurrences DateTimeType \"" +
        		DateTimeType.DATE_WITH_LOCAL_TIME + "\"";
        assertEquals(expectedMessage, e.errors().get(0));
    }
}
