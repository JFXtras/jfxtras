package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ByWeekNumberTest
{

    @Test
    public void canParseByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber(4,5);
        assertEquals(Arrays.asList(4,5), element.getValue());
        assertEquals("BYWEEKNO=4,5", element.toContent());
    }

    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=20
     */
    @Test
    public void canStreamByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber(20);
        LocalDateTime dateTimeStart = LocalDateTime.of(1997, 5, 12, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1997, 5, 12, 10, 0)
              , LocalDateTime.of(1997, 5, 13, 10, 0)
              , LocalDateTime.of(1997, 5, 14, 10, 0)
              , LocalDateTime.of(1997, 5, 15, 10, 0)
              , LocalDateTime.of(1997, 5, 16, 10, 0)
              , LocalDateTime.of(1997, 5, 17, 10, 0)
              , LocalDateTime.of(1997, 5, 18, 10, 0)
              , LocalDateTime.of(1998, 5, 11, 10, 0)
              , LocalDateTime.of(1998, 5, 12, 10, 0)
              , LocalDateTime.of(1998, 5, 13, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(10).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=53
     */
    @Test
    public void canStreamByWeekNumber2()
    {
        ByWeekNumber element = new ByWeekNumber(53);
        LocalDateTime dateTimeStart = LocalDateTime.of(1997, 12, 29, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1997, 12, 29, 10, 0)
              , LocalDateTime.of(1997, 12, 30, 10, 0)
              , LocalDateTime.of(1997, 12, 31, 10, 0)
              , LocalDateTime.of(1998, 12, 28, 10, 0)
              , LocalDateTime.of(1998, 12, 29, 10, 0)
              , LocalDateTime.of(1998, 12, 30, 10, 0)
              , LocalDateTime.of(1998, 12, 31, 10, 0)
              , LocalDateTime.of(2001, 12, 31, 10, 0)
              , LocalDateTime.of(2002, 12, 30, 10, 0)
              , LocalDateTime.of(2002, 12, 31, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=-53
     */
    @Test
    public void canStreamByWeekNumber3()
    {
        ByWeekNumber element = new ByWeekNumber(-53);
        LocalDateTime dateTimeStart = LocalDateTime.of(1997, 1, 29, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1998, 1, 1, 10, 0)
              , LocalDateTime.of(1998, 1, 2, 10, 0)
              , LocalDateTime.of(1998, 1, 3, 10, 0)
              , LocalDateTime.of(1998, 1, 4, 10, 0)
              , LocalDateTime.of(1999, 1, 1, 10, 0)
              , LocalDateTime.of(1999, 1, 2, 10, 0)
              , LocalDateTime.of(1999, 1, 3, 10, 0)
              , LocalDateTime.of(2000, 1, 1, 10, 0)
              , LocalDateTime.of(2000, 1, 2, 10, 0)
              , LocalDateTime.of(2004, 1, 1, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=-1
     */
    @Test
    public void canStreamByWeekNumber4()
    {
        ByWeekNumber element = new ByWeekNumber(-1);
        LocalDateTime dateTimeStart = LocalDateTime.of(1998, 12, 28, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1998, 12, 28, 10, 0)
              , LocalDateTime.of(1998, 12, 29, 10, 0)
              , LocalDateTime.of(1998, 12, 30, 10, 0)
              , LocalDateTime.of(1998, 12, 31, 10, 0)
              , LocalDateTime.of(1999, 12, 27, 10, 0)
              , LocalDateTime.of(1999, 12, 28, 10, 0)
              , LocalDateTime.of(1999, 12, 29, 10, 0)
              , LocalDateTime.of(1999, 12, 30, 10, 0)
              , LocalDateTime.of(1999, 12, 31, 10, 0)
              , LocalDateTime.of(2000, 12, 25, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canCatchInvalidByWeekNumber()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        ByWeekNumber element = new ByWeekNumber();
        element.getValue().add(999); // throws exception
    }

}
