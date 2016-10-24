package jfxtras.scene.control.agenda.icalendar.test.agenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

/**
 * Tests displaying edit popups from Agenda.
 *
 * @author David Bal
 */
public class VEventDisplayPopupTest extends AgendaTestAbstract
{
    @Override
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    public void canProduceEditPopup()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1()));

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        Node n = find("#editDisplayableTabPane");
//        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
        click("#cancelComponentButton");
    }
    
    @Test
    public void canProduceEditPopupFromExistingAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1()));

        // Open select one popup
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        // click on advanced edit
        click("#OneAppointmentSelectedEditButton");
        Node n = find("#editDisplayableTabPane");
//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
        click("#cancelComponentButton");
    }
    
    @Test
    public void canProduceEditPopupFromNewAppointment()
    {
        // Draw new appointment
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
        
        // click on advanced edit
        click("#newAppointmentEditButton");
        Node n = find("#editDisplayableTabPane");
//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
        click("#cancelComponentButton");
    }

    @Test
    public void canToggleRepeatableCheckBox()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1()));

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        click("#recurrenceRuleTab");

        // Get properties        
        CheckBox repeatableCheckBox = find("#repeatableCheckBox");

        // Check initial state
        assertTrue(repeatableCheckBox.isSelected());
        
        // Remove RRULE and verify state change
        TestUtil.runThenWaitForPaintPulse( () -> repeatableCheckBox.setSelected(false));
        click("#saveRepeatButton");
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent v = agenda.getVCalendar().getVEvents().get(0);
        assertTrue(v.getRecurrenceRule() == null);
    }
}
