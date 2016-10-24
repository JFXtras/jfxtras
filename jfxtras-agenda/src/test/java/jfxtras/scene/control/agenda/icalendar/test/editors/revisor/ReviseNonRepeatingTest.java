package jfxtras.scene.control.agenda.icalendar.test.editors.revisor;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.ReviserVEvent;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;

public class ReviseNonRepeatingTest
{
    @Test
    public void canEditIndividual()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponents.add(vComponentEdited);

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160516T113000" + System.lineSeparator() +
                "DURATION:PT1H" + System.lineSeparator() +
                "DESCRIPTION:Individual Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-007@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canEditIndividual2() // with other components present
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponents.add(vComponentEdited);
        vComponents.add(ICalendarStaticComponents.getDaily1());

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160516T113000" + System.lineSeparator() +
                "DURATION:PT1H" + System.lineSeparator() +
                "DESCRIPTION:Individual Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-007@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canEditIndividual3() // delete property
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponents.add(vComponentEdited);

        vComponentEdited.setSummary((Summary) null);
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160516T113000" + System.lineSeparator() +
                "DURATION:PT1H" + System.lineSeparator() +
                "DESCRIPTION:Individual Description" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-007@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
