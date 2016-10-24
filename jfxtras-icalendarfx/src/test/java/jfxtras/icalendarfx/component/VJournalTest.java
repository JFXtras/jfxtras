package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class VJournalTest
{
    @Test
    public void canBuildVJournal()
    {
        VJournal builtComponent = new VJournal()
                .withDescriptions("DESCRIPTION:description 1")
                .withDescriptions("description 2", "DESCRIPTION:description 3");
        
        String componentName = builtComponent.name();
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DESCRIPTION:description 1" + System.lineSeparator() +
                "DESCRIPTION:description 2" + System.lineSeparator() +
                "DESCRIPTION:description 3" + System.lineSeparator() +
                "END:" + componentName;
                
        VJournal madeComponent = VJournal.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContent());
    }
    
    @Test
    public void canStreamWithRange()
    {
        VJournal e = new VJournal()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 12, 22, 0), 
                                                           LocalDateTime.of(2015, 11, 24, 20, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
}
