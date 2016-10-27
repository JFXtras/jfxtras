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

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    // Different ways to create calendar objects and add children properties
    
    @Test
    public void canCreateEmptyVCalendar()
    {
        // create empty VCalendar - top-level object stores all calendar information
        VCalendar c = new VCalendar();
        
        // Show content. All calendar elements have toContent method - to produce text  like what is shown in RFC 5545
        String content = c.toContent();
        System.out.println(content);
    }
    
    @Test
    public void canAddElementsWithSetters()
    {
        VCalendar c = new VCalendar();
        c.setProductIdentifier("-//jfxtras/iCalendarFx//EN"); // PRODID calendar property
        
        VEvent e = new VEvent();
        // Properties can be set a number of ways
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.of(2007, 11, 4, 20, 0))); // most explicit - reference to new property
        e.setDateTimeStart("DTSTART:20071104T020000"); // by parsing iCalendar content
        e.setDateTimeStart("20071104T020000"); // parsing iCalendar value (tag omitted)
        e.setDateTimeStart(LocalDateTime.of(2007, 11, 4, 20, 0)); // passing value of property object
        // the last three setters the API builds new DateTimeStart objects for you.
        c.getVEvents().add(e);
                
        System.out.println(c.toContent());
    }
    
    @Test
    public void canCreateVCalendarByChaining()
    {
        VCalendar c = new VCalendar()
                .withProductIdentifier(new ProductIdentifier("-//jfxtras/iCalendarFx//EN")) // PRODID calendar property
                .withVersion(new Version()) // VERSION calendar property
                .withCalendarScale(new CalendarScale()) // CALSCALE calendar property
                .withVTodos(new VTodo() // VTODO calendar component
                        .withDateTimeCompleted("COMPLETED:19960401T150000Z") // CREATED component property
                        .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000") // DUE component property
                        .withPercentComplete(35)) // PERCENT-COMPLETE component property
                .withVTimeZones(new VTimeZone() // VTIMEZONE calendar component
                        .withTimeZoneIdentifier("America/New_York") // TZID component property
                        .withDateTimeLastModified("20050809T050000Z") // LAST-MODIFIED component property
                        .withStandardOrDaylight(
                                new DaylightSavingTime() // 1 - DAYLIGHT component sub-component
                                    .withDateTimeStart("19670430T020000") // DTSTART component property
                                    .withRecurrenceRule("FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0500") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0400") // TZOFFSETTO component property
                                    .withTimeZoneNames("EDT"), // TZNAME component property
                                new StandardTime() // 2 - STANDARD component sub-component
                                    .withDateTimeStart("19671029T020000") // DTSTART component property
                                    .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0400") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0500") // TZOFFSETTO component property
                                    .withTimeZoneNames("EST"), // TZNAME component property
                                new DaylightSavingTime() // 3 - DAYLIGHT component sub-component
                                    .withDateTimeStart("19740106T020000") // DTSTART component property
                                    .withRecurrenceDates("RDATE:19750223T020000") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0500") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0400") // TZOFFSETTO component property
                                    .withTimeZoneNames("EDT"), // TZNAME component property
                                new DaylightSavingTime() // 4 - DAYLIGHT component sub-component
                                    .withDateTimeStart("19760425T020000") // DTSTART component property
                                    .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0500") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0400") // TZOFFSETTO component property
                                    .withTimeZoneNames("EDT"), // TZNAME component property
                                new DaylightSavingTime() // 5 - DAYLIGHT component sub-component
                                    .withDateTimeStart("19870405T020000") // DTSTART component property
                                    .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0500") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0400") // TZOFFSETTO component property
                                    .withTimeZoneNames("EDT"), // TZNAME component property
                                new DaylightSavingTime() // 6 - DAYLIGHT component sub-component
                                    .withDateTimeStart("20070311T020000") // DTSTART component property
                                    .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0500") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0400") // TZOFFSETTO component property
                                    .withTimeZoneNames("EDT"), // TZNAME component property
                                new StandardTime() // 7 - STANDARD component sub-component
                                    .withDateTimeStart("20071104T020000") // DTSTART component property
                                    .withRecurrenceRule("RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU") // RRULE component property
                                    .withTimeZoneOffsetFrom("-0400") // TZOFFSETFROM component property
                                    .withTimeZoneOffsetTo("-0500") // TZOFFSETTO component property
                                    .withTimeZoneNames("EST"))) // TZNAME component property
                .withVEvents(
                    new VEvent()
                        .withCategories("group13") // CATEGORIES component property
                        .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC)) // CREATED component property
                        .withDescription("Yearly1 Description") // DESCRIPTION component property
                        .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC)) // DTSTAMP component property
                        .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0)) // DTSTART component property
                        .withDuration(Duration.ofHours(1)) // DURATION component property
                        .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC)) // LAST-MODIFIED component property
                        .withRecurrenceRule(new RecurrenceRule2() // RRULE component property
                                .withFrequency(FrequencyType.YEARLY)) // FREQ rrule value element 
                        .withSummary("Yearly1 Summary") // SUMMARY component property
                        .withUniqueIdentifier("20151109T082900-0@jfxtras.org"), // UID component property
                    new VEvent()
                        .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC)) // DTSTAMP component property
                        .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0)) // DTSTART component property
                        .withDuration(Duration.ofMinutes(90)) // DURATION component property
                        .withRecurrenceRule(new RecurrenceRule2() // RRULE component property
                                .withFrequency(FrequencyType.MONTHLY) // FREQ rrule value element 
                                .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER),  // BYMONTH rrule value element 
                                        new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))) // BYDAY rrule value element 
                    .withUniqueIdentifier("20150110T080000-0@jfxtras.org")); // UID component property
        System.out.println(c.toContent());
    }
    
    @Test // parse a string
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
    
    @Test // Can check if RFC 5545 rules are followed.
    public void canValidate()
    {
        VEvent e = new VEvent();
        List<String> errors = e.errors();
        errors.forEach(System.out::println);
        System.out.println("isValid:" + e.isValid());
        
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
        System.out.println(c.errors());
        System.out.println("isValid:" + c.isValid());
    }
    
    // iCalendar Transport-Independent Interoperability Protocol (iTIP) message defined in RFC 5546
    // Note: by default only supports PUBLISH, REQUEST and CANCEL messages without any ATTENDEES
    // additional features can be added by replacing the default iTIP factory and providing your own iTIP process classes. 
    @Test
    public void canProcessPublishiTIPMessage()
    {
        VCalendar main = new VCalendar();
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
        List<String> log = main.processITIPMessage(publish);
        log.forEach(System.out::println);
        System.out.println(main.toContent());
    }
    
    @Test
    public void canProcessCanceliTIPMessage()
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
        String cancel = 
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
        List<String> log = main.processITIPMessage(cancel);
        log.forEach(System.out::println);
        System.out.println(main.toContent());
    }
    
    @Test // parent-child hierarchy (e.g. find properties in a VEvent)
    public void canCreateChildrenList()
    {
        String content =
                "BEGIN:VEVENT" + System.lineSeparator() +
                "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
                "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:19970714" + System.lineSeparator() +
                "DTEND;VALUE=DATE:19970715" + System.lineSeparator() +
                "SUMMARY:Bastille Day Party" + System.lineSeparator() +
                "END:VEVENT";
        VEvent e = VEvent.parse(content); // Note: can also parse files and readers

        // children are component properties
        List<VChild> vEventChildren = e.childrenUnmodifiable();
        vEventChildren.forEach(System.out::println);   
        System.out.println();

        DateTimeStart dtstart = e.getDateTimeStart();
        // children are property parameters
        List<VChild> dtStartChildren = dtstart.childrenUnmodifiable();
        dtStartChildren.forEach(System.out::println);
    }
    
    // A number of calendar components (e.g. VEVENT) can be repeatable - done by the recurrence rule property.
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
                .streamRecurrences(dateTimeStart) // Make stream of date/time values here
                .forEach(System.out::println);
    }
    
    // Streams are great for an endless series of date/times (calculate values lazily)
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
                .streamRecurrences(dateTimeStart) // infinite steam make here
                .filter(t -> ((LocalDate) t).isAfter(LocalDate.of(2016, 1, 1)))
                .limit(5) // get only the quantity you want
                .forEach(System.out::println);
    }

    @Test // Bind two properties to synchronize data (e.g. between display control and application data)
    public void canBindProperties()
    {
        VEvent applicationVEvent = new VEvent()
                .withSummary("initial summary");
                
        StringProperty displayProperty = new SimpleStringProperty(); // make "display" property
        applicationVEvent.getSummary().valueProperty().bind(displayProperty); // bind "application" property to "display" property
        displayProperty.set("new summary"); // change "display" property and automatically change "application" property as well

        System.out.println(applicationVEvent.toContent());
    }
    
    @Test
    public void canAttachListenerToProperties()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0));
        
        InvalidationListener listener = (obs) -> System.out.println("Yikes! DTSTART changed.");
        e.dateTimeStartProperty().addListener(listener);
        
        e.setDateTimeStart(new DateTimeStart(LocalDateTime.now())); // fires listener
    }
    
}
