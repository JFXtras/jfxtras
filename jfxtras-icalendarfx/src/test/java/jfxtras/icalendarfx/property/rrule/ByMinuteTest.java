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

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;

public class ByMinuteTest
{
    @Test
    public void canParseByMinute()
    {
        ByMinute element = new ByMinute(10,20);
        assertEquals(Arrays.asList(10,20), element.getValue());
        assertEquals("BYMINUTE=10,20", element.toContent());
    }
    
    /*
    DTSTART:20160101T100000
    RRULE:FREQ=MINUTELY;BYMINUTE=10,15,20
     */
    @Test
    public void canStreamByMinute()
    {
        ByMinute element = new ByMinute(10,15,20);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 1, 10, 10);
        ChronoUnit frequency = ChronoUnit.MINUTES;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 1, 10, 10)
              , LocalDateTime.of(2016, 1, 1, 10, 15)
              , LocalDateTime.of(2016, 1, 1, 10, 20)
              , LocalDateTime.of(2016, 1, 1, 11, 10)
              , LocalDateTime.of(2016, 1, 1, 11, 15)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYMONTH=4,5
     */
    @Test
    public void canStreamByMinute2()
    {
        ByMinute element = new ByMinute(10,20);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 10);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 10)
              , LocalDateTime.of(2016, 5, 5, 10, 20)
              , LocalDateTime.of(2017, 5, 5, 10, 10)
              , LocalDateTime.of(2017, 5, 5, 10, 20)
              , LocalDateTime.of(2018, 5, 5, 10, 10)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test  (expected = IllegalArgumentException.class)
    public void canCatchOutOfRangeByMinute()
    {
        new ByMinute(1100,200,300);
    }
}
