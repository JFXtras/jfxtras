package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.junit.Test;

import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

public class NotifyCalendarUpdate extends AgendaTestAbstract
{
	private int events = 0;
	private DateTimeStart newStart;;
    @Test
    public void canNotifyOfDrawnCalendarUpdate()
    {
    	events = 0;
        Consumer<VCalendar> calendarConsumer = v -> {
        	events = v.getVEvents().size();
        };
        agenda.setVCalendarUpdatedConsumer(calendarConsumer);
        
        // Draw new appointment
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine12");
        release(MouseButton.PRIMARY);
        // create event
        clickOn("#newAppointmentCreateButton");
        
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        assertEquals(1, events); // confirm event consumer fired
    }
    
    @Test
    public void canNotifyOfEditedCalendarUpdate()
    {
    	events = 0;
        Consumer<VCalendar> calendarConsumer = v -> {
        	events = v.getVEvents().size();
        	newStart = v.getVEvents().get(0).getDateTimeStart();
        };
        agenda.setVCalendarUpdatedConsumer(calendarConsumer);
        
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
        
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        assertEquals(1, events); // confirm event consumer fired
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 30), newStart);
    }

}
