/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

/**
 * Tests displaying edit popups from Agenda.
 *
 * @author David Bal
 */
@Ignore // fails
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
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
        	agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
        	agenda.refresh();
        });

        // Open edit popup
        moveTo("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        Node n = find("#editDisplayableTabPane");
//        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
        clickOn("#cancelComponentButton");
    }
    
    @Test
    public void canProduceEditPopupFromExistingAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
        	agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
        	agenda.refresh();
        });
        
        // Open select one popup
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        
        // click on advanced edit
        clickOn("#OneAppointmentSelectedEditButton");
        Node n = find("#editDisplayableTabPane");
//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
        clickOn("#cancelComponentButton");
    }
    
    @Test
    public void canProduceEditPopupFromNewAppointment()
    {
        // Draw new appointment
        moveTo("#hourLine11");
        press(MouseButton.PRIMARY);
        moveTo("#hourLine12");
        release(MouseButton.PRIMARY);
        
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
        
        // click on advanced edit
        clickOn("#newAppointmentEditButton");
        Node n = find("#editDisplayableTabPane");
//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
        clickOn("#cancelComponentButton");
    }

    @Test
    public void canToggleRepeatableCheckBox()
    {
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
        	agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
        	agenda.refresh();
        });

        // Open edit popup
        moveTo("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        clickOn("#recurrenceRuleTab");

        // Get properties        
        CheckBox repeatableCheckBox = find("#repeatableCheckBox");

        // Check initial state
        assertTrue(repeatableCheckBox.isSelected());

        // Remove RRULE and verify state change
        TestUtil.runThenWaitForPaintPulse( () -> repeatableCheckBox.setSelected(false));
        clickOn("#saveRepeatButton");
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent v = agenda.getVCalendar().getVEvents().get(0);
        assertTrue(v.getRecurrenceRule() == null);
    }
}
