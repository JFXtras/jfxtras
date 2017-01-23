/**
 * AgendaMenuTest.java
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

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

/**
 * 
 */
public class AgendaMenuTest extends AbstractAgendaTestBase {

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
	public void showAppointmentMenu()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		clickOn("#AppointmentRegularBodyPane2014-01-01/0 .MenuIcon");
		assertPopupIsVisible(find("#AppointmentRegularBodyPane2014-01-01/0"));
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void showAppointmentMenuRMB()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		clickOn("#AppointmentRegularBodyPane2014-01-01/0", MouseButton.SECONDARY);
		assertPopupIsVisible(find("#AppointmentRegularBodyPane2014-01-01/0"));
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void deleteAppointmentByMenu()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		Assert.assertEquals(1, agenda.appointments().size() );
		clickOn("#AppointmentRegularBodyPane2014-01-01/0 .MenuIcon");
		clickOn(".delete-icon");
		Assert.assertEquals(0, agenda.appointments().size() );
		//TestUtil.sleep(3000);
	}

//	/**
//	 *  
//	 */
//	@Test
//	public void deleteAppointmentByDeleteKey()
//	{
//		TestUtil.runThenWaitForPaintPulse( () -> {
//			agenda.appointments().add( new Agenda.AppointmentImpl2()
//	            .withStartDateTime(TestUtil.quickParseLocalDateTime("2014-01-01T10:00"))
//	            .withEndDateTime(TestUtil.quickParseLocalDateTime("2014-01-01T12:00"))
//	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
//            );
//		});
//				
//		Assert.assertEquals(1, agenda.appointments().size() );
//		move("#AppointmentRegularBodyPane2014-01-01/0"); 
//		press(KeyCode.DELETE);
//		Assert.assertEquals(0, agenda.appointments().size() );
//		TestUtil.sleep(3000);
//	}

	/**
	 * 
	 */
	@Test
	public void toggleWholedayInMenu()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});

		clickOn("#AppointmentRegularBodyPane2014-01-01/0 .MenuIcon");
		clickOn("#wholeday-checkbox");
		clickOn(".close-icon");
		Assert.assertEquals(1, appointmentChangedCallbackList.size());
		Assert.assertTrue(appointmentChangedCallbackList.contains(agenda.appointments().get(0)));
		Assert.assertEquals(1, agenda.appointments().size() );
		Assert.assertEquals(true, agenda.appointments().get(0).isWholeDay().booleanValue() );

		// this not only checks if the appointment was changed, but also if it was rerendered
		{
			Node n = (Node)find("#AppointmentWholedayBodyPane2014-01-01/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
			new AssertNode(n).assertXYWH(0.5, 0.0, 5.0, 1006.125, 0.01);
		}
		{
			Node n = (Node)find("#AppointmentWholedayHeaderPane2014-01-01/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
			new AssertNode(n).assertXYWH(0.0, 24.0390625, 135.21763392857142, 20.9609375, 0.01);
		}
		//TestUtil.sleep(3000);
	}
}
