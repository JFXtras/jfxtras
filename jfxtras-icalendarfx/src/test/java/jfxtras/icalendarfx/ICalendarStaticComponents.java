package jfxtras.icalendarfx;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

/**
 * Static VEvents representing iCalendar components
 */
public final class ICalendarStaticComponents
{   
    public final static List<String> DEFAULT_APPOINTMENT_GROUPS = IntStream.range(0, 24)
            .mapToObj(i -> "group" + (i < 10 ? "0" : "") + i)
            .collect(Collectors.toList());
    
    private ICalendarStaticComponents() { }

    /** FREQ=YEARLY; */
    public static VEvent getYearly1()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(13))
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC))
                .withUniqueIdentifier("20151109T082900-001@jfxtras.org")
                .withDuration(Duration.ofHours(1))
                .withDescription("Yearly1 Description")
                .withSummary("Yearly1 Summary")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY));
    }
        
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    public static VEvent getMonthly1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY));
    }
    
    /** FREQ=MONTHLY;BYDAY=3MO */
    public static VEvent getMonthly7()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 16, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 16, 11, 0))
                .withUniqueIdentifier("20150110T080000-002@jfxtras.org")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.MONDAY, 3))));
    }
    
    /** FREQ=WEEKLY, Basic weekly stream */
    public static VEvent getWeekly1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY));
    }

    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    public static VEvent getWeekly2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withDuration(Duration.ofMinutes(45))
                .withDescription("Weekly1 Description")
                .withSummary("Weekly1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-002@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
    }
    
    /** FREQ=WEEKLY;BYDAY=MO */
    public static VEvent getWeekly3()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withUniqueIdentifier("20150110T080000-002@jfxtras.org")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY)));
    }

    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    public static VEvent getWeekly5()
    {
        VEvent vEvent = getWeekly2();
        vEvent.getRecurrenceRule().getValue().setCount(11);
        return vEvent;
    }
    
    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    public static VEvent getWeeklyZoned()
    {
        return new VEvent()
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 45), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withDescription("WeeklyZoned Description")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
                .withSummary("WeeklyZoned Summary")
                .withOrganizer("ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org")
                .withUniqueIdentifier("20150110T080000-003@jfxtras.org");
    }
    
    /** FREQ=DAILY, Basic daily stream */
    public static VEvent getDaily1()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(5))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily1 Description")
                .withSummary("Daily1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-004@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY))
                .withOrganizer("ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org");
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    public static VEvent getDaily2()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 30))
//                .withDuration(Duration.ofMinutes(90))
                .withDescription("Daily2 Description")
                .withSummary("Daily2 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-005@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3))
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org");
    }
    
    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151201T095959 */
    public static VEvent getDaily6()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-006@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2));
    }
    
    /** Individual - non repeatable VEvent */
    public static VEvent getIndividual1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30))
                .withDuration(Duration.ofMinutes(60))
                .withDescription("Individual Description")
                .withSummary("Individual Summary")
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-007@jfxtras.org");
    }
    
    // Whole day events
    public static VEvent getIndividual2()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(13))
                .withDateTimeStart(LocalDate.of(2015, 11, 11))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-008@jfxtras.org");
    }
    
    public static VEvent getIndividualZoned()
    {
        return new VEvent()
                .withOrganizer("ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org")
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(13))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.of("Europe/London")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-009@jfxtras.org");
    }
    
    /** FREQ=DAILY;INVERVAL=3;COUNT=6
     *  EXDATE=20151112T100000,20151115T100000 */
    public static VEvent getDailyWithException1()
    {
        return getDaily2()
                .withExceptionDates(new ExceptionDates(
                        LocalDateTime.of(2015, 11, 12, 10, 0),
                        LocalDateTime.of(2015, 11, 15, 10, 0)));
    }
    
    /* FREQ=DAILY */
    public static VEvent getWholeDayDaily1()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(6))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-010@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY))
                .withDateTimeStart(LocalDate.of(2015, 11, 8))
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withDateTimeEnd(LocalDate.of(2015, 11, 9));
    }
    
    /* FREQ=DAILY;INVERVAL=3;UNTIL=20151124 */
    public static VEvent getWholeDayDaily3()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(6))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-010@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(LocalDate.of(2015, 11, 23))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3))
                .withDateTimeStart(LocalDate.of(2015, 11, 8))
                .withDateTimeEnd(LocalDate.of(2015, 11, 10));
    }
    
    /* FREQ=DAILY;INVERVAL=3;UNTIL=20151124 */
    public static VEvent getWholeDayDaily4()
    {
        return new VEvent()
                .withCategories(DEFAULT_APPOINTMENT_GROUPS.get(6))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-010@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(LocalDate.of(2017, 11, 24))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3))
                .withDateTimeStart(LocalDate.of(2015, 11, 8))
                .withDateTimeEnd(LocalDate.of(2015, 11, 10));
    }
}
