/**
 * CalendarPickerTest.java
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

package jfxtras.scene.control.test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class CalendarPickerNoInitTest extends JFXtrasGuiTest {
	
	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		calendarPicker = new CalendarPicker();
		box.getChildren().add(calendarPicker);

		// make sure there is enough room for the time sliders
		box.setPrefSize(300, 300);
		return box;
	}
	private CalendarPicker calendarPicker = null;

	/**
	 * Github issue #92
	 */
	@Test
	public void endlessLoopInRangeMode()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.RANGE);
		TestUtil.runThenWaitForPaintPulse( () -> {
			GregorianCalendar calendar = new GregorianCalendar(2018, 0, 1, 12, 00, 00);
			calendar.set(Calendar.MILLISECOND, 200); // force a millisecond
			calendarPicker.setDisplayedCalendar(calendar);
		});

		clickOn("#2018-01-01");

		// click next in month
		clickOn("#monthListSpinner .right-arrow");

		clickOn("#2018-02-28", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2018-02-28", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals(59, calendarPicker.calendars().size());
	}
}
