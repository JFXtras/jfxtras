package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.Pair;

public class RecurrencesTest
{    
    @Test
    public void canParseRDate1()
    {
        String contentLine = "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("VALUE", "DATE"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "19970304,19970504,19970704,19970904"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canParseRDate2()
    {
        String contentLine = "RDATE;TZID=America/New_York:19970714T083000";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("TZID", "America/New_York"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "19970714T083000"));
        assertEquals(expectedList, valueList);
    }
    
    // Can parsing period, but period not currently supported in streaming dates
    @Test
    public void canParseRDate3()
    {
        String contentLine = "RDATE;VALUE=PERIOD:19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H";
        List<Pair<String, String>> valueList = ICalendarUtilities.parseInlineElementsToListPair(contentLine);
        List<Pair<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new Pair<>("VALUE", "PERIOD"));
        expectedList.add(new Pair<>(ICalendarUtilities.PROPERTY_VALUE_KEY, "19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H"));
        assertEquals(expectedList, valueList);
    }
    
    @Test
    public void canMakeRecurrences1()
    {
        RecurrenceDates property = new RecurrenceDates(
                LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDateTime.of(2015, 11, 14, 12, 0));
        Set<LocalDateTime> expectedDates = new LinkedHashSet<>(Arrays.asList(
                LocalDateTime.of(2015, 11, 12, 10, 0)
                , LocalDateTime.of(2015, 11, 14, 12, 0) ));
        assertEquals(expectedDates, property.getValue());
        assertEquals("RDATE:20151112T100000,20151114T120000", property.toString());
    }
    
    @Test
    public void canParseRecurrences2()
    {
        String content = "RDATE;TZID=America/Los_Angeles:19960402T010000";
        RecurrenceDates madeProperty = RecurrenceDates.parse(ZonedDateTime.class, content);
        madeProperty.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("America/Los_Angeles")));
        madeProperty.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")));
        String foldedContent = ICalendarUtilities.foldLine(content + ",19960403T010000,19960404T010000").toString();
        assertEquals(foldedContent, madeProperty.toString());
        RecurrenceDates expectedProperty = new RecurrenceDates(new HashSet<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")) )));
        assertEquals(expectedProperty, madeProperty);
        
        Set<ZonedDateTime> expectedValues = new HashSet<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")) ));
        assertEquals(expectedValues, madeProperty.getValue());
    }
    
    @Test
    public void canParseRecurrences3()
    {
        String content = "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904";
        RecurrenceDates madeProperty = new RecurrenceDates(
                LocalDate.of(1997, 3, 4),
                LocalDate.of(1997, 5, 4),
                LocalDate.of(1997, 7, 4),
                LocalDate.of(1997, 9, 4)
                );        
        assertEquals(content, madeProperty.toString());
    }
    
    /** Tests VEvent with RDATE VEvent */
    @Test
    public void canStreamRDate()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(60))
                .withRecurrenceDates(new RecurrenceDates(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 14, 12, 0)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 14, 12, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
}
