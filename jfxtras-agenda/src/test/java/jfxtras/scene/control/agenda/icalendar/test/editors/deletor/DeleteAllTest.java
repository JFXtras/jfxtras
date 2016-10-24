package jfxtras.scene.control.agenda.icalendar.test.editors.deletor;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
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

public class DeleteAllTest
{
    @Test
    public void canDeleteRepeatableAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withStartOriginalRecurrence(null)
                .delete();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20151109T100000" + System.lineSeparator() +
//                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
            String iTIPMessage = iTIPmessages.stream()
                    .map(v -> v.toContent())
                    .collect(Collectors.joining(System.lineSeparator()));
            System.out.println(expectediTIPMessage);
            assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // delete parent with recurrence children
    public void canDeleteAllWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withStartOriginalRecurrence(null)
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
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
            String iTIPMessage = iTIPmessages.stream()
                    .map(v -> v.toContent())
                    .collect(Collectors.joining(System.lineSeparator()));
            assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
