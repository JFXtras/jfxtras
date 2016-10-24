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

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ByMonthDayTest
{
    @Test
    public void canParseByMonth()
    {
        ByMonthDay element = new ByMonthDay(4,14);
        assertEquals(Arrays.asList(4, 14), element.getValue());
        assertEquals("BYMONTHDAY=4,14", element.toContent());
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=DAILY;BYMONTHDAY=4,5,7,8
     */
    @Test
    public void canStreamByMonthDay()
    {
        ByMonthDay element = new ByMonthDay(4,5,7,8);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.DAYS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2016, 5, 7, 10, 0)
              , LocalDateTime.of(2016, 5, 8, 10, 0)
              , LocalDateTime.of(2016, 6, 4, 10, 0)
              , LocalDateTime.of(2016, 6, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYMONTHDAY=4,5
     */
    @Test
    public void canStreamByMonthDay2()
    {
        ByMonthDay element = new ByMonthDay(4,5);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2016, 6, 4, 10, 0)
              , LocalDateTime.of(2016, 6, 5, 10, 0)
              , LocalDateTime.of(2016, 7, 4, 10, 0)
              , LocalDateTime.of(2016, 7, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160510T100000
    RRULE:FREQ=MONTHLY;BYMONTHDAY=10,11,12
     */
    @Test
    public void canStreamByMonthDay3()
    {
        ByMonthDay element = new ByMonthDay(10,11,12);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 10, 10, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 10, 10, 0)
              , LocalDateTime.of(2016, 5, 11, 10, 0)
              , LocalDateTime.of(2016, 5, 12, 10, 0)
              , LocalDateTime.of(2016, 6, 10, 10, 0)
              , LocalDateTime.of(2016, 6, 11, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }    
    
    /*
    DTSTART:20160510T100000
    RRULE:FREQ=MONTHLY;BYMONTHDAY=-3
     */
    @Test
    public void canStreamByMonthDay4()
    {
        ByMonthDay element = new ByMonthDay(-3);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 29, 10, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 29, 10, 0)
              , LocalDateTime.of(2016, 6, 28, 10, 0)
              , LocalDateTime.of(2016, 7, 29, 10, 0)
              , LocalDateTime.of(2016, 8, 29, 10, 0)
              , LocalDateTime.of(2016, 9, 28, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160510T100000
    RRULE:FREQ=MONTHLY;BYMONTHDAY=30
     */
    @Test
    public void canStreamByMonthDay5()
    {
        ByMonthDay element = new ByMonthDay(30);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 30, 10, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 3, 30, 10, 0)
              , LocalDateTime.of(2016, 4, 30, 10, 0)
              , LocalDateTime.of(2016, 5, 30, 10, 0)
              , LocalDateTime.of(2016, 6, 30, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
