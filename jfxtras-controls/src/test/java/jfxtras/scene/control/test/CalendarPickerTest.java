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
public class CalendarPickerTest extends JFXtrasGuiTest {
// TODO: highlighted, range callback, validation callback
	
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

		// TODO: should we rename the button as their date, so we can do: clickOn("#2013-01-01")
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click again
		clickOn("#2013-01-02");

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

		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-02");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-04");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// unselect
		clickOn("#2013-01-02");

		// since the selected calendar was not unselected, it stays the samt
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// unselect
		clickOn("#2013-01-04");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// unselect
		clickOn("#2013-01-01");

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

		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-03", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-05");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// unselect
		clickOn("#2013-01-02");

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

		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click again
		clickOn("#2013-01-02");

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

		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		clickOn("#2013-01-03", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// extending the range
		clickOn("#2013-01-05", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-04, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
		
		// switching to a single date
		clickOn("#2013-01-10");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// selecting a range downwards
		clickOn("#2013-01-05", KeyCode.SHIFT);

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
			calendarPicker.setShowTime(true);
		});
		
		clickOn("#2013-01-01");

		Assert.assertEquals("2013-01-01T00:00:00.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));		
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTimeSlide()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
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
		moveTo("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T20:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
		
		// move the minute slider
		moveTo("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T20:22:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
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

		clickOn("#2013-01-01");

		// first of January
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));		

		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setAllowNull(false);
		});
		
		// click again (which would unselect in allow null mode)
		clickOn("#2013-01-01");

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
		clickOn("#yearListSpinner .right-arrow");

		// January 2014 is shown
		Assert.assertEquals("2014-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click next in month
		clickOn("#monthListSpinner .right-arrow");

		// Feb 2014 is shown
		Assert.assertEquals("2014-02-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click 2x prev in month
		clickOn("#monthListSpinner .left-arrow");
		clickOn("#monthListSpinner .left-arrow");

		// Dec 2013 is shown
		Assert.assertEquals("2013-12-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));

		// click prev in year
		clickOn("#yearListSpinner .left-arrow");

		// Dec 2012 is shown
		Assert.assertEquals("2012-12-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		
		// click next in year
		clickOn("#monthListSpinner .right-arrow");

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

		// set a value and a format
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

		// change date, time should not change
		clickOn("#2013-01-02");
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

		clickOn("#2013-01-01");
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));

		// this is disabled
		clickOn("#2013-01-02");
		// there should be no change
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));

		clickOn("#2013-01-03");
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void validateInSingleMode()
	{
		// setup to invalidate odd days
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setValueValidationCallback( (calendar) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (calendar == null || ((calendar.get(Calendar.DATE) % 2) == 0) );
			});
		});
		int lCallbackCount = 0;

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// 1st of January: not valid
		clickOn("#2013-01-01");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 2nd of January: valid
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 3rd of January: not valid
		clickOn("#2013-01-03");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 2nd of January again; since 3rd was not selected, this will deselect 
		clickOn("#2013-01-02");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // reselecting does not add
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void validateInMultipleMode()
	{
		// setup to invalidate odd days
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setValueValidationCallback( (calendar) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (calendar == null || ((calendar.get(Calendar.DATE) % 2) == 0) );
			});
		});
		int lCallbackCount = 0;
		
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// 1st of January: not valid
		clickOn("#2013-01-01");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 2nd of January: valid
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 4th of January: valid
		clickOn("#2013-01-04");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 3rd of January: not valid
		clickOn("#2013-01-03");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 2nd of January (unselect)
		clickOn("#2013-01-02");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // unselecting does not validate the value
		Assert.assertEquals("[2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 4st of January (unselect)
		clickOn("#2013-01-04");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // unselecting does not validate the value
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
		
	}

	/**
	 * 
	 */
	@Test
	public void validateInMultipleModeSelectingRange()
	{
		// setup to invalidate every fifth days
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setValueValidationCallback( (calendar) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (calendar == null || ((calendar.get(Calendar.DATE) % 5) != 0) );
			});
		});
		int lCallbackCount = 0;

		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// 2nd of January: valid
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// range select to the 7th of January; 5th should be skipped
		clickOn("#2013-01-07", KeyCode.SHIFT);
		lCallbackCount += 5; Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // all dates from the 3rd to the 7th are validated: 5 times  
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04, 2013-01-06, 2013-01-07]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 5th of January: not valid
		clickOn("#2013-01-05");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04, 2013-01-06, 2013-01-07]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}


	/**
	 * 
	 */
	@Test
	public void validateInRangeModeSelectingRange()
	{
		// setup to invalidate every fifth days
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setValueValidationCallback( (calendar) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (calendar == null || ((calendar.get(Calendar.DATE) % 5) != 0) );
			});
		});
		int lCallbackCount = 0;

		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.RANGE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// 2nd of January: valid
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// range select to the 5th of January
		clickOn("#2013-01-05", KeyCode.SHIFT);
		lCallbackCount += 3; Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // all dates from the 3rd to the 5th are validated: 3 times, then in range mode the range is broken  
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// 5th of January: not valid
		clickOn("#2013-01-05");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}
	
	/**
	 * 
	 */
	@Test
	public void validateInSingleModeWithTimeSlide()
	{
		// setup to invalidate odd hours
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setValueValidationCallback( (calendar) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (calendar == null || ((calendar.get(Calendar.HOUR_OF_DAY) % 2) == 0) );
			});
		});
		int lCallbackCount = 0;

		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarPicker.setShowTime(true);
		});
		
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());
		
		// 2nd of January: valid
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02T00:00:00.000]", TestUtil.quickFormatCalendarsAsDateTime(calendarPicker.calendars()));
		
		// move the hour slider
		moveTo("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("[2013-01-02T08:00:00.000]", TestUtil.quickFormatCalendarsAsDateTime(calendarPicker.calendars()));
		
		// move the hour slider again
		moveTo("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(20,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("[2013-01-02T08:00:00.000]", TestUtil.quickFormatCalendarsAsDateTime(calendarPicker.calendars()));
	}

	
	/**
	 * As reported in
	 * https://groups.google.com/forum/#!topic/jfxtras-dev/Qpip31DsciA
	 */
	@Test
	public void arrayIndexOutOfBounds()
	{
		// setup to may 2016
		// The exception will occur during rendering and will be asserted in the configured Thread.currentThread().setDefaultUncaughtExceptionHandler
		TestUtil.runThenWaitForPaintPulse( () -> {
			Calendar lCalendar = Calendar.getInstance(Locale.GERMANY);
			lCalendar.set(2016, 5-1, 1);
			calendarPicker.setDisplayedCalendar(lCalendar);
			calendarPicker.setLocale(Locale.GERMANY);
		});
	}
	
	/**
	 * 
	 */
	@Test
	public void todayButton()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());
		Calendar lTodayCalendar = Calendar.getInstance();
		Calendar lTodayFirstOfMonthCalendar = Calendar.getInstance();
		lTodayFirstOfMonthCalendar.set(Calendar.DATE, 1);

		// click once: only display the month of today
		clickOn(".today-button");

		// the last selected value should be set
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(lTodayFirstOfMonthCalendar), TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		Assert.assertNull(calendarPicker.getCalendar());

		// click twice: also select today
		clickOn(".today-button");

		// the last selected value should be set
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(lTodayFirstOfMonthCalendar), TestUtil.quickFormatCalendarAsDate(calendarPicker.getDisplayedCalendar()));
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(lTodayCalendar), TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
	}
}
