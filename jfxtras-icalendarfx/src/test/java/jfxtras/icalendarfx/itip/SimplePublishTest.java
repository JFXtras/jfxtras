package jfxtras.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

/**
 * Tests to demonstrate PUBLISH iTIP message ability
 * 
 * @author David Bal
 *
 */
public class SimplePublishTest
{
    @Test
    public void canProcessPublish()
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
        VCalendar inputVCalendar = VCalendar.parse(publish);
        main.processITIPMessage(inputVCalendar);
        String expectedContent = "BEGIN:VCALENDAR" + System.lineSeparator() + 
//                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970701T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR";
        assertEquals(expectedContent, main.toString());
    }
    
    @Test
    public void canReviseWithPublish()
    {
        VEvent vComponent = ICalendarStaticComponents.getDaily1();
        VCalendar mainVCalendar = new VCalendar()
        		.withVersion()
        		.withVEvents(vComponent);
        String publish = new String(
                "BEGIN:VCALENDAR" + System.lineSeparator() + 
                "METHOD:PUBLISH" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151108T100000" + System.lineSeparator() +
                "DTEND:20151108T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:revised summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        mainVCalendar.processITIPMessage(publish);
        
        String expectedContent = new String(
                "BEGIN:VCALENDAR" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151108T100000" + System.lineSeparator() +
                "DTEND:20151108T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:revised summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        assertEquals(expectedContent, mainVCalendar.toString());
    }
    
    @Test // the time has been changed, an end time has been added, and the sequence number has been adjusted.
    public void canProcessPublishToReplace()
    {
        String mainContent = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970705T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        VCalendar main = VCalendar.parse(mainContent);
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() + 
              "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
              "DTSTART:19970701T200000Z" + System.lineSeparator() + 
              "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
              "DTEND:19970701T230000Z" + System.lineSeparator() + 
              "SEQUENCE:1" + System.lineSeparator() + 
              "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
              "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        VCalendar inputVCalendar = VCalendar.parse(publish);
        main.processITIPMessage(inputVCalendar);
        String expectedContent = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970701T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "DTEND:19970701T230000Z" + System.lineSeparator() + 
                "SEQUENCE:1" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        assertEquals(expectedContent, main.toString());
    }
    
    @Test // edit an individual recurrence of a repeatable event twice
    public void canEditOneRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "DTEND:20160516T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T151835Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.DTSTART_COMPARATOR);
        VEvent myComponentRepeats = vComponents.get(0);

        // check results
        assertEquals(vComponentOriginal, myComponentRepeats);
        VEvent myComponentIndividual = vComponents.get(1);
        VEvent expectedComponentIndividual = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 30))
                .withDateTimeStamp(new DateTimeStamp(myComponentIndividual.getDateTimeStamp()))
                .withRecurrenceRule((RecurrenceRule) null)
                .withRecurrenceId("20160516T100000")
                .withSequence(1);
        assertEquals(expectedComponentIndividual, myComponentIndividual);
                
        // Check child components
        assertEquals(Arrays.asList(myComponentIndividual), myComponentRepeats.recurrenceChildren());
        assertEquals(Collections.emptyList(), myComponentIndividual.recurrenceChildren());

        // 2nd edit - edit component with RecurrenceID (individual)       
        String iTIPMessage2 =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T120000" + System.lineSeparator() +
                "DTEND:20160516T130000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                "DTSTAMP:20160914T155333Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:2" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";        
        mainVCalendar.processITIPMessage(iTIPMessage2);

        assertEquals(2, vComponents.size());
        
        // confirm change
        VEvent myComponentIndividual2 = vComponents.get(1);
        VEvent expectedComponentIndividual2 = ICalendarStaticComponents.getDaily1()
                .withSummary("new summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 16, 12, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 16, 13, 0))
                .withDateTimeStamp(new DateTimeStamp(myComponentIndividual2.getDateTimeStamp()))
                .withRecurrenceRule((RecurrenceRule) null)
                .withRecurrenceId("20160516T100000")
                .withSequence(2);
        assertEquals(expectedComponentIndividual2, myComponentIndividual2);

        // Check child components
        assertEquals(Arrays.asList(myComponentIndividual2), myComponentRepeats.recurrenceChildren());
        assertEquals(Collections.emptyList(), myComponentIndividual2.recurrenceChildren());
    }
    
    /* edits a repeatable event, with one recurrence, with ALL-IGNORE-RECURRENCES selection.
     * Only edits the repeatable event.
     */
    @Test
    public void canProcessPublishReplaceRepeatableAllIgnoreRecurrences()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule) null)
                .withRecurrenceId(LocalDateTime.of(2015, 11, 12, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 12, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 12, 9, 30));
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentEdited, vComponentRecurrence));
        mainVCalendar.setVEvents(vComponents);
                
        // Publish change to ALL VEvents (recurrence gets deleted)
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              // PARENT
              "BEGIN:VEVENT" + System.lineSeparator() +
              "CATEGORIES:group05" + System.lineSeparator() +
              "DTSTART:20151109T090000" + System.lineSeparator() +
              "DTEND:20151109T103000" + System.lineSeparator() +
              "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
              "SUMMARY:Edited summary" + System.lineSeparator() +
              "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
              "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
              "RRULE:FREQ=DAILY" + System.lineSeparator() +
              "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
              "SEQUENCE:1" + System.lineSeparator() +
              "END:VEVENT" + System.lineSeparator() + 
              // RECURRENCE CHILD
              "BEGIN:VEVENT" + System.lineSeparator() +
              "CATEGORIES:group05" + System.lineSeparator() +
              "DTSTART:20151112T083000" + System.lineSeparator() +
              "DTEND:20151112T093000" + System.lineSeparator() +
              "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
              "SUMMARY:recurrence summary" + System.lineSeparator() +
              "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
              "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
              "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
              "RECURRENCE-ID:20151112T090000" + System.lineSeparator() +
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        
        mainVCalendar.processITIPMessage(VCalendar.parse(publish));
        
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.DTSTART_COMPARATOR);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withSequence(1)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, vComponents.get(0));
        VEvent vComponentRecurrence2 = new VEvent(vComponentRecurrence)
                .withRecurrenceId(LocalDateTime.of(2015, 11, 12, 9, 0));
        assertEquals(vComponentRecurrence2, vComponents.get(1));
    }
   
    @Test // divides one repeatable event into two.  First one ends with UNTIL
    public void canEditThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "DTEND:20160516T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T173109Z" + System.lineSeparator() +
                "UID:20160914T103109-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515T170000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.DTSTART_COMPARATOR);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentFuture.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentFuture.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponentFuture.getSummary().getValue());
        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentOriginal.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentOriginal.getDateTimeEnd().getValue()); 
        Temporal until = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        RecurrenceRuleValue expectedRRule = ICalendarStaticComponents.getDaily1().getRecurrenceRule().getValue().withUntil(until);
        assertEquals(expectedRRule, myComponentOriginal.getRecurrenceRule().getValue());
    }
    
    @Test // change INTERVAL
    public void canEditThisAndFuture2()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "DTEND:20160516T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T180627Z" + System.lineSeparator() +
                "UID:20160914T110627-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=2" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515T170000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
                
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.DTSTART_COMPARATOR);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentFuture.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentFuture.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponentFuture.getSummary().getValue());
        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentOriginal.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentOriginal.getDateTimeEnd().getValue()); 
        Temporal until = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        RecurrenceRuleValue expectedRRule = ICalendarStaticComponents.getDaily1().getRecurrenceRule().getValue().withUntil(until);
        assertEquals(expectedRRule, myComponentOriginal.getRecurrenceRule().getValue());
    }
    
    @Test
    public void canAddRRuleToAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividualZoned();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "CATEGORIES:group13" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20151113T090000" + System.lineSeparator() + // one hour earlier
                "DTEND;TZID=Europe/London:20151113T100000" + System.lineSeparator() + // one hour earlier
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-009@jfxtras.org" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR" + System.lineSeparator() + // added RRULE
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 9, 0), ZoneId.of("Europe/London")), myComponent.getDateTimeStart().getValue());        
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Europe/London")), myComponent.getDateTimeEnd().getValue());
        RecurrenceRuleValue r = new RecurrenceRuleValue()
                .withFrequency(FrequencyType.WEEKLY)
                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertEquals(r, myComponent.getRecurrenceRule().getValue());
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    }
