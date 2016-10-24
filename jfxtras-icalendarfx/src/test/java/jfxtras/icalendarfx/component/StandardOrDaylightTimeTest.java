package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardOrDaylight;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class StandardOrDaylightTimeTest
{
    @Test
    public void canBuildStandardOrDaylight() throws InstantiationException, IllegalAccessException
    {
        List<StandardOrDaylight<?>> components = Arrays.asList(
                new DaylightSavingTime()
                    .withTimeZoneOffsetFrom(ZoneOffset.ofHours(-4))
                    .withTimeZoneOffsetTo(ZoneOffset.ofHours(-5))
                    .withTimeZoneNames("TZNAME;LANGUAGE=fr-CA:HNE"),
                new StandardTime()
                    .withTimeZoneOffsetFrom(ZoneOffset.ofHours(-4))
                    .withTimeZoneOffsetTo(ZoneOffset.ofHours(-5))
                    .withTimeZoneNames("TZNAME;LANGUAGE=fr-CA:HNE")
                );
        
        for (StandardOrDaylight<?> builtComponent : components)
        {
            String componentName = builtComponent.name();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "TZOFFSETFROM:-0400" + System.lineSeparator() +
                    "TZOFFSETTO:-0500" + System.lineSeparator() +
                    "TZNAME;LANGUAGE=fr-CA:HNE" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.parseContent(expectedContent);

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());            
        }
    }    
    
    @Test
    public void canStreamWithRange()
    {
        StandardTime e = new StandardTime()
                .withDateTimeStart("19961027T020000")
                .withRecurrenceRule(RecurrenceRule2.parse("FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU"));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1996, 10, 27, 2, 0)
              , LocalDateTime.of(1997, 10, 26, 2, 0)
              , LocalDateTime.of(1998, 10, 25, 2, 0)
              , LocalDateTime.of(1999, 10, 31, 2, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(1996, 1, 1, 0, 0), 
                                                           LocalDateTime.of(2000, 1, 1, 0, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
}
