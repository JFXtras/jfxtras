/**
 * CalendarTimePickerTest.java
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jfxtras.internal.scene.control.skin.CalendarTimePickerSkin;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class CalendarTimePickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		calendarTimePicker = new CalendarTimePicker();
		box.getChildren().add(calendarTimePicker);

		// make sure there is enough room for the time sliders
		box.setPrefSize(300, 300);
		return box;
	}
	private CalendarTimePicker calendarTimePicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultControl()
	{
		// default value is not null
		Assert.assertNotNull(calendarTimePicker.getCalendar());
	}

	/**
	 * 
	 */
	@Test
	public void locale()
	{
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 20, 30, 00));			
		});
		
		Text lLabelText = (Text)find(".timeLabel");
		
		// assert label
		Assert.assertEquals("8:30 PM", lLabelText.getText());
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// change locale
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setLocale(Locale.GERMAN);			
		});
		
		// assert label
		Assert.assertEquals("20:30", lLabelText.getText());
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}
	
	/**
	 * 
	 */
	@Test
	public void slide()
	{
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 12, 30, 00));			
		});
		
		Text lLabelText = (Text)find(".timeLabel");
		
		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("8:30 PM", lLabelText.getText());
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("8:19 PM", lLabelText.getText());
		Assert.assertEquals("20:19:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}
	
	/**
	 * 
	 */
	@Test
	public void slideStep15()
	{
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 12, 30, 00));
			calendarTimePicker.setHourStep(2);
			calendarTimePicker.setMinuteStep(15);
			calendarTimePicker.setSecondStep(15);
			calendarTimePicker.setStyle("-fxx-label-dateformat:\"HH:mm:ss\";");
		});
		
		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(90,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:15:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// move the second slider
		move("#secondSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(40,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:15:15", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}
	
	/**
	 * 
	 */
	@Test
	public void validate()
	{
		// setup to invalidate odd hours
		AtomicInteger lCallbackCountAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setValueValidationCallback( (calendar) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (calendar == null || ((calendar.get(Calendar.HOUR_OF_DAY) % 2) == 0) );
			});
		});
		int lCallbackCount = 0;

		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 12, 30, 00));			
		});
		
		// move the hour slider: even hour is accepted
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));

		// move the hour slider: odd hour is not accepted
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(15,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// move the hour slider: even hour is accepted
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(30,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("22:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void labelDateFormatSlider()
	{
        
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 20, 30, 00));
		});
		
		// show only hours
		TestUtil.runThenWaitForPaintPulse( () -> {
	        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
	        ((CalendarTimePickerSkin)calendarTimePicker.getSkin()).setLabelDateFormat(timeFormat);
		});
		assertFind("#hourSlider");
		assertNotFind("#minuteSlider");
		assertNotFind("#secondSlider");
		
		// show only minutes
		TestUtil.runThenWaitForPaintPulse( () -> {
	        SimpleDateFormat timeFormat = new SimpleDateFormat("mm");
	        ((CalendarTimePickerSkin)calendarTimePicker.getSkin()).setLabelDateFormat(timeFormat);
		});
		assertNotFind("#hourSlider");
		assertFind("#minuteSlider");
		assertNotFind("#secondSlider");
		
		// show only seconds
		TestUtil.runThenWaitForPaintPulse( () -> {
	        SimpleDateFormat timeFormat = new SimpleDateFormat("ss");
	        ((CalendarTimePickerSkin)calendarTimePicker.getSkin()).setLabelDateFormat(timeFormat);
		});
		assertNotFind("#hourSlider");
		assertNotFind("#minuteSlider");
		assertFind("#secondSlider");
		
		// show hms
		TestUtil.runThenWaitForPaintPulse( () -> {
	        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	        ((CalendarTimePickerSkin)calendarTimePicker.getSkin()).setLabelDateFormat(timeFormat);
		});
		assertFind("#hourSlider");
		assertFind("#minuteSlider");
		assertFind("#secondSlider");
	}

	/**
	 * 
	 */
	@Test
	public void labelDateFormatTimeZone()
	{
        
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 20, 30, 00));
	        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			timeFormat.setTimeZone(TimeZone.getTimeZone("CET"));
	        ((CalendarTimePickerSkin)calendarTimePicker.getSkin()).setLabelDateFormat(timeFormat);
		});
		
		Text lLabelText = (Text)find(".timeLabel");
		
		// assert label
		Assert.assertEquals("20:30:00", lLabelText.getText());
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// change timezone
		TestUtil.runThenWaitForPaintPulse( () -> {
	        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	        ((CalendarTimePickerSkin)calendarTimePicker.getSkin()).setLabelDateFormat(timeFormat);
		});

		// assert label
		Assert.assertEquals("20:30:00", lLabelText.getText());
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}
}
