package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
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

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ByDayTest
{    
    @Test
    public void canParseByDay()
    {
        ByDay element = ByDay.parse("-1SU");
        ByDayPair byDayPair = new ByDayPair()
                .withDayOfWeek(DayOfWeek.SUNDAY)
                .withOrdinal(-1);
        assertEquals(byDayPair, element.getValue().get(0));
        assertEquals("BYDAY=-1SU", element.toContent());
    }
    
    /*
    DTSTART:20160503T100000
    RRULE:FREQ=YEARLY;BYDAY=-1SU,2MO
     */
    @Test
    public void canStreamByDay()
    {
        ByDay element = ByDay.parse("-1SU,2MO");
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2016, 1, 11, 10, 0)
              , LocalDateTime.of(2016, 12, 25, 10, 0)
              , LocalDateTime.of(2017, 1, 9, 10, 0)
              , LocalDateTime.of(2017, 12, 31, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
       DTSTART:20160503T100000
       RRULE:FREQ=YEARLY;BYDAY=SU,MO
     */
    @Test
    public void canStreamByDay2()
    {
        ByDay element = ByDay.parse("SU,MO");
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 2, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 2, 10, 0)
              , LocalDateTime.of(2016, 5, 8, 10, 0)
              , LocalDateTime.of(2016, 5, 9, 10, 0)
              , LocalDateTime.of(2016, 5, 15, 10, 0)
              , LocalDateTime.of(2016, 5, 16, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160615T100000
    RRULE:FREQ=MONTHLY;BYDAY=3WE,2TU
  */
    @Test
    public void canStreamByDay3()
    {
        ByDay element = ByDay.parse("3WE,2TU");
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 6, 15, 10, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 6, 15, 10, 0)
              , LocalDateTime.of(2016, 7, 12, 10, 0)
              , LocalDateTime.of(2016, 7, 20, 10, 0)
              , LocalDateTime.of(2016, 8, 9, 10, 0)
              , LocalDateTime.of(2016, 8, 17, 10, 0)
              , LocalDateTime.of(2016, 9, 13, 10, 0)
              , LocalDateTime.of(2016, 9, 21, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(7)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160508T100000
    RRULE:FREQ=MONTHLY;BYDAY=SA,SU
  */
    @Test
    public void canStreamByDay4()
    {
        ByDay element = ByDay.parse("SA,SU");
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 8, 10, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 8, 10, 0)
              , LocalDateTime.of(2016, 5, 14, 10, 0)
              , LocalDateTime.of(2016, 5, 15, 10, 0)
              , LocalDateTime.of(2016, 5, 21, 10, 0)
              , LocalDateTime.of(2016, 5, 22, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160509T100000
    RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR
  */
    @Test
    public void canStreamByDay5()
    {
        ByDay element = ByDay.parse("MO,WE,FR");
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 9, 10, 0);
        ChronoUnit frequency = ChronoUnit.WEEKS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 9, 10, 0)
              , LocalDateTime.of(2016, 5, 11, 10, 0)
              , LocalDateTime.of(2016, 5, 13, 10, 0)
              , LocalDateTime.of(2016, 5, 16, 10, 0)
              , LocalDateTime.of(2016, 5, 18, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160510T100000
    RRULE:FREQ=HOURLY;INTERVAL=8;BYDAY=TU
  */
    @Test
    public void canStreamByDay7()
    {
        ByDay element = ByDay.parse("TU");
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 10, 0, 0);
        ChronoUnit frequency = ChronoUnit.HOURS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(8, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 10, 0, 0)
              , LocalDateTime.of(2016, 5, 10, 8, 0)
              , LocalDateTime.of(2016, 5, 10, 16, 0)
              , LocalDateTime.of(2016, 5, 17, 0, 0)
              , LocalDateTime.of(2016, 5, 17, 8, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
