package jfxtras.scene.control.agenda.icalendar.test.editors.revisor;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.Reviser;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.ReviserVEvent;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;

/**
 * Tests the {@link Reviser} ability to make iTIP publish messages to edit components.
 * 
 * Tests CANCEL functionality 
 * 
 * @author David Bal
 *
 */
public class CancelRevisionTest
{
    @Test
    public void canCancelEdit()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        
        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.CANCEL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        assertEquals(Collections.emptyList(), iTIPMessages);        
    }
}
