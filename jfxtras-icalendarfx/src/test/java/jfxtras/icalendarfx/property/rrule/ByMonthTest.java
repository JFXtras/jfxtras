package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ByMonthTest
{
    @Test
    public void canParseByMonth()
    {
        ByMonth element = new ByMonth(4);
        assertEquals(Month.APRIL, element.getValue().get(0));
        assertEquals("BYMONTH=4", element.toContent());
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=DAILY;BYMONTH=4
     */
    @Test
    public void canStreamByMonth()
    {
        ByMonth element = new ByMonth(4);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.DAYS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2017, 4, 1, 10, 0)
              , LocalDateTime.of(2017, 4, 2, 10, 0)
              , LocalDateTime.of(2017, 4, 3, 10, 0)
              , LocalDateTime.of(2017, 4, 4, 10, 0)
              , LocalDateTime.of(2017, 4, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYMONTH=4,5
     */
    @Test
    public void canStreamByMonth2()
    {
        ByMonth element = new ByMonth(4,5);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2017, 4, 5, 10, 0)
              , LocalDateTime.of(2017, 5, 5, 10, 0)
              , LocalDateTime.of(2018, 4, 5, 10, 0)
              , LocalDateTime.of(2018, 5, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
