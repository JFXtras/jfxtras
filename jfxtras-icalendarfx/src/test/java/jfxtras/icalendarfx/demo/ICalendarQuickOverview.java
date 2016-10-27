package jfxtras.icalendarfx.demo;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

public class ICalendarQuickOverview
{
    @Test
    public void canCreateEmptyVCalendar()
    {
        // create empty VCalendar
        VCalendar c = new VCalendar();
        
        // Show content. All calendar elements have toContent method - to produce content like what is shown in RFC 5545
        System.out.println(c.toContent());
    }
    
    @Test
    public void canCreateVCalendarByChaining()
    {
        VCalendar c = new VCalendar()
                .withProductIdentifier(new ProductIdentifier("-//jfxtras/iCalendarFx//EN"))
                .withVersion(new Version())
                .withCalendarScale(new CalendarScale())
                .withVTodos(new VTodo()
                        .withDateTimeCompleted("COMPLETED:19960401T150000Z")
                        .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000")
                        .withPercentComplete(35))
                .withVTimeZones(new VTimeZone()
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
                                    .withTimeZoneNames("EST")))
                .withVEvents(new VEvent()
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
                    .withUniqueIdentifier("20151109T082900-0@jfxtras.org"))
                .withVEvents(new VEvent()
                    .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                    .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0))
                    .withDuration(Duration.ofMinutes(90))
                    .withRecurrenceRule(new RecurrenceRule2()
                            .withFrequency(FrequencyType.MONTHLY)
                            .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER),
                                    new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
                    .withUniqueIdentifier("20150110T080000-0@jfxtras.org"));
        System.out.println(c.toContent());
    }
    
    @Test
    public void canCreateVCalendarByParsing()
    {
        String content =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "VERSION:2.0" + System.lineSeparator() +
                "PRODID:-//hacksw/handcal//NONSGML v1.0//EN" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
                "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
                "DTSTART:19970714T170000Z" + System.lineSeparator() +
                "DTEND:19970715T040000Z" + System.lineSeparator() +
                "SUMMARY:Bastille Day Party" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        VCalendar c = VCalendar.parse(content); // Note: can also parse files and readers
        System.out.println(c.toContent());
    }
    
    @Test
    public void canProcessPublishiTIPMessage() // Note: default iTIP classes can handle PUBLISH, REQUEST and CANCEL messages without any ATTENDEES
    {
        VCalendar c = new VCalendar();
        String publish = "BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() + 
              "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
              "DTSTART:19970701T200000Z" + System.lineSeparator() + 
              "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
              "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
              "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR";
        VCalendar message = VCalendar.parse(publish);
        c.processITIPMessage(message);
        System.out.println(c.toContent());
    }
    
    @Test
    public void canProcessCanceliTIPMessage() // Note: default iTIP classes can handle PUBLISH, REQUEST and CANCEL messages without any ATTENDEES
    {
        String mainContent = 
                "BEGIN:VCALENDAR" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970705T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR";
        VCalendar main = VCalendar.parse(mainContent);
        String publish = 
              "BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:CANCEL" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() + 
              "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
              "COMMENT:DUKES forfeit the game" + System.lineSeparator() + 
              "SEQUENCE:2" + System.lineSeparator() + 
              "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
              "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR";
        VCalendar message = VCalendar.parse(publish);
        main.processITIPMessage(message);
        System.out.println(main.toContent());
    }
    
    @Test // parent-child hierarchy
    public void canCreateChildrenList()
    {
        String content =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "VERSION:2.0" + System.lineSeparator() +
                "PRODID:-//hacksw/handcal//NONSGML v1.0//EN" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
                "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:19970714" + System.lineSeparator() +
                "DTEND;VALUE=DATE:19970715" + System.lineSeparator() +
                "SUMMARY:Bastille Day Party" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        VCalendar c = VCalendar.parse(content); // Note: can also parse files and readers
        // children are calendar properties and calendar components
        // Note: children are in the same order they are added to the parent
        List<VChild> vCalendarChildren = c.childrenUnmodifiable();
        vCalendarChildren.forEach(System.out::println);
        System.out.println();

        VEvent e = c.getVEvents().get(0);
        // children are component properties
        List<VChild> vEventChildren = e.childrenUnmodifiable();
        vEventChildren.forEach(System.out::println);   
        System.out.println();

        DateTimeStart dtstart = e.getDateTimeStart();
        // children are property parameters
        List<VChild> dtStartChildren = dtstart.childrenUnmodifiable();
        dtStartChildren.forEach(System.out::println);
        
        // property value is not a child, but here it is
        System.out.println(dtstart.getValue());
    }
    
    /*
     * Daily for 10 occurrences:
     * 
     DTSTART;TZID=America/New_York:19970902T090000
     RRULE:FREQ=DAILY;COUNT=10
     */
    @Test
    public void canStreamRRule1()
    {
        String s = "RRULE:FREQ=DAILY;COUNT=10";
        RecurrenceRule rRule = RecurrenceRule.parse(s);
        Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
        rRule.getValue()
                .streamRecurrences(dateTimeStart)
                .forEach(System.out::println);
    }
    
    /*
     * Every Friday the 13th, forever:
     * 
    DTSTART;VALUE=DATE:19980213
    RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13
    */
    @Test
    public void canStreamRRule2()
    {
        String s = "RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13";
        RecurrenceRule rRule = RecurrenceRule.parse(s);
        assertEquals(rRule.toContent(), s);
        Temporal dateTimeStart = LocalDate.of(1998, 2, 13);
        rRule.getValue()
                .streamRecurrences(dateTimeStart)
                .filter(t -> ((LocalDate) t).isAfter(LocalDate.of(2016, 1, 1)))
                .limit(5)
                .forEach(System.out::println);
    }
   
    @Test
    public void canBindProperties()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0));
        
        e.dateTimeStartProperty().addListener((obs) -> System.out.println("Yikes! DTSTART changed."));
        
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.now())); // fires
        e.setDateTimeStart(LocalDateTime.now()); // fires
        e.getDateTimeStart().setValue(LocalDate.now()); // won't fire listener
    }
    
}
