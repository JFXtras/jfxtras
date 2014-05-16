/**
 * CalendarPickerTest.java
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
import java.util.GregorianCalendar;
import java.util.Locale;

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
public class CalendarPickerTest extends JFXtrasGuiTest {
// TODO: highlighted, disabled, range callback
	
	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		calendarPicker = new CalendarPicker();
		box.getChildren().add(calendarPicker);

		// display first of January
		calendarPicker.setDisplayedCalendar(new GregorianCalendar(2013, 0, 1, 12, 00, 00));
		
		// make sure there is enough room for the time sliders
		box.setPrefSize(300, 300);
		return box;
	}
	private CalendarPicker calendarPicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultModeIsSingleWithNull()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNull()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 4th of January
		click("#day5");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// since the selected calendar was not unselected, it stays the samt
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 4st of January (unselecting it)
		click("#day5");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 1th of January (unselecting it)
		click("#day2");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNullSelectingRange()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 5th of January
		click("#day6");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// since the selected calendar was not unselected, it stays the samt
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-03, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}
	
	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingSingles()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingRange()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.RANGE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 5th of January (extending the range)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-04, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
		
		// click the 10th of January (switching to a single date)
		click("#day11");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 5th of January (selecting a range downwards)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set 
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10, 2013-01-09, 2013-01-08, 2013-01-07, 2013-01-06, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTime()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			calendarPicker.setShowTime(true);
		});
		
		// click the 1st of January
		click("#day2");

		// first of January at midnight
		Assert.assertEquals("2013-01-01T00:00:00.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));		
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTimeSlide()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			calendarPicker.setShowTime(true);
		});
		
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// set a value
		final Calendar lCalendar = new GregorianCalendar(2013, 00, 01, 12, 34, 56);
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setCalendar( (Calendar)lCalendar.clone() );
		});
		Assert.assertEquals("2013-01-01T12:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));

		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T20:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T20:23:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
	}
	
	/**
	 * 
	 */
	@Test
	public void notNullWhileNull()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			calendarPicker.setAllowNull(false);
		});
		
		// not null, so it defaults to now
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[" + TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()) + "]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	
	/**
	 * 
	 */
	@Test
	public void notNullWhileSet()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// first of January
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));		

		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			calendarPicker.setAllowNull(false);
		});
		
		// click the 1st of January again (which would unselect in allow null mode)
		click("#day2");

		// first of January
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));		
	}

	/**
	 * 
	 */
	@Test
	public void navigateYear()
	{
		// January 2013 is shown
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click next in year
		click("#yearListSpinner .right-arrow");

		// January 2014 is shown
		Assert.assertEquals("2014-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click next in month
		click("#monthListSpinner .right-arrow");

		// Feb 2014 is shown
		Assert.assertEquals("2014-02-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click 2x prev in month
		click("#monthListSpinner .left-arrow");
		click("#monthListSpinner .left-arrow");

		// Dec 2013 is shown
		Assert.assertEquals("2013-12-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));

		// click prev in year
		click("#yearListSpinner .left-arrow");

		// Dec 2012 is shown
		Assert.assertEquals("2012-12-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click next in year
		click("#monthListSpinner .right-arrow");

		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
	}
	
	/**
	 * 
	 */
	@Test
	public void changingDateShouldNotTouchTime()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// set a value
		final Calendar lCalendar = new GregorianCalendar(2013, 00, 01, 12, 34, 56);
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setShowTime(true);
		});
		final CalendarTimePicker lCalendarTimePicker = (CalendarTimePicker)find(".CalendarTimePicker");
		TestUtil.runThenWaitForPaintPulse( () -> {
			lCalendarTimePicker.setStyle("-fxx-label-dateformat:\"HH:mm:ss\";");
			calendarPicker.setCalendar( (Calendar)lCalendar.clone() );
		});
		Assert.assertEquals("2013-01-01T12:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
		
		// click the 2nd of January
		click("#day3");
		Assert.assertEquals("2013-01-02T12:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void disabledCalendars()
	{
		// disable 2nd of January
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.disabledCalendars().add(new GregorianCalendar(2013, 00, 02, 12, 34, 56));
		});

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));

		// click the 2nd of January
		click("#day3");

		// there should be no change
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));

		// click the 35d of January
		click("#day4");

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
	}

}
