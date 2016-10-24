package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.components.VRepeatable;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see StandardTime
 * @see DaylightSavingTime
 * 
 * for the following properties:
 * @see RecurrenceDates
 * @see RecurrenceRule
 * 
 * @author David Bal
 *
 */
public class RepeatableTest //extends Application
{
    @Test
    public void canBuildRepeatable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VRepeatable<?>> components = Arrays.asList(
                new VEvent()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new VTodo()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new VJournal()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new DaylightSavingTime()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4)),
                new StandardTime()
                    .withRecurrenceDates("RDATE;VALUE=DATE:20160504,20160508,20160509")
                    .withRecurrenceDates(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(4))
                );
        
        List<LocalDate> expectedDates = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2016, 4, 13) // DTSTART
              , LocalDate.of(2016, 4, 15) // 2nd RDATE
              , LocalDate.of(2016, 4, 16) // 2nd RDATE
              , LocalDate.of(2016, 4, 17) // 2nd RDATE and RRULE
              , LocalDate.of(2016, 4, 21) // RRULE
              , LocalDate.of(2016, 4, 25) // RRULE
              , LocalDate.of(2016, 4, 29) // RRULE
              , LocalDate.of(2016, 5, 3) // RRULE
              , LocalDate.of(2016, 5, 4) // 1st RDATE
              , LocalDate.of(2016, 5, 7) // RRULE
              , LocalDate.of(2016, 5, 8) // 1st RDATE
              , LocalDate.of(2016, 5, 9) // 1st RDATE
                ));
        
        for (VRepeatable<?> builtComponent : components)
        {
            String componentName = builtComponent.name();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "RDATE;VALUE=DATE:20160504,20160508,20160509" + System.lineSeparator() +
                    "RDATE;VALUE=DATE:20160415,20160416,20160417" + System.lineSeparator() +
                    "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.parseContent(expectedContent);
            System.out.println("parsedComponent:" + parsedComponent);

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());
            
            ((VPrimary<?>) builtComponent).setDateTimeStart(new DateTimeStart(LocalDate.of(2016, 4, 13)));
            List<Temporal> madeDates = builtComponent                    
                    .streamRecurrences()
                    .limit(12)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
    }
    
    @Test
    public void canDetectErrors1()
    {
        VEvent component = new VEvent()
                .withRecurrenceRule(new RecurrenceRule2());
        component.errors().forEach(System.out::println);
        assertEquals(4, component.errors().size());
    }

    @Test
    public void canStreamRecurrences1()
    {
        LocalDate dateTimeStart = LocalDate.of(2016, 4, 22);
        VEvent component = new VEvent()
                .withRecurrenceRule("RRULE:FREQ=DAILY")
                .withDateTimeStart(dateTimeStart);
        List<Temporal> expectedRecurrences = Stream
                .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(100)
                .collect(Collectors.toList());
        List<Temporal> madeRecurrences = component.streamRecurrences().limit(100).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }

    @Test
    public void canStreamLaterStart()
    {
        VEvent component = new VEvent()
                .withRecurrenceRule("RRULE:FREQ=DAILY")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 22, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 2, 0));
        List<Temporal> expectedRecurrences = Stream
                .iterate(LocalDateTime.of(2016, 5, 31, 22, 0), a -> a.plus(1, ChronoUnit.DAYS))
                .limit(10)
                .collect(Collectors.toList());
        List<Temporal> madeRecurrences = component
                .streamRecurrences(LocalDateTime.of(2016, 5, 31, 22, 0))
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }

    @Test //(expected = DateTimeException.class)
    // TODO - RELY ON isValid test instead of listener - change test
    public void canHandleDTStartTypeChange()
    {
        VEvent component = new VEvent()
            .withDateTimeStart(LocalDate.of(1997, 3, 1))
            .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        String errorPrefix = "RDATE:";
        boolean hasError = component.errors().stream()
            .anyMatch(s -> s.substring(0, errorPrefix.length()).equals(errorPrefix));
        assertFalse(hasError);
        component.setDateTimeStart(DateTimeStart.parse(ZonedDateTime.class, "20160302T223316Z")); // invalid
        hasError = component.errors().stream()
            .anyMatch(s -> s.substring(0, errorPrefix.length()).equals(errorPrefix));
        assertTrue(hasError);
    }

    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType()
    {
        VEvent component = new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        ObservableList<RecurrenceDates> recurrences = FXCollections.observableArrayList();
        recurrences.add(RecurrenceDates.parse(LocalDateTime.class, "20160228T093000"));
        component.setRecurrenceDates(recurrences);
        String errorPrefix = "RDATE:";
        boolean hasError = component.errors().stream()
            .anyMatch(s -> s.substring(0, errorPrefix.length()).equals(errorPrefix));
        assertTrue(hasError);
    }

    @Test (expected = DateTimeException.class)
    public void canCatchDifferentRepeatableTypes()
    {
        Thread.currentThread().setUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        VEvent builtComponent = new VEvent()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        ObservableSet<Temporal> expectedValues = FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) );        
        builtComponent.getRecurrenceDates().add(new RecurrenceDates(expectedValues));
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchDifferentRepeatableTypes2()
    {
        Thread.currentThread().setUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        VEvent builtComponent = new VEvent()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        builtComponent.getRecurrenceDates().get(0).getValue().add(LocalDateTime.of(1996, 4, 4, 1, 0));
    }
    
    /*
     * STREAM RECURRENCES TESTS
     */
    
    /** tests converting ISO.8601.2004 date-time string to LocalDateTime */
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.YEARLY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2016, 11, 9, 10, 0)
              , LocalDateTime.of(2017, 11, 9, 10, 0)
              , LocalDateTime.of(2018, 11, 9, 10, 0)
              , LocalDateTime.of(2019, 11, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=YEARLY;BYDAY=FR */
    @Test
    public void yearlyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 4, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYDAY=FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8 */
    @Test
    public void yearlyStreamTest3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(DayOfWeek.THURSDAY),
                                new ByMonth(Month.JUNE, Month.JULY, Month.AUGUST)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(20)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 5, 9, 0)
              , LocalDateTime.of(1997, 6, 12, 9, 0)
              , LocalDateTime.of(1997, 6, 19, 9, 0)
              , LocalDateTime.of(1997, 6, 26, 9, 0)
              , LocalDateTime.of(1997, 7, 3, 9, 0)
              , LocalDateTime.of(1997, 7, 10, 9, 0)
              , LocalDateTime.of(1997, 7, 17, 9, 0)
              , LocalDateTime.of(1997, 7, 24, 9, 0)
              , LocalDateTime.of(1997, 7, 31, 9, 0)
              , LocalDateTime.of(1997, 8, 7, 9, 0)
              , LocalDateTime.of(1997, 8, 14, 9, 0)
              , LocalDateTime.of(1997, 8, 21, 9, 0)
              , LocalDateTime.of(1997, 8, 28, 9, 0)
              , LocalDateTime.of(1998, 6, 4, 9, 0)
              , LocalDateTime.of(1998, 6, 11, 9, 0)
              , LocalDateTime.of(1998, 6, 18, 9, 0)
              , LocalDateTime.of(1998, 6, 25, 9, 0)
              , LocalDateTime.of(1998, 7, 2, 9, 0)
              , LocalDateTime.of(1998, 7, 9, 9, 0)
              , LocalDateTime.of(1998, 7, 16, 9, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    @Test
    public void yearlyStreamTest4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 1, 6, 10, 0)
              , LocalDateTime.of(2015, 2, 6, 10, 0)
              , LocalDateTime.of(2016, 1, 6, 10, 0)
              , LocalDateTime.of(2016, 2, 6, 10, 0)
              , LocalDateTime.of(2017, 1, 6, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    @Test
    public void yearlyStreamTest5()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByMonth(Month.NOVEMBER), new ByMonthDay(10)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 0, 0)
              , LocalDateTime.of(2016, 11, 10, 0, 0)
              , LocalDateTime.of(2017, 11, 10, 0, 0)
              , LocalDateTime.of(2018, 11, 10, 0, 0)
              , LocalDateTime.of(2019, 11, 10, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());

    }
    
    /** FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYMONTHDAY=2,3,4,5,6,7,8;BYDAY=TU
     * (U.S. Presidential Election day) */
    @Test
    public void yearlyStreamTest6()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withInterval(4)
                        .withByRules(new ByMonth(Month.NOVEMBER)
                                   , new ByMonthDay(2,3,4,5,6,7,8)
                                   , new ByDay(DayOfWeek.TUESDAY)
                                   ));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1996, 11, 5, 0, 0)
              , LocalDateTime.of(2000, 11, 7, 0, 0)
              , LocalDateTime.of(2004, 11, 2, 0, 0)
              , LocalDateTime.of(2008, 11, 4, 0, 0)
              , LocalDateTime.of(2012, 11, 6, 0, 0)
              , LocalDateTime.of(2016, 11, 8, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYMONTHDAY=2,3,4,5,6,7,8;BYDAY=TU";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    @Test
    public void yearlyStreamTest7()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 19, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 20))));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 5, 19, 10, 0)
              , LocalDateTime.of(1998, 5, 18, 10, 0)
              , LocalDateTime.of(1999, 5, 17, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYDAY=20MO";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO */
    @Test
    public void yearlyStreamTest8()
    {
//        Locale oldLocale = Locale.getDefault();
//        Locale.setDefault(Locale.FRANCE); // has Monday as first day of week system.  US is Sunday which causes an error.
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 12, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByWeekNumber(20),
                                     new ByDay(DayOfWeek.MONDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 5, 12, 10, 0)
              , LocalDateTime.of(1998, 5, 11, 10, 0)
              , LocalDateTime.of(1999, 5, 17, 10, 0)
              , LocalDateTime.of(2000, 5, 15, 10, 0)
              , LocalDateTime.of(2001, 5, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
//        Locale.setDefault(oldLocale);
    }
    
    /** Tests daily stream with FREQ=MONTHLY */
    @Test
    public void monthlyStreamTest()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2016, 1, 9, 10, 0)
              , LocalDateTime.of(2016, 2, 9, 10, 0)
              , LocalDateTime.of(2016, 3, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYMONTHDAY=-2 */
    @Test
    public void monthlyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 29, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                                .withByRules(new ByMonthDay()
                                        .withValue(-2))); // repeats 2nd to last day of month
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 29, 10, 0)
              , LocalDateTime.of(2015, 12, 30, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 28, 10, 0)
              , LocalDateTime.of(2016, 3, 30, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYMONTHDAY=-2";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYDAY=TU,WE,FR */
    @Test
    public void monthlyStreamTest3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 1, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYDAY=TU,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYDAY=-1SA */
    @Test
    public void monthlyStreamTest4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1)))); // last Saturday in month
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 28, 10, 0)
              , LocalDateTime.of(2015, 12, 26, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 27, 10, 0)
              , LocalDateTime.of(2016, 3, 26, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYDAY=-1SA";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    @Test
    public void monthlyStreamTest5()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 13, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY), new ByMonthDay(13)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 13, 10, 0)
              , LocalDateTime.of(1998, 2, 13, 10, 0)
              , LocalDateTime.of(1998, 3, 13, 10, 0)
              , LocalDateTime.of(1998, 11, 13, 10, 0)
              , LocalDateTime.of(1999, 8, 13, 10, 0)
              , LocalDateTime.of(2000, 10, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
        RecurrenceRule2 r = RecurrenceRule2.parse("FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13");
        assertEquals(r, e.getRecurrenceRule().getValue()); // verify order of parameters doesn't matter
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR */
    @Test
    public void monthlyStreamTest6()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER)
                                   , new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(13)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 3, 10, 0)
              , LocalDateTime.of(2015, 11, 4, 10, 0)
              , LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 1, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=WEEKLY */
    @Test
    public void weeklyStreamTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 30, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    @Test
    public void weeklyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 21, 10, 0)
              , LocalDateTime.of(2015, 12, 23, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR */
    @Test
    public void weeklyStreamTest3()
    {
        VEvent e = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
            .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.WEEKLY)
                    .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    @Test
    public void canStreamWeekly4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withCount(11)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        );
        List<Temporal> madeDates = e.streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 21, 10, 0)
              , LocalDateTime.of(2015, 12, 23, 10, 0)
              , LocalDateTime.of(2015, 12, 25, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test // tests starting on Sunday (1st day of week) with other day of the week
    public void canStreamWeekly5()
    {
        VEvent e = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2016, 1, 3, 5, 0))
            .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.WEEKLY)
                    .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY))); 
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2016, 1, 3, 5, 0)
              , LocalDateTime.of(2016, 1, 6, 5, 0)
              , LocalDateTime.of(2016, 1, 10, 5, 0)
              , LocalDateTime.of(2016, 1, 13, 5, 0)
              , LocalDateTime.of(2016, 1, 17, 5, 0)
              , LocalDateTime.of(2016, 1, 20, 5, 0)
              , LocalDateTime.of(2016, 1, 24, 5, 0)
              , LocalDateTime.of(2016, 1, 27, 5, 0)
              , LocalDateTime.of(2016, 1, 31, 5, 0)
              , LocalDateTime.of(2016, 2, 3, 5, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;BYDAY=SU,WE";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void canStreamWeeklyZoned()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<ZonedDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 23, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 25, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 27, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 30, 10, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=3;COUNT=6 */
    @Test
    public void dailyStreamTest2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3)
                        .withCount(6)
                        );
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=3;BYMONTHDAY=9,10,11,12,13,14 */
    @Test
    public void dailyStreamTest3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3)
                        .withByRules(new ByMonthDay()
                            .withValue(9,10,11,12,13,14)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 12, 10, 0)
              , LocalDateTime.of(2016, 1, 11, 10, 0)
              , LocalDateTime.of(2016, 1, 14, 10, 0)
              , LocalDateTime.of(2016, 2, 10, 10, 0)
              , LocalDateTime.of(2016, 2, 13, 10, 0)
              , LocalDateTime.of(2016, 3, 11, 10, 0)
              , LocalDateTime.of(2016, 3, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=3;BYMONTHDAY=9,10,11,12,13,14";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=2;BYMONTHDAY=9 */
    @Test
    public void dailyStreamTest4()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2)
                        .withByRules(new ByMonthDay(9)) );
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2016, 2, 9, 10, 0)
              , LocalDateTime.of(2016, 4, 9, 10, 0)
              , LocalDateTime.of(2016, 5, 9, 10, 0)
              , LocalDateTime.of(2016, 8, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;BYMONTHDAY=9";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    /** Tests daily stream with FREQ=DAILY;INTERVAL=2;BYDAY=FR */
    @Test
    public void dailyStreamTest5()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 25, 10, 0)
              , LocalDateTime.of(2016, 1, 8, 10, 0)
              , LocalDateTime.of(2016, 1, 22, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;BYDAY=FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTest6()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        );
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 11, 29, 10, 0)
              ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=" + 
                DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(
                        ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                        .withZoneSameInstant(ZoneId.of("Z")));
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTestJapanZone()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan")))
                .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.DAILY)
                    .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 1, 0), ZoneId.of("Z")))
                    );
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 12, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 14, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 8, 0), ZoneId.of("Japan"))
              ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=DAILY;UNTIL=20151119T010000Z";
        assertEquals(expectedContent, e.getRecurrenceRule().toContent());
    }
    
    @Test
    public void dailyStreamTestUTC()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("Z")))
                .withRecurrenceRule(new RecurrenceRule2()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0), ZoneId.of("Z")))
                .withFrequency(FrequencyType.DAILY)
                        .withInterval(2));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<ZonedDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 21, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 23, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 25, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 27, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneId.of("Z"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0), ZoneId.of("Z"))
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests individual VEvent */
    @Test
    public void individualTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 30)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests VEvent with RDATE VEvent */
    @Test
    public void canStreamRDate()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
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
    
    @Test
    public void getWeekly2ChangeStart()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));

        LocalDateTime start2 = LocalDateTime.of(2015, 12, 6, 0, 0);
        List<Temporal> madeDates2 = e
                .streamRecurrences(start2)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
    }
    
    // ten years in future
    @Test
    public void getWeekly2FarFuture()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        
        LocalDateTime start = LocalDateTime.of(2025, 11, 10, 0, 0);
        List<Temporal> madeDates = e
                .streamRecurrences(start)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 10, 10, 0)
              , LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        
        LocalDateTime start2 = LocalDateTime.of(2015, 11, 11, 0, 0);
        List<Temporal> madeDates2 = e
                .streamRecurrences(start2)
                .limit(2)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
        
        LocalDateTime start3 = LocalDateTime.of(2025, 11, 12, 0, 0);
        List<Temporal> madeDates3 = e
                .streamRecurrences(start3)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates3 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
              , LocalDateTime.of(2025, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates3, madeDates3);

        LocalDateTime start4 = LocalDateTime.of(2025, 11, 17, 0, 0);
        List<Temporal> madeDates4 = e
                .streamRecurrences(start4)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates4 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 24, 10, 0)
              , LocalDateTime.of(2025, 11, 26, 10, 0)
              , LocalDateTime.of(2025, 11, 28, 10, 0)
                ));
        assertEquals(expectedDates4, madeDates4);
    }
    
    // Whole day tests
    
    @Test
    public void makeDatesWholeDayDaily2()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        List<Temporal> madeDates = e               
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 9)
              , LocalDate.of(2015, 11, 12)
              , LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
              , LocalDate.of(2015, 11, 24)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void makeDatesWholeDayDaily3()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(LocalDate.of(2015, 11, 24))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        List<Temporal> madeDates = e                
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 9)
              , LocalDate.of(2015, 11, 12)
              , LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
              , LocalDate.of(2015, 11, 24)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test // LocalDate
    public void canChangeToWholeDay()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY));
        e.setDateTimeStart(new DateTimeStart(LocalDate.of(2015, 11, 9))); // change to whole-day
        {
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDate.of(2015, 11, 9)
                  , LocalDate.of(2015, 11, 10)
                  , LocalDate.of(2015, 11, 11)
                  , LocalDate.of(2015, 11, 12)
                  , LocalDate.of(2015, 11, 13)
                  , LocalDate.of(2015, 11, 14)
                    ));
            assertEquals(expectedDates, madeDates);
        }
        
        // Change back to date/time
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))); // change to date/time
        { // start date/time
            List<Temporal> madeDates = e                
                    .streamRecurrences()
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, madeDates);
        }
    }
    
    @Test // tests cached stream ability to reset when RRule and start changes
    public void canChangeStartStreamTest()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY));

        { // initialize stream
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
        
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.of(2015, 11, 10, 10, 0))); // change start
        { // make new stream
            List<Temporal> madeDates = e
                    .streamRecurrences(LocalDateTime.of(2015, 12, 9, 10, 0))
//                    .recurrenceStreamer().stream(LocalDateTime.of(2015, 12, 9, 10, 0))
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 12, 9, 10, 0);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // request date beyond first cached date to test cache system
        Temporal t = e.streamRecurrences(LocalDateTime.of(2016, 12, 25, 10, 0)).findFirst().get();
        assertEquals(LocalDateTime.of(2016, 12, 25, 10, 0), t);
    }
    
    @Test // tests cached stream ability to reset when RRule and start changes
    public void canChangeRRuleStreamTest()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY));        
        { // initialize stream
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // Change RRule
        e.setRecurrenceRule(new RecurrenceRule2()
                .withFrequency(FrequencyType.WEEKLY)
                .withInterval(2)
                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));

        { // check new repeatable stream
            List<Temporal> madeDates = e
                    .streamRecurrences()
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(2, ChronoUnit.WEEKS))
                    .flatMap(d -> 
                    {
                        List<Temporal> days = new ArrayList<>();
                        days.add(d); // Mondays
                        days.add(d.plus(2, ChronoUnit.DAYS)); // Wednesdays
                        days.add(d.plus(4, ChronoUnit.DAYS)); // Fridays
                        return days.stream();
                    })
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // request date beyond first cached date to test cache system
        Temporal date = e.streamRecurrences(LocalDateTime.of(2015, 12, 9, 10, 0)).findFirst().get();
        assertEquals(LocalDateTime.of(2015, 12, 9, 10, 0), date);
    }
    
    @Test
    public void canFindPreviousStreamTemporal()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        e.streamRecurrences().limit(100).collect(Collectors.toList()); // set cache
        assertEquals(LocalDateTime.of(2016, 1, 20, 10, 0), e.recurrenceCache().previousValue(LocalDateTime.of(2016, 1, 21, 10, 0)));

        VEvent e2 = new VEvent() // without cache
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        assertEquals(LocalDate.of(2015, 11, 24), e2.recurrenceCache().previousValue(LocalDate.of(2015, 12, 31)));
    }
    
    // Tests added components with recurrence ID to parent's list of recurrences
    @Test
    public void canHandleRecurrenceID()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        e.streamRecurrences().limit(100).collect(Collectors.toList()); // set cache
        assertEquals(LocalDateTime.of(2016, 1, 20, 10, 0), e.recurrenceCache().previousValue(LocalDateTime.of(2016, 1, 21, 10, 0)));

        VEvent e2 = new VEvent() // without cache
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
        assertEquals(LocalDate.of(2015, 11, 24), e2.recurrenceCache().previousValue(LocalDate.of(2015, 12, 31)));
    }
    
    @Test
    public void canFindPreviousStreamValue()
    {
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal previous = vComponentEdited.previousStreamValue(startRecurrence);
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 0), previous);
    }

    @Test
    public void canCheckIfIsRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        VEvent vComponentMain = ICalendarStaticComponents.getDaily1();
        vCalendar.addVComponent(vComponentMain);
        
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vCalendar.addVComponent(vComponentRecurrence);
        
        assertTrue(vComponentMain.isRecurrence(LocalDateTime.of(2016, 5, 16, 10, 0)));
        assertFalse(vComponentMain.isRecurrence(LocalDateTime.of(2016, 5, 17, 10, 0))); // is not recurrence because vComponentRecurrence uses this date/time
        assertTrue(vComponentMain.isRecurrence(LocalDateTime.of(2016, 5, 18, 10, 0)));
        
        assertTrue(vComponentRecurrence.isRecurrence(LocalDateTime.of(2016, 5, 17, 8, 30)));
    }

}
