package jfxtras.icalendarfx;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Comparator;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public abstract class ICalendarTestAbstract
{
    /** Sorts by DTSTART, then by the string content */
    public final static Comparator<VPrimary<?>> VCOMPONENT_COMPARATOR = (VPrimary<?> v1, VPrimary<?> v2) ->
    {
        if ((v1.getDateTimeStart() != null) && (v2.getDateTimeStart() != null))
        {
            Temporal t1 = v1.getDateTimeStart().getValue();
            Temporal t2 = v2.getDateTimeStart().getValue();
            int dtstartCompare = DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(t1, t2);
            if (dtstartCompare != 0) return dtstartCompare;
        }
        return v1.toContent().compareTo(v2.toContent());
    };
    
    /** FREQ=YEARLY; */
    protected VEvent getYearly1()
    {
        return new VEvent()
                .withCategories("group13")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC))
                .withDescription("Yearly1 Description")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofHours(1))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY))
                .withSummary("Yearly1 Summary")
                .withUniqueIdentifier("20151109T082900-0@jfxtras.org");
    }
    
    /** FREQ=YEARLY; */
    protected VEvent getRecurrenceForYearly1()
    {
        return new VEvent()
                .withCategories("group13")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 6, 0))
                .withDescription("Yearly1 Description-edited")
                .withDuration(Duration.ofHours(2))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC))
                .withRecurrenceId(LocalDateTime.of(2016, 11, 9, 10, 0))
                .withSequence(1)
                .withSummary("Yearly1 Summary-edited")
                .withUniqueIdentifier("20151109T082900-0@jfxtras.org");
    }
    
    /** FREQ=YEARLY; */
    protected VEvent getRecurrenceForYearly2()
    {
        return new VEvent()
                .withCategories("group13")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 6, 0))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC))
                .withUniqueIdentifier("20151109T082900-0@jfxtras.org")
                .withDuration(Duration.ofHours(2))
                .withDescription("Yearly1 Description-edited")
                .withSummary("Yearly1 Summary-edited")
                .withRecurrenceId(LocalDateTime.of(2018, 11, 9, 10, 0))
                .withSequence(1);
    }

    /** FREQ=YEARLY;BYDAY=FR; */
    protected VEvent getYearly2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY)));
    }
    
    /**Every Thursday, but only during June, July, and August, forever:
     * DTSTART;TZID=America/New_York:19970605T090000
     * RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8
     * example in RFC 5545 iCalendar, page 129 */
    protected VEvent getYearly3()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(DayOfWeek.THURSDAY),
                                new ByMonth(Month.JUNE, Month.JULY, Month.AUGUST)));
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    protected VEvent getYearly4()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY)));
//        RRule rule = new RRule();
//                .withRecurrenceRule(rule);
//        Frequency yearly = FrequencyType.YEARLY;
//        rule.setFrequency(yearly);
//        Rule byRule = new ByMonth(Month.JANUARY, Month.FEBRUARY);
//        yearly.addByRule(byRule);
    }

    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    protected VEvent getYearly5()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByMonth(Month.NOVEMBER),
                                new ByMonthDay(10)));
    }

    /** RFC 5545 iCalendar, page 130 
     * Every 4 years, the first Tuesday after a Monday in November,
      forever (U.S. Presidential Election day):

       DTSTART;TZID=America/New_York:19961105T090000
       RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;
        BYMONTHDAY=2,3,4,5,6,7,8 */
    protected VEvent getYearly6()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withInterval(4)
                        .withByRules(new ByMonth(Month.NOVEMBER),
                                new ByDay(DayOfWeek.TUESDAY),
                                new ByMonthDay(2,3,4,5,6,7,8)));
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    protected VEvent getYearly7()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 19, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 20))));
    }
    
    /** FREQ=YEARLY;WKST=MO;BYWEEKNO=20;BYDAY=MO */
    protected VEvent getYearly8()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 12, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.YEARLY)
                        .withByRules(new ByWeekNumber(20)
                                   , new ByDay(DayOfWeek.MONDAY)));
    }
        
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    protected VEvent getMonthly1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY));
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected VEvent getMonthly2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 29, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByMonthDay()
                                .withValue(-2))); // repeats 2nd to last day of month
    }

    /** FREQ=MONTHLY;BYDAY=TU,WE,FR */
    protected VEvent getMonthly3()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
    }

    /** FREQ=MONTHLY;BYDAY=-1SA */
    protected VEvent getMonthly4()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1)))); // last Saturday in month
    }

    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    protected VEvent getMonthly5()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 13, 10, 0))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(1997, 9, 1, 8, 30), ZoneOffset.UTC))
                .withDuration(Duration.ofHours(1))
                .withUniqueIdentifier("19970901T083000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY), new ByMonthDay(13)));
    }

    /** FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR  */
    protected VEvent getMonthly6()
    {
        return new VEvent()
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0))
                .withDuration(Duration.ofMinutes(90))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER),
                                new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    /** FREQ=MONTHLY;BYDAY=3MO */
    protected VEvent getMonthly7()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.MONDAY, 3))));
    }
    
    
    /** FREQ=WEEKLY, Basic weekly stream */
    protected VEvent getWeekly1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY));
    }

    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    protected static VEvent getWeekly2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withDuration(Duration.ofMinutes(45))
                .withDescription("Weekly1 Description")
                .withSummary("Weekly1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(2)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    protected VEvent getWeekly3()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
                .withDuration(Duration.ofMinutes(45))
                .withDescription("Weekly3 Description")
                .withLocation("here")
                .withSummary("Weekly3 Summary")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    protected VEvent getWeekly4()
    {
        VEvent vEvent = getWeekly2();
        vEvent.getRecurrenceRule().getValue().setCount(11);
        return vEvent;
    }
    
    protected VEvent getWeekly5()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2016, 1, 3, 5, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 1, 3, 7, 0))
                .withDescription("Weekly5 Description")
                .withSummary("Weekly5 Summary")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY)));      
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
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }

    
    /** FREQ=DAILY, Basic daily stream */
    public static VEvent getDaily1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily1 Description")
                .withSummary("Daily1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY));
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static VEvent getDaily2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(90))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withCategories("group03")
                .withDescription("Daily2 Description")
                .withSummary("Daily2 Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3)
                        .withCount(6));
    }

    /** FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14 */
    protected VEvent getDaily3()
    {
        return new VEvent()
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.DAILY)
                    .withInterval(3)
                    .withCount(10)
                    .withByRules(new ByMonthDay()
                            .withValue(9,10,11,12,13,14)) )
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }

    /** FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9 */
    protected VEvent getDaily4()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                                .withInterval(2)
                                .withByRules(new ByMonthDay(9)));
    }
    
    /** FREQ=DAILY;INVERVAL=2;BYDAY=FR */
    protected VEvent getDaily5()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                                .withInterval(2)
                                .withByRules(new ByDay(DayOfWeek.FRIDAY)));
    }

    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151201T095959 */
    protected static VEvent getDaily6()
    {
        return new VEvent()
                .withCategories("group03")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2));
    }
    
    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151129T100000 
     * Tests inclusive UNTIL */
    protected static VEvent getDaily7()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2));
    }
    
    public static VEvent getDailyUTC()
    {
        return new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 11, 0), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withCategories("group03")
                .withDescription("DailyUTC Description")
                .withSummary("DailyUTC Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(2)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0), ZoneOffset.UTC)));
    }
    
    public static VEvent getDailyJapanZone()
    {
        return new VEvent()
                .withCategories("group03")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 9, 0), ZoneId.of("Japan")))
                .withDescription("Japan Description")
                .withSummary("Japan Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 1, 0), ZoneOffset.UTC))
                        .withFrequency(FrequencyType.DAILY));
    }
    
    /** Individual - non repeatable VEvent */
    public static VEvent getIndividual1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30))
                .withDuration(Duration.ofMinutes(60))
                .withDescription("Individual Description")
                .withSummary("Individual Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    protected static VEvent getIndividual2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 11))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    public static VEvent getIndividualZoned()
    {
        return new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.of("Europe/London")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
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

    protected static VEvent getRecurrences()
    {
        return new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(60))
                .withRecurrenceDates(new RecurrenceDates(
                        LocalDateTime.of(2015, 11, 12, 10, 0),
                        LocalDateTime.of(2015, 11, 14, 12, 0)));
    }
    
    /** all-day appointments */
    protected VEvent getWholeDayDaily1()
    {
        return new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    /* FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected VEvent getWholeDayDaily2()
    {
        return new VEvent()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
    }

    /* FREQ=DAILY;INVERVAL=3;UNTIL=20151124 */
    protected static VEvent getWholeDayDaily3()
    {
        return new VEvent()
                .withCategories("group06")
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 11))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(LocalDate.of(2015, 11, 24))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3));
    }

    /*
     *  Tests for multi-part recurrence sets
     *  Children have RECURRENCE-ID
     *  Branches have RELATED-TO
     */
    
//    public static List<VComponent<InstanceMock>> getRecurrenceSetDaily1()
//    {
//        List<VComponent<InstanceMock>> recurrenceSet = new ArrayList<>();
//        VEventNew parent = getDaily1();
//
//        VEventNew child1 = getDaily1()
//                .withRecurrenceRule((RecurrenceRule) null)
//                .withRecurrenceId(LocalDateTime.of(2015, 11, 10, 10, 0))
//                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 15, 0))
//                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 17, 0));
//
//        VEventNew child2 = getDaily1()
//                .withRecurrenceRule((RecurrenceRule) null)
//                .withRecurrenceId(LocalDateTime.of(2015, 11, 12, 10, 0))
//                .withDateTimeStart(LocalDateTime.of(2015, 11, 13, 6, 0))
//                .withDateTimeEnd(LocalDateTime.of(2015, 11, 13, 7, 0));
//                
//        recurrenceSet.add(parent);
//        recurrenceSet.add(child1);
//        recurrenceSet.add(child2);
//        parent.getRecurrenceRule().getValue().recurrences().add(child1);
//        parent.getRecurrenceRule().getValue().recurrences().add(child2);
//
//        return recurrenceSet;
//
//    }
//
//    // child of getDaily6
//    protected static VEventNew getChild1()
//    {
//        VEventNew v = getDaily6();
//        v.setDateTimeStart(LocalDateTime.of(2015, 11, 22, 16, 0));
//        v.setDateTimeEnd(LocalDateTime.of(2015, 11, 22, 18, 0));
//        v.setRecurrenceId(LocalDateTime.of(2015, 11, 21, 10, 0));
//        v.setRecurrenceRule((RecurrenceRule) null);
//        return v;
//    }
//    
//    protected static List<VEventNew> getDailyWithRecurrence()
//    {
//        List<VEventNew> recurrenceSet = new ArrayList<>();
//        VEventNew parent = getDaily6();
//        VEventNew child = getChild1();
//        
//        recurrenceSet.add(parent);
//        recurrenceSet.add(child);
//        parent.getRecurrenceRule().getValue().recurrences().add(child);
//
//        return recurrenceSet;
//    }
    
    /* Example Google individual appointment */
    protected static VEvent getGoogleIndividual()
    {
        VEvent v = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 12, 30), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 15, 0), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 32), ZoneOffset.UTC))
                .withUniqueIdentifier("vpqej26mlpg3adcncqqs7t7a34@google.com")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 13), ZoneOffset.UTC))
                .withDescription("")
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 13), ZoneOffset.UTC))
                .withLocation("")
                .withSequence(0)
                .withStatus("CONFIRMED")
                .withSummary("test1")
                .withTimeTransparency("OPAQUE")
                ;
        return v;
    }
    
    /* Example Google repeatable appointment */
    public static VEvent getGoogleRepeatable()
    {
        VEvent v = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 8, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 32), ZoneOffset.UTC))
                .withUniqueIdentifier("im8hmpakeigu3d85j3vq9q8bcc@google.com")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDescription("")
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withLocation("")
                .withSequence(0)
                .withStatus(StatusType.CONFIRMED)
                .withSummary("test2")
                .withTimeTransparency(TimeTransparencyType.OPAQUE)
                ;
        return v;
    }
    
    /* Example Google repeatable appointment with EXDATE*/
    protected static VEvent getGoogleWithExceptions()
    {
        return new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 15, 30), ZoneId.of("America/Los_Angeles")))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneOffset.UTC)))
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 7, 22, 31), ZoneOffset.UTC))
                .withUniqueIdentifier("86801l7316n97h75cefk1ruc00@google.com")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDescription("")
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withLocation("")
                .withSequence(0)
                .withStatus(StatusType.CONFIRMED)
                .withSummary("test3")
                .withTimeTransparency(TimeTransparencyType.OPAQUE)
                ;
    }
    
    /* Example Google repeatable appointment with 3 parts 
     * Parent*/
    protected static VEvent getGoogleRepeatablePart1()
    {
        return new VEvent()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 13, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 17), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 18, 59, 59), ZoneOffset.UTC)))
                .withSummary("test4")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts
     * 
     * This-and-future edit of Parent
     * For this part, Google doesn't use RELATED-TO to establish the parent.
     * Instead, Google adds a UTC date, like a RECURRENCE-ID, to the UID
     * The special UID is converted to the RELATED-TO field internally */
    protected static VEvent getGoogleRepeatablePart2()
    {
        return new VEvent()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 14, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 17), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRelatedTo("mrrfvnj5acdcvn13273on9nrhs@google.com")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withCount(6))
                .withSummary("test5")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs_R20160218T190000@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts 
     * Recurrence */
    protected static VEvent getGoogleRepeatablePart3()
    {
        return new VEvent()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 9, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 32, 26), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 7, 0), ZoneId.of("America/Los_Angeles")))
                .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withSequence(1)
                .withSummary("test6")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs@google.com");
    }
    
//    protected static List<VEventNew> getGoogleRepeatableParts()
//    {
//        List<VEventNew> vComponents = new ArrayList<>();
//        VEventNew p1 = getGoogleRepeatablePart1();
//        VEventNew p2 = getGoogleRepeatablePart2();
//        VEventNew p3 = getGoogleRepeatablePart3();
//        vComponents.add(p1);
//        vComponents.add(p2);
//        vComponents.add(p3);
//        p1.getRecurrenceRule().withRecurrences(p3);
//        return vComponents;
//    }

    protected static VEvent getSplitWeek()
    {
        return new VEvent()
                .withDateTimeEnd(LocalDateTime.of(2016, 3, 13, 5, 45))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2016, 3, 12, 4, 0))
                .withDescription("Split Description")
                .withSummary("Split Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    public static VTimeZone getTimeZone1()
    {
        return new VTimeZone()
            .withTimeZoneIdentifier("America/New_York")
            .withDateTimeLastModified("20050809T050000Z")
            .withStandardOrDaylight(
                    new DaylightSavingTime() // 1
                        .withDateTimeStart("19670430T020000")
                        .withRecurrenceRule("FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z")
                        .withTimeZoneOffsetFrom("-0500")
                        .withTimeZoneOffsetTo("-0400")
                        .withTimeZoneNames("EDT"),
                    new StandardTime() // 2
                        .withDateTimeStart("19671029T020000")
                        .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z")
                        .withTimeZoneOffsetFrom("-0400")
                        .withTimeZoneOffsetTo("-0500")
                        .withTimeZoneNames("EST"),
                    new DaylightSavingTime() // 3
                        .withDateTimeStart("19740106T020000")
                        .withRecurrenceDates("RDATE:19750223T020000")
                        .withTimeZoneOffsetFrom("-0500")
                        .withTimeZoneOffsetTo("-0400")
                        .withTimeZoneNames("EDT"),
                    new DaylightSavingTime() // 4
                        .withDateTimeStart("19760425T020000")
                        .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z")
                        .withTimeZoneOffsetFrom("-0500")
                        .withTimeZoneOffsetTo("-0400")
                        .withTimeZoneNames("EDT"),
                    new DaylightSavingTime() // 5
                        .withDateTimeStart("19870405T020000")
                        .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z")
                        .withTimeZoneOffsetFrom("-0500")
                        .withTimeZoneOffsetTo("-0400")
                        .withTimeZoneNames("EDT"),
                    new DaylightSavingTime() // 6
                        .withDateTimeStart("20070311T020000")
                        .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU")
                        .withTimeZoneOffsetFrom("-0500")
                        .withTimeZoneOffsetTo("-0400")
                        .withTimeZoneNames("EDT"),
                    new StandardTime() // 6
                        .withDateTimeStart("20071104T020000")
                        .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU")
                        .withTimeZoneOffsetFrom("-0400")
                        .withTimeZoneOffsetTo("-0500")
                        .withTimeZoneNames("EST"));
    }
}
