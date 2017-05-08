package jfxtras.icalendarfx.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

public class MiscICalendarTests
{
    @Test
    public void canCreateEmptyVCalendar()
    {
        // Create top-level calendar element
        VCalendar c = new VCalendar();
        
        // Produce text like what is shown in RFC 5545
        String expectedContent = "BEGIN:VCALENDAR" + System.lineSeparator() + 
        		"END:VCALENDAR";
        String content = c.toString();
        assertEquals(expectedContent, content);
    }
    
    @Test
    public void canAddElementsWithSetters()
    {
        VCalendar calendar = new VCalendar();
        
        VEvent event = new VEvent();
        // Properties can be set a number of ways
        event.setDateTimeStart(new DateTimeStart(LocalDateTime.of(2007, 11, 4, 20, 0))); // most explicit - reference to new property
        event.setDateTimeStart(LocalDateTime.of(2007, 11, 4, 20, 0)); // passing value of property object
        event.setDateTimeStart("DTSTART:20071104T020000"); // by parsing iCalendar content
        event.setDateTimeStart("20071104T020000"); // parsing iCalendar value (tag omitted)
        // the last three setters the API builds new DateTimeStart objects for you.
        calendar.setVEvents(new ArrayList<>(Arrays.asList(event))); // add the event to the calendar
        calendar.setProductIdentifier("-//jfxtras/iCalendarFx//EN"); // PRODID calendar property
        
        String expectedContent = "BEGIN:VCALENDAR" + System.lineSeparator() + 
        		"BEGIN:VEVENT" + System.lineSeparator() + 
        		"DTSTART:20071104T020000" + System.lineSeparator() + 
        		"END:VEVENT" + System.lineSeparator() + 
        		"PRODID:-//jfxtras/iCalendarFx//EN" + System.lineSeparator() + 
        		"END:VCALENDAR";
        assertEquals(expectedContent, calendar.toString());
    }
    
    @Test
    public void canAddElementsWithListSetters()
    {
        VEvent event = new VEvent();
        Comment comment = Comment.parse("A comment");
        List<Comment> comments = Arrays.asList(comment);
        event.setComments(comments);
        assertEquals(comments, event.getComments());
    }
    
    @Test // parse a string
    public void canCreateVCalendarByChaining2()
    {
        VEvent event = new VEvent()
            .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC)) // DTSTAMP component property
            .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0)) // DTSTART component property
            .withDuration(Duration.ofMinutes(90)) // DURATION component property
            .withRecurrenceRule(new RecurrenceRuleValue() // RRULE component property
                    .withFrequency(FrequencyType.MONTHLY) // FREQ rrule value element 
                    .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER),  // BYMONTH rrule value element 
                            new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))) // BYDAY rrule value element 
            .withUniqueIdentifier("20150110T080000-0@jfxtras.org"); // UID component property
        String content = event.toString();
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
                        .withRecurrenceRule(new RecurrenceRuleValue() // RRULE component property
                                .withFrequency(FrequencyType.YEARLY)) // FREQ rrule value element 
                        .withSummary("Yearly1 Summary") // SUMMARY component property
                        .withUniqueIdentifier("20151109T082900-0@jfxtras.org"), // UID component property
                    new VEvent()
                        .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC)) // DTSTAMP component property
                        .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0)) // DTSTART component property
                        .withDuration(Duration.ofMinutes(90)) // DURATION component property
                        .withRecurrenceRule(new RecurrenceRuleValue() // RRULE component property
                                .withFrequency(FrequencyType.MONTHLY) // FREQ rrule value element 
                                .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER),  // BYMONTH rrule value element 
                                        new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))) // BYDAY rrule value element 
                    .withUniqueIdentifier("20150110T080000-0@jfxtras.org")); // UID component property
    }
        
    @Test // parse a string
    public void canCreateVCalendarByParsing()
    {
        String nl = System.lineSeparator();
        String content =
                "BEGIN:VCALENDAR" + nl +
                "VERSION:2.0" + nl +
                "PRODID:-//jfxtras/iCalendarFx//EN" + nl +
                "BEGIN:VEVENT" + nl +
                "UID:19970610T172345Z@jfxtras.org" + nl +
                "DTSTAMP:19970610T172345Z" + nl +
                "DTSTART:19970714T170000Z" + nl +
                "DTEND:19970715T040000Z" + nl +
                "SUMMARY:Bastille Day Party" + nl +
                "END:VEVENT" + nl +
                "END:VCALENDAR";
        VCalendar c = VCalendar.parse(content); // Note: can also parse files and readers
    }
    
    @Test // Can check if RFC 5545 rules are followed.
    public void canValidate()
    {
        VEvent vEvent = new VEvent()
                .withDateTimeStart(LocalDate.of(2016, 3, 7))
                .withDateTimeEnd(LocalDate.of(2016, 3, 8))
                .withDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")))
                .withUniqueIdentifier();
        assertTrue(vEvent.isValid());
    }

    
    @Test // Can check if RFC 5545 rules are followed.
    public void canValidate2()
    {
        VEvent e = new VEvent();
        assertEquals(3, e.errors().size());
        assertFalse(e.isValid());
    }
    
    // iCalendar Transport-Independent Interoperability Protocol (iTIP) message defined in RFC 5546
    // Note: by default only supports PUBLISH, REQUEST and CANCEL messages without any ATTENDEES
    // additional features can be added by replacing the default iTIP factory and providing your own iTIP process classes. 
    @Test
    public void canProcessPublishiTIPMessage()
    {
        String ls = System.lineSeparator();
        VCalendar main = new VCalendar();
        String publish = "BEGIN:VCALENDAR"+ls+
         "METHOD:PUBLISH"+ls+
         "PRODID:-//jfxtras/iCalendarFx//EN"+ls+
         "VERSION:2.0"+ls+
         "BEGIN:VEVENT"+ls+
         "ORGANIZER:mailto:a@example.com"+ls+
         "DTSTART:19970701T200000Z"+ls+
         "DTSTAMP:19970611T190000Z"+ls+
         "SUMMARY:Jim' Birthday Party"+ls+
         "UID:1234234-23@example.com"+ls+
         "END:VEVENT"+ls+
         "END:VCALENDAR";
        List<String> log = main.processITIPMessage(publish);
        List<String> expectedLog = Arrays.asList("SUCCESS: added VEvent with UID:1234234-23@example.com");
        assertEquals(expectedLog, log);
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
    }
    
    @Test // parent-child hierarchy (e.g. find properties in a VEvent)
    public void canCreateChildrenList()
    {
        String ls = System.lineSeparator();
        String content = "BEGIN:VEVENT"+ls+
         "UID:AF23B2@example.com"+ls+
         "DTSTAMP:19970610T172345Z"+ls+
         "DTSTART;VALUE=DATE:19970714"+ls+
         "SUMMARY:Bastille Day Party"+ls+
         "END:VEVENT";
        VEvent e = VEvent.parse(content);
        
        List<VChild> eChildren = e.childrenUnmodifiable();
        DateTimeStart d = e.getDateTimeStart();
        assertTrue(eChildren.contains(d));

        List<VChild> dChildren = d.childrenUnmodifiable();
        ValueParameter dt = d.getValueType();
        assertTrue(dChildren.contains(dt));
    }
}
