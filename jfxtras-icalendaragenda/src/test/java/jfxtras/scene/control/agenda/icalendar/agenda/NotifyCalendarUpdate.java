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

import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

@Ignore // fails
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
