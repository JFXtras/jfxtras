/**
 * LocalDateTimePickerTest.java
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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.LocalDateTimePicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class LocalDateTimePickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		localDateTimePicker = new LocalDateTimePicker();
		box.getChildren().add(localDateTimePicker);

		localDateTimePicker.setDisplayedLocalDateTime(LocalDateTime.of(2013, 1, 1, 12, 00, 00));
		
		// make sure the pixels moved by the mouse are constant
		box.setPrefSize(300, 300);
		return box;
	}
	private LocalDateTimePicker localDateTimePicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultModeIsSingleWithNull()
	{
		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());

		// click the 2nd of January again
		clickOn("#2013-01-02");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull( localDateTimePicker.getLocalDateTime());
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTime()
	{
		// click the 1st of January
		clickOn("#2013-01-01");

		// first of January at midnight
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());		
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTimeSlide()
	{
		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// set a value
		final LocalDateTime lLocalDateTime = LocalDateTime.of(2013, 01, 01, 12, 34, 56);
		TestUtil.runThenWaitForPaintPulse( () -> {
			localDateTimePicker.setLocalDateTime( lLocalDateTime );
		});
		Assert.assertEquals("2013-01-01T12:34:56", localDateTimePicker.getLocalDateTime().toString());

		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T20:34:56", localDateTimePicker.getLocalDateTime().toString());
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T20:22:56", localDateTimePicker.getLocalDateTime().toString());
	}
	
	
	/**
	 * 
	 */
	@Test
	public void notNullWhileNull()
	{
		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			localDateTimePicker.setAllowNull(false);
		});
		
		// not null caused a value to be set, which defaults to now 
		// These asserts will be green about 99.9999% of the time, because we only check on the date, not the time. Only when a date transition occurs exactly between the setAllowNull and the assert will they fail.
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), localDateTimePicker.getLocalDateTime().toLocalDate().toString());
	}

	
	/**
	 * 
	 */
	@Test
	public void notNullWhileSet()
	{
		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		clickOn("#2013-01-01");

		// first of January
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());		

		TestUtil.runThenWaitForPaintPulse( () -> {
			// show time
			localDateTimePicker.setAllowNull(false);
		});
		
		// click the 1st of January again (which would unselect in allow null mode)
		clickOn("#2013-01-01");

		// first of January
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());		
	}


	/**
	 * 
	 */
	@Test
	public void navigateYear()
	{
		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click next in year
		clickOn("#yearListSpinner .right-arrow");

		// Jan 2014 is shown
		Assert.assertEquals("2014-01-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click next in month
		clickOn("#monthListSpinner .right-arrow");

		// Feb 2014 is shown
		Assert.assertEquals("2014-02-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click 2x prev in month
		clickOn("#monthListSpinner .left-arrow");
		clickOn("#monthListSpinner .left-arrow");

		// Dec 2013 is shown
		Assert.assertEquals("2013-12-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());

		// click prev in year
		clickOn("#yearListSpinner .left-arrow");

		// Dec 2012 is shown
		Assert.assertEquals("2012-12-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click next in year
		clickOn("#monthListSpinner .right-arrow");

		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
	}

	/**
	 * 
	 */
	@Test
	public void disabledCalendars()
	{
		// disable 2nd of January
		TestUtil.runThenWaitForPaintPulse( () -> {
			localDateTimePicker.disabledLocalDateTimes().add(LocalDateTime.of(2013, 01, 02, 00, 00, 00));
		});

		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		clickOn("#2013-01-01");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());

		// click the 2nd of January
		clickOn("#2013-01-02");

		// there should be no change
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());

		// click the 35d of January
		clickOn("#2013-01-03");

		// the last selected value should be set
		Assert.assertEquals("2013-01-03T00:00", localDateTimePicker.getLocalDateTime().toString());
	}

	/**
	 * 
	 */
	@Test
	public void validate()
	{
		// setup to invalidate odd days
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			localDateTimePicker.setValueValidationCallback( (localDateTime) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localDateTime == null || ((localDateTime.getDayOfMonth() % 2) == 0) );
			});
		});
		int lCallbackCount = 0;

		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		// click the 1st of January: not valid
		clickOn("#2013-01-01");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		// click the 2nd of January
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());

		// click the 3rd of January: not valid
		clickOn("#2013-01-03");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get());
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());

		// click the 2nd of January again
		clickOn("#2013-01-02");
		Assert.assertEquals(lCallbackCount, lCallbackCountAtomicInteger.get()); // reselecting does not add
		Assert.assertNull(localDateTimePicker.getLocalDateTime());
	}
	
	/**
	 * 
	 */
	@Test
	public void validateWithTimeSlide()
	{
		// setup to invalidate odd hours
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			localDateTimePicker.setValueValidationCallback( (localDateTime) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localDateTime == null || (localDateTime.getHour() %2) == 0);
			});
		});
		int lCallbackCount = 0;

		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());
		
		// click the 2nd of January
		clickOn("#2013-01-02");
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());
		
		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-02T08:00", localDateTimePicker.getLocalDateTime().toString());
		
		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(20,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-02T08:00", localDateTimePicker.getLocalDateTime().toString());
	}
}
