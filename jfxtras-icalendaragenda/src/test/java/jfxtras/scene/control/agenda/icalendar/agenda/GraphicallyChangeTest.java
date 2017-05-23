package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

public class GraphicallyChangeTest extends AgendaTestAbstract
{
    @Test
    public void canMoveIndividual()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getIndividual1());
            agenda.refresh();
        });
        
        // drag to new location
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine9");
        release(MouseButton.PRIMARY);
        
        // check appointment
        assertEquals(1, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 8, 30)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        // check VEvent
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent editedVEvent = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent = ICalendarStaticComponents.getIndividual1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 8, 30))
                .withDateTimeStamp(editedVEvent.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedVEvent, editedVEvent);
    }
    
    @Test
    public void canMoveOneOfRepeatable()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        // drag to new location
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine9");
        release(MouseButton.PRIMARY);
        
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ONE);
        });
        clickOn("#changeDialogOkButton");
        
        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 11, 8, 0),
                LocalDateTime.of(2015, 11, 12, 10, 0),
                LocalDateTime.of(2015, 11, 13, 10, 0),
                LocalDateTime.of(2015, 11, 14, 10, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .sorted(DateTimeUtilities.TEMPORAL_COMPARATOR2)
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        // check VEvent
        Collections.sort(agenda.getVCalendar().getVEvents(), VPrimary.DTSTART_COMPARATOR);
        assertEquals(2, agenda.getVCalendar().getVEvents().size());
        VEvent expectedVEvent0 = ICalendarStaticComponents.getDaily1();
        VEvent editedVEvent0 = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent0, editedVEvent0);

        VEvent editedVEvent1 = agenda.getVCalendar().getVEvents().get(1);
        VEvent expectedVEvent1 = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 9, 0))
                .withDateTimeStamp(new DateTimeStamp(editedVEvent1.getDateTimeStamp())) // copy DTSTAMP, because it depends on current date/time
                .withRecurrenceRule((RecurrenceRule) null)
                .withRecurrenceId(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withSequence(1);
        assertEquals(expectedVEvent1, editedVEvent1);
        
        // check DTSTAMP
        String dtstamp = editedVEvent1.getDateTimeStamp().toString();
        String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toString();
        assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time
    }
    
    @Test
    public void canMoveAllOfRepeatable()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        // drag to new location
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine9");
        release(MouseButton.PRIMARY);
        
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ALL);
        });
        clickOn("#changeDialogOkButton");
        
        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 8, 0),
                LocalDateTime.of(2015, 11, 10, 8, 0),
                LocalDateTime.of(2015, 11, 11, 8, 0),
                LocalDateTime.of(2015, 11, 12, 8, 0),
                LocalDateTime.of(2015, 11, 13, 8, 0),
                LocalDateTime.of(2015, 11, 14, 8, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .sorted(DateTimeUtilities.TEMPORAL_COMPARATOR2)
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        // check VEvent
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent editedVEvent0 = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent0 = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeStamp(editedVEvent0.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedVEvent0, editedVEvent0);
    }
    
    @Test
    public void canMoveThisAndFutureOfRepeatable()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        // drag to new location
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine9");
        release(MouseButton.PRIMARY);
        
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.THIS_AND_FUTURE);
        });
        clickOn("#changeDialogOkButton");

        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 11, 8, 0),
                LocalDateTime.of(2015, 11, 12, 8, 0),
                LocalDateTime.of(2015, 11, 13, 8, 0),
                LocalDateTime.of(2015, 11, 14, 8, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .sorted(DateTimeUtilities.TEMPORAL_COMPARATOR2)
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        // check VEvent
        Collections.sort(agenda.getVCalendar().getVEvents(), VPrimary.DTSTART_COMPARATOR);
        assertEquals(2, agenda.getVCalendar().getVEvents().size());
        VEvent expectedVEvent0 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"))))
                .withSequence(1);
        VEvent editedVEvent0 = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent0, editedVEvent0);

        VEvent editedVEvent1 = agenda.getVCalendar().getVEvents().get(1);
        VEvent expectedVEvent1 = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 9, 0))
                .withDateTimeStamp(new DateTimeStamp(editedVEvent1.getDateTimeStamp())) // copy DTSTAMP, because it depends on current date/time
                .withUniqueIdentifier(new UniqueIdentifier(editedVEvent1.getUniqueIdentifier()))
                .withRelatedTo(editedVEvent0.getUniqueIdentifier().getValue());
        assertEquals(expectedVEvent1, editedVEvent1);
        
        // check DTSTAMP
        String dtstamp = editedVEvent1.getDateTimeStamp().toString();
        String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toString();
        assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time
    }
}
