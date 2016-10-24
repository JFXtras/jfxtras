package jfxtras.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySetPosition;

public class BySetPositionTest
{
    @Test
    public void canParseSetPosition()
    {
        BySetPosition element = new BySetPosition(-2,5);
        assertEquals(Arrays.asList(-2,5), element.getValue());
        assertEquals("BYSETPOS=-2,5", element.toContent());
    }
    
    @Test
    public void canStreamSetPosition()
    {
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 0, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        List<Temporal> list = Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 0, 0), 
                LocalDateTime.of(2016, 1, 5, 0, 0),
                LocalDateTime.of(2016, 1, 6, 0, 0)
                );
        
        BySetPosition element = new BySetPosition(2);        
        Stream<Temporal> recurrenceStream = element.streamRecurrences(list.stream(), frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 5, 0, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(8).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test
    public void canStreamSetPosition2()
    {
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 0, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        List<Temporal> list = Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 0, 0), 
                LocalDateTime.of(2016, 1, 5, 0, 0),
                LocalDateTime.of(2016, 1, 6, 0, 0)
                );
        
        BySetPosition element = new BySetPosition(-1);        
        Stream<Temporal> recurrenceStream = element.streamRecurrences(list.stream(), frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 6, 0, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(8).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
