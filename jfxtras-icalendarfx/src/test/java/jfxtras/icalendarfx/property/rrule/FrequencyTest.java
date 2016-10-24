package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;

public class FrequencyTest
{
    @Test
    public void canParseFrequency()
    {
        String content = "DAILY";
        Frequency element = Frequency.parse(content);
        assertEquals(FrequencyType.DAILY, element.getValue());
        assertEquals("FREQ=DAILY", element.toContent());
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=YEARLY
     */
    @Test
    public void canStreamYearly()
    {
        Frequency element = new Frequency(FrequencyType.YEARLY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2016, 12, 27, 10, 0)
              , LocalDateTime.of(2017, 12, 27, 10, 0)
              , LocalDateTime.of(2018, 12, 27, 10, 0)
              , LocalDateTime.of(2019, 12, 27, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=MONTHLY
     */
    @Test
    public void canStreamMonthly()
    {
        Frequency element = new Frequency(FrequencyType.MONTHLY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2016, 1, 27, 10, 0)
              , LocalDateTime.of(2016, 2, 27, 10, 0)
              , LocalDateTime.of(2016, 3, 27, 10, 0)
              , LocalDateTime.of(2016, 4, 27, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=WEEKLY
     */
    @Test
    public void canStreamWeekly()
    {
        Frequency element = new Frequency(FrequencyType.WEEKLY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2016, 1, 3, 10, 0)
              , LocalDateTime.of(2016, 1, 10, 10, 0)
              , LocalDateTime.of(2016, 1, 17, 10, 0)
              , LocalDateTime.of(2016, 1, 24, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=DAILY
     */
    @Test
    public void canStreamDaily()
    {
        Frequency element = new Frequency(FrequencyType.DAILY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 28, 10, 0)
              , LocalDateTime.of(2015, 12, 29, 10, 0)
              , LocalDateTime.of(2015, 12, 30, 10, 0)
              , LocalDateTime.of(2015, 12, 31, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=HOURLY
     */
    @Test
    public void canStreamHourly()
    {
        Frequency element = new Frequency(FrequencyType.HOURLY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 27, 11, 0)
              , LocalDateTime.of(2015, 12, 27, 12, 0)
              , LocalDateTime.of(2015, 12, 27, 13, 0)
              , LocalDateTime.of(2015, 12, 27, 14, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=MINUTELY
     */
    @Test
    public void canStreamMinutely()
    {
        Frequency element = new Frequency(FrequencyType.MINUTELY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 27, 10, 1)
              , LocalDateTime.of(2015, 12, 27, 10, 2)
              , LocalDateTime.of(2015, 12, 27, 10, 3)
              , LocalDateTime.of(2015, 12, 27, 10, 4)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20151227T100000
    RRULE:FREQ=SECONDLY
     */
    @Test
    public void canStreamSecondly()
    {
        Frequency element = new Frequency(FrequencyType.SECONDLY);
        LocalDateTime dateTimeStart = LocalDateTime.of(2015, 12, 27, 10, 0);
        Stream<Temporal> recurrenceStream = element.streamRecurrences(dateTimeStart, 1);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 12, 27, 10, 0, 0)
              , LocalDateTime.of(2015, 12, 27, 10, 0, 1)
              , LocalDateTime.of(2015, 12, 27, 10, 0, 2)
              , LocalDateTime.of(2015, 12, 27, 10, 0, 3)
              , LocalDateTime.of(2015, 12, 27, 10, 0, 4)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
