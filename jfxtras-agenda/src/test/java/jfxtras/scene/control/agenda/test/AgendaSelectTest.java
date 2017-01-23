/**
 * AgendaSelectTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.scene.control.agenda.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.test.TestUtil;

/**
 * 
 */
public class AgendaSelectTest extends AbstractAgendaTestBase {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		return super.getRootNode();
	}

	/**
	 * 
	 */
	@Test
	public void selectSingle()
	{
		// given
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#AppointmentRegularBodyPane2014-01-01/0");
		
		// then
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#hourLine15");
		
		// then
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void selectShift()
	{
		// given
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T07:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T08:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
	        );
		});
		Assert.assertEquals(2, agenda.appointments().size() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#AppointmentRegularBodyPane2014-01-01/0"); // select first
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#AppointmentRegularBodyPane2014-01-01/1"); // select second
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// when
		press(KeyCode.SHIFT);
		clickOn("#AppointmentRegularBodyPane2014-01-01/0"); // select both
		release(KeyCode.SHIFT);
		Assert.assertEquals(2, agenda.selectedAppointments().size() );
		
		// when
		press(KeyCode.SHIFT);
		clickOn("#AppointmentRegularBodyPane2014-01-01/0"); // select again (no change)
		release(KeyCode.SHIFT);
		Assert.assertEquals(2, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#hourLine15");
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void selectControl()
	{
		// given
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T07:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T08:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
	        );
		});
		Assert.assertEquals(2, agenda.appointments().size() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#AppointmentRegularBodyPane2014-01-01/0"); // select first
		TestUtil.sleep(500); // TODO: this should not be needed!
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// when
		press(KeyCode.CONTROL);
		clickOn("#AppointmentRegularBodyPane2014-01-01/1"); // select second
		release(KeyCode.CONTROL);
		TestUtil.sleep(500); // TODO: this should not be needed!
		Assert.assertEquals(2, agenda.selectedAppointments().size() );
		
		// when
		press(KeyCode.CONTROL);
		clickOn("#AppointmentRegularBodyPane2014-01-01/1"); // select again (deselects)
		release(KeyCode.CONTROL);
		TestUtil.sleep(500); // TODO: this should not be needed!
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// when
		press(KeyCode.CONTROL);
		clickOn("#AppointmentRegularBodyPane2014-01-01/0"); // select again (deselects)
		release(KeyCode.CONTROL);
		TestUtil.sleep(500); // TODO: this should not be needed!
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void action()
	{
		// given
		AtomicInteger cnt = new AtomicInteger(0);
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.setActionCallback( (appointment) -> {
				cnt.incrementAndGet();
				return null;
			});
		});
		Assert.assertEquals(0, cnt.get() );
		
		// when
		move("#AppointmentRegularBodyPane2014-01-01/0"); 
		clickOn(MouseButton.PRIMARY); // single click
		clickOn(MouseButton.PRIMARY); // double click
		
		// then
		Assert.assertEquals(1, cnt.get() );
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void actionMMB()
	{
		// given
		AtomicInteger cnt = new AtomicInteger(0);
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.setActionCallback( (appointment) -> {
				cnt.incrementAndGet();
				return null;
			});
		});
		Assert.assertEquals(0, cnt.get() );
		
		// when
		move("#AppointmentRegularBodyPane2014-01-01/0"); 
		clickOn(MouseButton.MIDDLE); // one click of the middle button suffices
		
		// then
		Assert.assertEquals(1, cnt.get() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		
		// TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void selectSingleWithDragNotAllowed()
	{
		// given
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.withAllowDragging(false);
		});
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#AppointmentRegularBodyPane2014-01-01/0");
		
		// then
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(1, agenda.selectedAppointments().size() );
		
		// when
		clickOn("#hourLine15");
		
		// then
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(0, agenda.selectedAppointments().size() );
		//TestUtil.sleep(3000);
	}
}
