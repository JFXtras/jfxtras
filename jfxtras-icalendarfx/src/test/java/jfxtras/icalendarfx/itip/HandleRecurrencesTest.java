package jfxtras.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;

public class HandleRecurrencesTest
{
    @Test // edit ALL with 2 recurrences in date range
    public void canEditAllWithRecurrences()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrences
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);
        
        VEvent vComponentRecurrence2 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 10, 0))
                .withSummary("recurrence summary2")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 19, 7, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 19, 8, 30));
        vComponents.add(vComponentRecurrence2);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T090000" + System.lineSeparator() +
                "DTEND:20151109T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160519T073000" + System.lineSeparator() +
                "DTEND:20160519T083000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary2" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160519T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
        assertEquals(1, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedVComponent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0));
        expectedVComponent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, myComponent1);
    }
    
    /* edits a repeatable event, with one recurrence, with ALL selection.
     * The edit deletes the recurrence and edits the repeatable event.
     */
    @Test
    public void canProcessPublishReplaceRepeatableAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        vComponents.addAll(vComponentEdited, vComponentRecurrence);
//        System.out.println(mainVCalendar);
        // Publish change to ALL VEvents (recurrence gets deleted)
        String publish = "BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
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
              "END:VCALENDAR";
        String cancel = "BEGIN:VCALENDAR" + System.lineSeparator() + 
                "METHOD:CANCEL" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR";
        
        mainVCalendar.processITIPMessage(VCalendar.parse(publish));
        mainVCalendar.processITIPMessage(VCalendar.parse(cancel));
        
        VCalendar expectedVCalendar = new VCalendar();        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withSequence(1)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        expectedVCalendar.addVComponent(expectedVComponent);
        assertEquals(expectedVCalendar, mainVCalendar);
    }
        
    @Test // edit ALL and ignore 2 recurrences in date range - tests changing Recurrence-ID of recurrences to match parent's change
    public void canEditAllIgnoreRecurrences()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrences
        VEvent vComponentRecurrence2 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence2);
        
        VEvent vComponentRecurrence3 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 10, 0))
                .withSummary("recurrence summary2")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 19, 7, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 19, 8, 30));
        vComponents.add(vComponentRecurrence3);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T090000" + System.lineSeparator() +
                "DTEND:20151109T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T090000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160519T073000" + System.lineSeparator() +
                "DTEND:20160519T083000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary2" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160519T090000" + System.lineSeparator() +
                "END:VEVENT";
        mainVCalendar.processITIPMessage(iTIPMessage);

        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        assertEquals(3, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        VEvent myComponent2 = vComponents.get(1);
        VEvent myComponent3 = vComponents.get(2);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedVComponent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0));
        expectedVComponent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, myComponent1);
        
        VEvent expectedComponent2 = new VEvent(vComponentRecurrence2)
                .withSummary("recurrence summary")
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 9, 0))
                .withDateTimeStart("20160517T083000")
                .withDateTimeEnd("20160517T093000");
        assertEquals(expectedComponent2, myComponent2);
        
        VEvent expectedComponent3 = new VEvent(vComponentRecurrence3)
                .withSummary("recurrence summary2")
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 9, 0))
                .withDateTimeStart("20160519T073000")
                .withDateTimeEnd("20160519T083000");
        assertEquals(expectedComponent3, myComponent3);
    }
    
    @Test // with a recurrence in between new date range - remove special recurrence, replaces with normal recurrence
    public void canEditThisAndFutureWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
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
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
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
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(2, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDateTime.of(2016, 5, 15, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        assertEquals(expectedOriginalEdited, myComponentOriginal);
        
        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
        VEvent expectedComponentFuture = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 30))
                .withRelatedTo(FXCollections.observableArrayList(relatedTo))
                .withSummary("Edited summary")
                .withUniqueIdentifier(new UniqueIdentifier(myComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(myComponentFuture.getDateTimeStamp()));
        assertEquals(expectedComponentFuture, myComponentFuture);
    }
    
    @Test // with a recurrence in between new date range - special recurrence stays unmodified.
    public void canEditThisAndFutureAllIgnoreRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);

        // make recurrence before
        VEvent vComponentRecurrenceBefore = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2015, 12, 25, 10, 0))
                .withSummary("recurrence summary before")
                .withDateTimeStart(LocalDateTime.of(2015, 12, 26, 0, 30))
                .withDateTimeEnd(LocalDateTime.of(2015, 12, 26, 2, 30));
        vComponents.add(vComponentRecurrenceBefore);

        // make recurrence after
        VEvent vComponentRecurrenceAfter = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary after")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrenceAfter);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
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
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                // NOTE: Yahoo calendar deletes the recurrence before the new UNTIL date.  It may be necessary to include the before recurrences in the REQUEST message.
                "END:VCALENDAR" + System.lineSeparator() +
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
                "DTSTAMP:20160918T235541Z" + System.lineSeparator() +
                "UID:20160918T165541-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary after" + System.lineSeparator() +
                "DTSTAMP:20160918T235541Z" + System.lineSeparator() +
                "UID:20160918T165541-0jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T090000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        assertEquals(4, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponentOriginal = vComponents.get(0);
        // vComponent #1 is recurrence before (unchanged)
        VEvent myComponentFuture = vComponents.get(2);
        VEvent myComponentRecurrence = vComponents.get(3);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDateTime.of(2016, 5, 15, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        assertEquals(expectedOriginalEdited, myComponentOriginal);

        RelatedTo relatedTo = RelatedTo.parse(vComponentOriginal.getUniqueIdentifier().getValue());
        VEvent expectedComponentFuture = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 30))
                .withRelatedTo(FXCollections.observableArrayList(relatedTo))
                .withSummary("Edited summary")
                .withUniqueIdentifier(new UniqueIdentifier(myComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(myComponentFuture.getDateTimeStamp()));
        assertEquals(expectedComponentFuture, myComponentFuture);

        VEvent expectedvComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 9, 0))
                .withSummary("recurrence summary after")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30))
                .withDateTimeStamp(new DateTimeStamp(myComponentFuture.getDateTimeStamp()))
                .withUniqueIdentifier(new UniqueIdentifier(expectedComponentFuture.getUniqueIdentifier()));
        assertEquals(expectedvComponentRecurrence, myComponentRecurrence);
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
        VEvent vComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDate.of(2016, 5, 17))
                .withSummary("recurrence summary")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")));
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
                "DTSTAMP:20160919T032343Z" + System.lineSeparator() +
                "UID:20160918T202343-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160515T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160515T103000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160919T032343Z" + System.lineSeparator() +
                "UID:20160918T202343-0jfxtras.org" + System.lineSeparator() +
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
        assertEquals(expectedComponentFuture, newVComponentFuture);

        VEvent expectedvComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1()
                .withUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()))
                .withDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp())) // TODO - Decide if I should recycle old DTSTAMP?
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 0), ZoneId.of("Europe/London")))
                .withSummary("recurrence summary")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")))
                .withUniqueIdentifier(new UniqueIdentifier(expectedComponentFuture.getUniqueIdentifier()));
        assertEquals(expectedvComponentRecurrence, myComponentRecurrence);
    }
    
    @Test // makes sure when recurrence deleted the parent gets an EXDATE
    public void canDeleteThisAndFutureWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "RECURRENCE-ID;RANGE=THISANDFUTURE:20160515T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
        assertEquals(1, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        RecurrenceRule2 newRRule = new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY)
                .withUntil(LocalDateTime.of(2016, 5, 14, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        expectedVComponent.setRecurrenceRule(newRRule);
        assertEquals(expectedVComponent, myComponent1);
    }
}
