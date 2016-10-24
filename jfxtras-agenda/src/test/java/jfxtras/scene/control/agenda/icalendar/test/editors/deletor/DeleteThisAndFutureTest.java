package jfxtras.scene.control.agenda.icalendar.test.editors.deletor;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.DeleterVEvent;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.SimpleDeleterFactory;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;

public class DeleteThisAndFutureTest
{
    @Test
    public void canDeleteThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getWeeklyZoned();
        vComponents.add(vComponentOriginal);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withStartOriginalRecurrence(LocalDateTime.of(2016, 5, 16, 10, 0).atZone(ZoneId.of("America/Los_Angeles")))
                .delete();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTAMP:20151110T080000Z" + System.lineSeparator() +
                "DESCRIPTION:WeeklyZoned Description" + System.lineSeparator() +
                "SUMMARY:WeeklyZoned Summary" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "UID:20150110T080000-003@jfxtras.org" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "RECURRENCE-ID;TZID=America/Los_Angeles;RANGE=THISANDFUTURE:20160516T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
            String iTIPMessage = iTIPmessages.stream()
                    .map(v -> v.toContent())
                    .collect(Collectors.joining(System.lineSeparator()));
            assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // makes sure when recurrence deleted the parent gets an EXDATE
    public void canDeleteThisAndFutureWithRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 15, 10, 0);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponent1))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .delete();

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
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
        String iTIPMessage = iTIPmessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
