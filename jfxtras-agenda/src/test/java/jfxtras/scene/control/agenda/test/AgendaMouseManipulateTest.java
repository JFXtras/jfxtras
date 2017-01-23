/**
 * AgendaMouseManipulateTest.java
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

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.test.TestUtil;

/**
 * 
 */	
public class AgendaMouseManipulateTest extends AbstractAgendaTestBase {

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
	public void createAppointmentByDragging()
	{
		Assert.assertEquals(0, agenda.appointments().size() );
		
		moveTo("#hourLine10");
		press(MouseButton.PRIMARY);
		moveTo("#hourLine12");
		release(MouseButton.PRIMARY);
		
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-01T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		
		find("#AppointmentRegularBodyPane2014-01-01/0"); // validate that the pane has the expected id
		//TestUtil.sleep(3000);
	}
	
	/**
	 * 
	 */
	@Test
	public void createWholedayAppointmentByClicking()
	{
		Assert.assertEquals(0, agenda.appointments().size() );
		
		moveTo("#DayHeader2014-01-01");
		press(MouseButton.PRIMARY);
		release(MouseButton.PRIMARY);
		
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T00:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-02T00:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		
		assertFind("#AppointmentWholedayBodyPane2014-01-01/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-01/0");
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void dragRegularAppointment()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		assertFind("#AppointmentRegularBodyPane2014-01-01/0"); 
				
		moveTo("#hourLine11"); // the pane is beneath the mouse now since it runs from 10 to 12
		press(MouseButton.PRIMARY);
		moveTo("#hourLine15");
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));
		
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T14:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-01T16:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		//TestUtil.sleep(3000);
	}
	
	/**
	 * 
	 */
	@Test
	public void dragRegularAppointmentToNextDay()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		assertFind("#AppointmentRegularBodyPane2014-01-01/0");
				
		moveTo("#hourLine11"); // the pane is beneath the mouse now since it runs from 10 to 12
		press(MouseButton.PRIMARY);
		moveBy(100, 0);
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));

		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-02T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-02T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void extendAppointmentByDragging()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		moveTo("#AppointmentRegularBodyPane2014-01-01/0 .DurationDragger"); 
		press(MouseButton.PRIMARY);
		moveTo("#hourLine15");
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));

		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-01T15:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		//TestUtil.sleep(3000);
	}


	/**
	 * 
	 */
	@Test
	public void dragWholeDayToOutside()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withWholeDay(true)
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		
		// make sure the two nodes exist 
		assertFind("#AppointmentWholedayBodyPane2014-01-01/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-01/0");
		
		moveTo("#AppointmentWholedayBodyPane2014-01-01/0"); 
		press(MouseButton.PRIMARY);
		moveTo(0, 0);
		release(MouseButton.PRIMARY);
		
		// nothing changed 
		Assert.assertEquals(1, agenda.appointments().size() );
		assertFind("#AppointmentWholedayBodyPane2014-01-01/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-01/0");
		// TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void dragWholeDayToBody()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T00:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withWholeDay(true)
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		
		// make sure the two nodes exist 
		assertFind("#AppointmentWholedayBodyPane2014-01-01/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-01/0");
		
		// drag from header to body
		moveTo("#AppointmentWholedayHeaderPane2014-01-01/0"); 
		press(MouseButton.PRIMARY);
		moveTo("#hourLine10");
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));

		// now there should be a regular appointment
		System.out.println(agenda.appointments());
		assertFind("#AppointmentRegularBodyPane2014-01-01/0");
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-01T11:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void dragWholeDayTaskToBody()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T00:00"))
	            .withWholeDay(true)
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		
		// make sure the two nodes exist 
		assertFind("#AppointmentWholedayBodyPane2014-01-01/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-01/0");
		
		// drag from header to body
		moveTo("#AppointmentWholedayHeaderPane2014-01-01/0"); 
		press(MouseButton.PRIMARY);
		moveTo("#hourLine10");
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));
		
		// now there should be a regular appointment
		assertFind("#AppointmentTaskBodyPane2014-01-01/0");
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertNull(agenda.appointments().get(0).getEndLocalDateTime());
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void dragRegularToHeader()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T01:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		
		// make sure the node exist 
		assertFind("#AppointmentRegularBodyPane2014-01-01/0");
		
		// drag from header to body
		moveTo("#AppointmentRegularBodyPane2014-01-01/0"); 
		press(MouseButton.PRIMARY);
		moveTo("#DayHeader2014-01-02"); // header of next day
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));
		
		// now there should be a regular appointment
		assertFind("#AppointmentWholedayBodyPane2014-01-02/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-02/0");
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-02T01:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertTrue(agenda.appointments().get(0).isWholeDay());
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void dragTaskToHeader()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T01:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
		
		// make sure the node exist 
		assertFind("#AppointmentTaskBodyPane2014-01-01/0");
		
		// drag from header to body
		moveTo("#AppointmentTaskBodyPane2014-01-01/0"); 
		press(MouseButton.PRIMARY);
		moveTo("#DayHeader2014-01-02"); // header of next day
		release(MouseButton.PRIMARY);
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));

		// now there should be a regular appointment
		assertFind("#AppointmentWholedayBodyPane2014-01-02/0");
		assertFind("#AppointmentWholedayHeaderPane2014-01-02/0");
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-02T01:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertTrue(agenda.appointments().get(0).isWholeDay());
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void attemptExtendAppointmentByDraggingWhenNotAllowed()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.withAllowResize(false);
		});
				
		assertNotFind("#AppointmentRegularBodyPane2014-01-01/0 .DurationDragger"); 
		//TestUtil.sleep(3000);
	}
	
	/**
	 * 
	 */
	@Test
	public void attemptDragRegularAppointmentWhenNotAllowed()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.withAllowDragging(false);
		});
		assertFind("#AppointmentRegularBodyPane2014-01-01/0"); 
				
		moveTo("#hourLine11"); // the pane is beneath the mouse now since it runs from 10 to 12
		press(MouseButton.PRIMARY);
		moveTo("#hourLine15");
		release(MouseButton.PRIMARY);
		
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
		Assert.assertEquals("2014-01-01T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
		//TestUtil.sleep(3000);
	}
}
