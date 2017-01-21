/**
 * LocalDatePickerTest.java
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
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class LocalDatePickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		localDatePicker = new LocalDatePicker();
		box.getChildren().add(localDatePicker);

		localDatePicker.setDisplayedLocalDate(LocalDate.of(2013, 1, 1));
		return box;
	}
	private LocalDatePicker localDatePicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultModeIsSingleWithNull()
	{
		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01]", localDatePicker.localDates().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// click the 2nd of January again
		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull( localDatePicker.getLocalDate());
		Assert.assertEquals("[]", localDatePicker.localDates().toString());
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNull()
	{
		// change localDatePicker's setting
		localDatePicker.setMode(LocalDatePicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull( localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01]", localDatePicker.localDates().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-02]", localDatePicker.localDates().toString());

		// click the 4th of January
		clickOn("#2013-01-04");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-04]", localDatePicker.localDates().toString());

		// click the 2nd of January (unselecting it)
		clickOn("#2013-01-02");

		// since the selected calendar was not unselected, it stays the same
		Assert.assertEquals("2013-01-04", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-04]", localDatePicker.localDates().toString());

		// click the 4th of January (unselecting it)
		clickOn("#2013-01-04");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01]", localDatePicker.localDates().toString());

		// click the 1st of January (unselecting it)
		clickOn("#2013-01-01");

		// the first value in the list should be selected
		Assert.assertNull( localDatePicker.getLocalDate());
		Assert.assertEquals("[]", localDatePicker.localDates().toString());
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNullSelectingRange()
	{
		// change localDatePicker's setting
		localDatePicker.setMode(LocalDatePicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull( localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01]", localDatePicker.localDates().toString());

		// shift click the 3rd of January
		clickOn("#2013-01-03", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", localDatePicker.localDates().toString());

		// click the 5th of January
		clickOn("#2013-01-05");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-05]", localDatePicker.localDates().toString());

		// click the 2nd of January (unselecting it)
		clickOn("#2013-01-02");

		// since the selected calendar was not unselected, it stays the samt
		Assert.assertEquals("2013-01-05", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-03, 2013-01-05]", localDatePicker.localDates().toString());
	}
	
	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingSingles()
	{
		// default value is null
		Assert.assertNull( localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01]", localDatePicker.localDates().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// click the 2nd of January again
		clickOn("#2013-01-02");

		// the first value in the list should be selected
		Assert.assertNull( localDatePicker.getLocalDate());
		Assert.assertEquals("[]", localDatePicker.localDates().toString());
	}

	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingRange()
	{
		// change localDatePicker's setting
		localDatePicker.setMode(LocalDatePicker.Mode.RANGE);

		// default value is null
		Assert.assertNull( localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01]", localDatePicker.localDates().toString());

		// shift click the 3rd of January
		clickOn("#2013-01-03", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", localDatePicker.localDates().toString());

		// shift click the 5th of January (extending the range)
		clickOn("#2013-01-05", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-04, 2013-01-05]", localDatePicker.localDates().toString());
		
		// click the 10th of January (switching to a single date)
		clickOn("#2013-01-10");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-10]", localDatePicker.localDates().toString());

		// shift click the 5th of January (selecting a range downwards)
		clickOn("#2013-01-05", KeyCode.SHIFT);

		// the last selected value should be set 
		Assert.assertEquals("2013-01-05", localDatePicker.getLocalDate().toString());
		Assert.assertEquals("[2013-01-10, 2013-01-09, 2013-01-08, 2013-01-07, 2013-01-06, 2013-01-05]", localDatePicker.localDates().toString());
	}

	/**
	 * 
	 */
	@Test
	public void notNullWhileNull()
	{
		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			localDatePicker.setAllowNull(false);
		});
		
		// not null caused a value to be set, which defaults to now 
		// These asserts will be green about 99.9999% of the time, because we only check on the date, not the time. Only when a date transition occurs exactly between the setAllowNull and the assert will they fail.
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), localDatePicker.getLocalDate().toString());
		Assert.assertEquals(1, localDatePicker.localDates().size());
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), localDatePicker.localDates().get(0).toString());
	}

	/**
	 * 
	 */
	@Test
	public void notNullWhileSet()
	{
		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// first of January
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());		

		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			localDatePicker.setAllowNull(false);
		});
		
		// click the 1st of January again (which would unselect in allow null mode)
		clickOn("#2013-01-01");

		// first of January
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());		
	}


	/**
	 * 
	 */
	@Test
	public void navigateYear()
	{
		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01", localDatePicker.getDisplayedLocalDate().toString());
		
		// click next in year
		clickOn("#yearListSpinner .right-arrow");

		// Jan 2014 is shown
		Assert.assertEquals("2014-01-01", localDatePicker.getDisplayedLocalDate().toString());
		
		// click next in month
		clickOn("#monthListSpinner .right-arrow");

		// Feb 2014 is shown
		Assert.assertEquals("2014-02-01", localDatePicker.getDisplayedLocalDate().toString());
		
		// click 2x prev in month
		clickOn("#monthListSpinner .left-arrow");
		clickOn("#monthListSpinner .left-arrow");

		// Dec 2013 is shown
		Assert.assertEquals("2013-12-01", localDatePicker.getDisplayedLocalDate().toString());

		// click prev in year
		clickOn("#yearListSpinner .left-arrow");

		// Dec 2012 is shown
		Assert.assertEquals("2012-12-01", localDatePicker.getDisplayedLocalDate().toString());
		
		// click next in year
		clickOn("#monthListSpinner .right-arrow");

		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01", localDatePicker.getDisplayedLocalDate().toString());
	}

	/**
	 * 
	 */
	@Test
	public void disabledCalendars()
	{
		// disable 2nd of January
		TestUtil.runThenWaitForPaintPulse( () -> {
			localDatePicker.disabledLocalDates().add(LocalDate.of(2013, 01, 02));
		});

		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");

		// there should be no change
		Assert.assertEquals("2013-01-01", localDatePicker.getLocalDate().toString());

		// click the 35d of January
		clickOn("#2013-01-03");

		// the last selected value should be set
		Assert.assertEquals("2013-01-03",localDatePicker.getLocalDate().toString());
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
			localDatePicker.setValueValidationCallback( (localDate) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localDate == null || ((localDate.getDayOfMonth() % 2) == 0) );
			});
		});
		int lCallbackCount = 0;

		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 1st of January: not valid
		clickOn("#2013-01-01");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[]", localDatePicker.localDates().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// click the 3rd of January: not valid
		clickOn("#2013-01-03");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// click the 2nd of January again
		clickOn("#2013-01-02");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // reselecting does not add
		Assert.assertEquals("[]", localDatePicker.localDates().toString());
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
			localDatePicker.setValueValidationCallback( (localDate) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localDate == null || ((localDate.getDayOfMonth() % 2) == 0) );
			});
		});
		int lCallbackCount = 0;
		
		// change localDatePicker's setting
		localDatePicker.setMode(LocalDatePicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 1st of January: not valid
		clickOn("#2013-01-01");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[]", localDatePicker.localDates().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// click the 4th of January
		clickOn("#2013-01-04");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02, 2013-01-04]", localDatePicker.localDates().toString());

		// click the 3rd of January: not valid
		clickOn("#2013-01-03");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("[2013-01-02, 2013-01-04]", localDatePicker.localDates().toString());

		// click the 2nd of January (unselecting it)
		clickOn("#2013-01-02");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // unselecting does not validate the value
		Assert.assertEquals("[2013-01-04]", localDatePicker.localDates().toString());

		// click the 4st of January (unselecting it)
		clickOn("#2013-01-04");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // unselecting does not validate the value
		Assert.assertEquals("[]", localDatePicker.localDates().toString());
		
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
			localDatePicker.setValueValidationCallback( (localDate) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localDate == null || ((localDate.getDayOfMonth() % 5) != 0) );
			});
		});
		int lCallbackCount = 0;

		// change localDatePicker's setting
		localDatePicker.setMode(LocalDatePicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 2nd of January
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// shift click the 7th of January; 5th should be skipped
		clickOn("#2013-01-07", KeyCode.SHIFT);
		lCallbackCount += 5; Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // all dates from the 3rd to the 7th are validated: 5 times  
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04, 2013-01-06, 2013-01-07]", localDatePicker.localDates().toString());

		// click the 5th of January: not valid
		clickOn("#2013-01-05");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04, 2013-01-06, 2013-01-07]", localDatePicker.localDates().toString());
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
			localDatePicker.setValueValidationCallback( (localDate) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localDate == null || ((localDate.getDayOfMonth() % 5) != 0) );
			});
		});
		int lCallbackCount = 0;

		// change localDatePicker's setting
		localDatePicker.setMode(LocalDatePicker.Mode.RANGE);

		// default value is null
		Assert.assertNull(localDatePicker.getLocalDate());

		// click the 2nd of January
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02]", localDatePicker.localDates().toString());

		// shift click the 7th of January
		clickOn("#2013-01-07", KeyCode.SHIFT);
		lCallbackCount += 3; Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // all dates from the 3rd to the 5th are validated: 3 times, then in range mode the range is broken  
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04]", localDatePicker.localDates().toString());

		// click the 5th of January: not valid
		clickOn("#2013-01-05");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("[2013-01-02, 2013-01-03, 2013-01-04]", localDatePicker.localDates().toString());
	}
}
