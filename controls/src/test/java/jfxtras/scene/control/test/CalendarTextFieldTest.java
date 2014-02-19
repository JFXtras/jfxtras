/**
 * CalendarTextFieldTest.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

package jfxtras.scene.control.test;

import java.util.Calendar;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;
import jfxtras.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.exceptions.NoNodesFoundException;

/**
 * Created by tbee on 26-12-13.
 */
public class CalendarTextFieldTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		VBox box = new VBox();
		calendarTextField = new CalendarTextField();
		box.getChildren().add(calendarTextField);

		return box;
	}
	private CalendarTextField calendarTextField = null;

	/**
	 * 
	 */
	@Test
	public void defaultModeIsNull()
	{
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
		
		// open the popup
		click(".icon");
		
		// click the last day in the first week (this is always clickable)
		click(".today");
		
		// now should be the value in the textfield
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void openPopupAndCloseOnEscape()
	{
		// popup should be closed
//		assertPopupIsNotVisible();
		
		// open the popup
		click(".icon");
		
		// popup should be open
		Assert.assertNotNull(find("#day6"));
		
		// send esc
		press(KeyCode.ESCAPE);
		
		// popup should be closed
//		assertPopupIsNotVisible();
	}
	
	private void assertPopupIsNotVisible() {
		TestUtil.sleep(2000);
		PlatformUtil.waitForPaintPulse();
		// popup should be closed
		try {
			Node n = find("#day6"); // last day of first week is always enabled
			System.out.println(">" + n.isVisible());
			Assert.assertTrue("Exception expected", false);
		}
		catch (NoNodesFoundException e) {
			// this is expected
		}
	}
}
