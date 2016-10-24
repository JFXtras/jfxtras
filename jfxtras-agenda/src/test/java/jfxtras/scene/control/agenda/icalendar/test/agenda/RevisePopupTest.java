package jfxtras.scene.control.agenda.icalendar.test.agenda;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

public class RevisePopupTest extends AgendaTestAbstract
{
    @Test
    public void canEditOne()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1());
        });
        
        // drag to new location
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        // edit property
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        
        // save changes to THIS AND FUTURE
        click("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");
        
        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<String> summaries = agenda.appointments().stream()
                .sorted((a1, a2) -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime()))
                .map(a -> a.getSummary())
                .collect(Collectors.toList());
        List<String> expectedSummaries = Arrays.asList(
                "Daily1 Summary",
                "Daily1 Summary",
                "new summary",
                "Daily1 Summary",
                "Daily1 Summary",
                "Daily1 Summary"
                );
        assertEquals(expectedSummaries, summaries);
        assertEquals(2, agenda.getVCalendar().getVEvents().size());
        agenda.getVCalendar().getVEvents().sort(VPrimary.VPRIMARY_COMPARATOR);
        VEvent vComponentOriginal = agenda.getVCalendar().getVEvents().get(0);
        VEvent vComponentIndividual = agenda.getVCalendar().getVEvents().get(1);

        assertEquals(ICalendarStaticComponents.getDaily1(), vComponentOriginal);
        VEvent expectedVComponentIndividual = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart("20151111T100000")
                .withDateTimeEnd("20151111T110000")
                .withDateTimeStamp(new DateTimeStamp(vComponentIndividual.getDateTimeStamp()))
                .withRecurrenceId("20151111T100000")
                .withRecurrenceRule((RecurrenceRule) null)
                .withSummary("new summary")
                .withSequence(1);
        assertEquals(expectedVComponentIndividual, vComponentIndividual);
    }
}
