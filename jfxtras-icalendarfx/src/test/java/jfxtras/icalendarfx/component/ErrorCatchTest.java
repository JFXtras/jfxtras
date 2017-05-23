package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

public class ErrorCatchTest
{
    @Test
    public void canCatchInvalidExDates()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withExceptionDates(new ExceptionDates(LocalDateTime.of(2016, 2, 12, 12, 30))) // invalid - stop processing
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                ;
        String error = "EXDATE: DateTimeType " + DateTimeType.DATE_WITH_LOCAL_TIME + " doesn't match previous recurrence's DateTimeType " + DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE;
        boolean isErrorPresent = e.errors()
        	.stream()
        	.anyMatch(r -> r.equals(error));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canCatchParseInvalidExDates()
    {
            String content = "BEGIN:VEVENT" + System.lineSeparator() +
            "DTSTART;TZID=America/Los_Angeles:20160207T123000" + System.lineSeparator() +
            "EXDATE;TZID=America/Los_Angeles:20160210T123000" + System.lineSeparator() +
            "EXDATE:20160212T123000" + System.lineSeparator() + // invalid - ignore
            "EXDATE;TZID=America/Los_Angeles:20160209T123000" + System.lineSeparator() +
            "END:VEVENT";
            VEvent v = VEvent.parse(content);
            String expectedError = "EXDATE: DateTimeType DATE_WITH_LOCAL_TIME doesn't match previous recurrence's DateTimeType DATE_WITH_LOCAL_TIME_AND_TIME_ZONE";
            boolean isErrorPresent = v.errors().stream().anyMatch(s -> s.equals(expectedError));
            assertTrue(isErrorPresent);
//            VEvent expected = new VEvent()
//                    .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
//                    .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))))
//                    .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
//                    ;
//            assertEquals(expected, v);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canIgnoreDuplicateProperty()
    {
            String content = "BEGIN:VEVENT" + System.lineSeparator() +
            "SUMMARY:#1" + System.lineSeparator() +
            "DTSTART;TZID=America/Los_Angeles:20160207T123000" + System.lineSeparator() +
            "SUMMARY:#2" + System.lineSeparator() +
            "END:VEVENT";
            VEvent v = VEvent.parse(content);
            
            VEvent expected = new VEvent()
                    .withSummary("#1")
                    .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                    ;
            assertEquals(expected, v);
    }
    
    @Test
    public void canCatchParseWithBadLine()
    {
            String content = "BEGIN:VEVENT" + System.lineSeparator() +
            "SUMMARY:#1" + System.lineSeparator() +
            "X-CUSTOM-PROP:THE DATA" + System.lineSeparator() +
            "IGNORE THIS LINE" + System.lineSeparator() +
            "END:VEVENT";
            VEvent v = VEvent.parse(content);
            
            VEvent expected = new VEvent()
                    .withSummary("#1")
                    .withNonStandard("X-CUSTOM-PROP:THE DATA")
                    ;
            assertEquals(expected, v);
    }
}
