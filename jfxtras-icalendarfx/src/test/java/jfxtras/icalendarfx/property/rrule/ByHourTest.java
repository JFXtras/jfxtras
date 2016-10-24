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

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;

public class ByHourTest
{
    @Test
    public void canParseByHour()
    {
        ByHour element = new ByHour(10,20);
        assertEquals(Arrays.asList(10,20), element.getValue());
        assertEquals("BYHOUR=10,20", element.toContent());
    }
    
    /*
    DTSTART:20160104T000000
    RRULE:FREQ=MINUTELY;BYHOUR=10,12
     */
    @Test
    public void canStreamByHour()
    {
        ByHour element = new ByHour(10,12);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 10, 0);
        ChronoUnit frequency = ChronoUnit.MINUTES;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(30, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 10, 0)
              , LocalDateTime.of(2016, 1, 4, 10, 30)
              , LocalDateTime.of(2016, 1, 4, 12, 0)
              , LocalDateTime.of(2016, 1, 4, 12, 30)
              , LocalDateTime.of(2016, 1, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYMONTH=4,5
     */
    @Test
    public void canStreamByHour2()
    {
        ByHour element = new ByHour(10,11,12);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2016, 5, 5, 11, 0)
              , LocalDateTime.of(2016, 5, 5, 12, 0)
              , LocalDateTime.of(2017, 5, 5, 10, 0)
              , LocalDateTime.of(2017, 5, 5, 11, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test  (expected = IllegalArgumentException.class)
    public void canCatchOutOfRangeByHour()
    {
        new ByHour(1100,200,300);
    }
}
