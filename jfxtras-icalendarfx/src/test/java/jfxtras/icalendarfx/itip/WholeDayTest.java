package jfxtras.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;

public class WholeDayTest
{
    @Test
    public void canChangeToWholeDayAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(LocalDate.of(2015, 11, 8), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDate.of(2015, 11, 9), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test
    public void canChangOneWholeDayToTimeBased()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20160516" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20160517" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T180627Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(2, vComponents.size());
        // TODO - CHECK COMPONENTS
    }
    
    @Test
    public void canChangeWholeDayToTimeBasedThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        vComponents.add(vComponentEdited);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND:20160516T100000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);


        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        VEvent newVComponentOriginal = vComponents.get(0);
        VEvent newVComponentFuture = vComponents.get(1);

        VEvent expectedVComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        RecurrenceRule2 rrule = expectedVComponentOriginal.getRecurrenceRule().getValue();
        rrule.setUntil(LocalDate.of(2016, 5, 15));
        
        assertEquals(expectedVComponentOriginal, newVComponentOriginal);

        VEvent expectedVComponentFuture = ICalendarStaticComponents.getWholeDayDaily1();
        expectedVComponentFuture.setDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0));
        expectedVComponentFuture.setDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 0));
        expectedVComponentFuture.setUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()));
        expectedVComponentFuture.setDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp()));
        expectedVComponentFuture.setSummary("Edited summary");
        expectedVComponentFuture.withRelatedTo(vComponentOriginal.getUniqueIdentifier().getValue());
        expectedVComponentFuture.setSequence(1);
        
        assertEquals(expectedVComponentFuture, newVComponentFuture);
    }
    
    @Test // with a recurrence in between new date range, from whole-day to time-based - special recurrence stays unmodified.
    public void canEditWholeDayToTimeBasedThisAndFutureIgnoreRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDate.of(2016, 5, 17));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")));
        vComponentRecurrence.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")));
        vComponents.add(vComponentRecurrence);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160514" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160515T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160515T103000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160517T083000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160517T093000" + System.lineSeparator() +
                "RECURRENCE-ID;TZID=Europe/London:20160517T090000" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
        assertEquals(3, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponentOriginal = vComponents.get(0);
        VEvent newVComponentFuture = vComponents.get(1);
        VEvent myComponentRecurrence = vComponents.get(2);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getWholeDayDaily1()
                .withSequence(1);
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDate.of(2016, 5, 14));
        assertEquals(expectedOriginalEdited, myComponentOriginal);

        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
        VEvent expectedComponentFuture = ICalendarStaticComponents.getWholeDayDaily1()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 9, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 30), ZoneId.of("Europe/London")))
                .withSummary("Edited summary")
                .withRelatedTo(FXCollections.observableArrayList(relatedTo))
                .withUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp()));

        VEvent expectedvComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 0), ZoneId.of("Europe/London")))
                .withSummary("recurrence summary")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")))
                .withUniqueIdentifier(new UniqueIdentifier(expectedComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(expectedComponentFuture.getDateTimeStamp()));
        assertEquals(expectedvComponentRecurrence, myComponentRecurrence);
    }
}
