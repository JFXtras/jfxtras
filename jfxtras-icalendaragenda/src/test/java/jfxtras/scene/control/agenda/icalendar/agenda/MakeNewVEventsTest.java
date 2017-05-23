package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

public class MakeNewVEventsTest extends AgendaTestAbstract
{
    @Test
    public void canCreateSimpleVEvent()
    {
        // Draw new appointment
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine12");
        release(MouseButton.PRIMARY);
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id

        // find and edit properties in simple dialog
        TextField summaryTextField = (TextField) find("#summaryTextField");
        summaryTextField.setText("Edited summary");
        ComboBox<AppointmentGroup> appointmentGroupComboBox = find("#appointmentGroupComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> appointmentGroupComboBox.setValue(agenda.appointmentGroups().get(10)) );

        // create event
        clickOn("#newAppointmentCreateButton");
        
        // verify event's creation
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent = new VEvent()
                .withOrganizer(ICalendarAgenda.DEFAULT_ORGANIZER)
                .withSummary("Edited summary")
                .withCategories("group10")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 11, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 12, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeCreated(vEvent.getDateTimeCreated())
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withUniqueIdentifier(vEvent.getUniqueIdentifier())
                ;
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canCancelSimpleVEvent()
    {
        // Draw new appointment
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine12");
        release(MouseButton.PRIMARY);
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
        
        // create event
        clickOn("#newAppointmentCancelButton");
        
        // verify no event creation
        assertNull(agenda.getVCalendar().getVEvents());
        assertEquals(0, agenda.appointments().size());
        
        Node node = find("#AppointmentRegularBodyPane2015-11-11/0");
        assertNull(node);
    }

    // Cancel when other events exist
    @Test
    public void canCancelSimpleVEvent2()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getIndividual1());
            agenda.refresh();
        });
        
        // Draw new appointment
        moveTo("#hourLine5");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine6");
        release(MouseButton.PRIMARY);
        find("#AppointmentRegularBodyPane2015-11-5/0"); // validate that the pane has the expected id
        
        // create event
        clickOn("#newAppointmentCancelButton");
        
        // verify no event creation
        assertEquals(1, agenda.appointments().size());
        
        assertFind("#AppointmentRegularBodyPane2015-11-11/0");
        Node node = find("#AppointmentRegularBodyPane2015-11-5/0");
        assertNull(node);
    }
    
    @Test
    public void canCreateAdvancedVEvent()
    {
        // Draw new appointment
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine12");
        release(MouseButton.PRIMARY);
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
        
        // do advanced edit
        clickOn("#newAppointmentEditButton");
        TextField locationTextField = find("#locationTextField");
        locationTextField.setText("new location");
        clickOn("#recurrenceRuleTab");
        clickOn("#repeatableCheckBox");
        clickOn("#saveRepeatButton");

        // verify event's creation
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
//        System.out.println("old vEvent:" + System.identityHashCode(vEvent));
        VEvent expectedVEvent = new VEvent()
                .withOrganizer(ICalendarAgenda.DEFAULT_ORGANIZER)
                .withSummary("New")
                .withCategories("group00")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 11, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 12, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeCreated(vEvent.getDateTimeCreated())
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withUniqueIdentifier(vEvent.getUniqueIdentifier())
                .withLocation("new location")
                .withRecurrenceRule("RRULE:FREQ=WEEKLY;BYDAY=WE")
                ;
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canCreateSimpleWholeDayVEvent()
    {
        // Draw new appointment
        moveTo("#DayHeader2015-11-12");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        assertFind("#AppointmentWholedayBodyPane2015-11-12/0");
        assertFind("#AppointmentWholedayHeaderPane2015-11-12/0");
        
        // create event
        clickOn("#newAppointmentCreateButton");
        
        // verify event's creation
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent = new VEvent()
                .withOrganizer(ICalendarAgenda.DEFAULT_ORGANIZER)
                .withSummary("New")
                .withCategories("group00")
                .withDateTimeStart(LocalDate.of(2015, 11, 12))
                .withDateTimeEnd(LocalDate.of(2015, 11, 13))
                .withDateTimeCreated(vEvent.getDateTimeCreated())
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withUniqueIdentifier(vEvent.getUniqueIdentifier())
                ;
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canDragWholeDayToTimeBasedVEvent()
    {
        // Draw new appointment
        moveTo("#DayHeader2015-11-12");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        assertFind("#AppointmentWholedayBodyPane2015-11-12/0");
        assertFind("#AppointmentWholedayHeaderPane2015-11-12/0");
        
        // create event
        clickOn("#newAppointmentCreateButton");

        moveTo("#AppointmentWholedayHeaderPane2015-11-12/0"); 
        press(MouseButton.PRIMARY);
        moveTo("#hourLine10");
        release(MouseButton.PRIMARY);
        
        // verify event's creation
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent = new VEvent()
                .withOrganizer(ICalendarAgenda.DEFAULT_ORGANIZER)
                .withSummary("New")
                .withCategories("group00")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0).atZone(ZoneId.systemDefault()))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 11, 0).atZone(ZoneId.systemDefault()))
                .withDateTimeCreated(vEvent.getDateTimeCreated())
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withUniqueIdentifier(vEvent.getUniqueIdentifier())
                .withSequence(1)
                ;
        assertEquals(expectedVEvent, vEvent);
    }
}
