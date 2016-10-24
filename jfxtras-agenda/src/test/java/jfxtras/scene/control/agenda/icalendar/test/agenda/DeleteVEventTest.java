package jfxtras.scene.control.agenda.icalendar.test.agenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

public class DeleteVEventTest extends AgendaTestAbstract
{
    @Test
    public void canDeleteOne()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1());
        });
        
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        click("#OneAppointmentSelectedDeleteButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ONE);
        });
        click("#changeDialogOkButton");
        assertEquals(5, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 12, 10, 0),
                LocalDateTime.of(2015, 11, 13, 10, 0),
                LocalDateTime.of(2015, 11, 14, 10, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        assertEquals(1, agenda.getVCalendar().getAllVComponents().size());
        VEvent expectedVEvent = ICalendarStaticComponents.getDaily1()
                .withExceptionDates("20151111T100000")
                .withSequence(1);
        VEvent editedVEvent = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent, editedVEvent);
    }
    
    @Test
    public void canDeleteOneWithDeleteKey()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1());
        });
        
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        click("Cancel");
        press(KeyCode.DELETE);
        release(KeyCode.DELETE);
        
        assertEquals(5, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 12, 10, 0),
                LocalDateTime.of(2015, 11, 13, 10, 0),
                LocalDateTime.of(2015, 11, 14, 10, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        assertEquals(1, agenda.getVCalendar().getAllVComponents().size());
        VEvent expectedVEvent = ICalendarStaticComponents.getDaily1()
                .withExceptionDates("20151111T100000")
                .withSequence(1);
        VEvent editedVEvent = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent, editedVEvent);
    }
    
    @Test
    public void canDeleteAll()
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1());
        });
        
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        click("#OneAppointmentSelectedDeleteButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ALL);
        });
        click("#changeDialogOkButton");
        
        assertEquals(0, agenda.appointments().size());
        assertEquals(0, agenda.getVCalendar().getAllVComponents().size());
    }
}
