/**
 * LocalTimePickerTest.java
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

import java.time.LocalTime;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class LocalTimePickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		localTimePicker = new LocalTimePicker();
		box.getChildren().add(localTimePicker);

		// make sure there is enough room for the time sliders
		box.setPrefSize(300, 300);
		return box;
	}
	private LocalTimePicker localTimePicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultControl()
	{
		// default value is null
		Assert.assertNotNull(localTimePicker.getLocalTime());
	}

	/**
	 * 
	 */
	@Test
	public void slide()
	{
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			localTimePicker.setLocalTime(LocalTime.of(12, 30, 00));			
		});
		
		Text lLabelText = (Text)find(".timeLabel");

		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("8:30 PM", lLabelText.getText());
		Assert.assertEquals("20:30", localTimePicker.getLocalTime().toString());
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("8:19 PM", lLabelText.getText());
		Assert.assertEquals("20:19", localTimePicker.getLocalTime().toString());
	}
	

	/**
	 * 
	 */
	@Test
	public void slideStep15()
	{
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			localTimePicker.setLocalTime(LocalTime.of(12, 30, 00));			
			localTimePicker.setMinuteStep(15);
			localTimePicker.setSecondStep(15);
			localTimePicker.setStyle("-fxx-label-dateformat:\"HH:mm:ss\";");
		});
		
		Text lLabelText = (Text)find(".timeLabel");

		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:30:00", lLabelText.getText());
		Assert.assertEquals("20:30", localTimePicker.getLocalTime().toString());

		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:15:00", lLabelText.getText());
		Assert.assertEquals("20:15", localTimePicker.getLocalTime().toString());

		// move the second slider
		move("#secondSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(40,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:15:15", lLabelText.getText());
		Assert.assertEquals("20:15:15", localTimePicker.getLocalTime().toString());
	}
	
	/**
	 * 
	 */
	@Test
	public void locale()
	{
		Text lLabelText = (Text)find(".timeLabel");
		
		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			localTimePicker.setLocalTime(LocalTime.of(20, 30, 00));			
		});
		
		// assert label
		Assert.assertEquals("8:30 PM", lLabelText.getText());
		
		// change locale
		TestUtil.runThenWaitForPaintPulse( () -> {
			localTimePicker.setLocale(Locale.GERMAN);			
		});
		
		// assert label
		Assert.assertEquals("20:30", lLabelText.getText());
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
			localTimePicker.setValueValidationCallback( (localTime) -> {
				// if day is odd, return false, so if even return true
				lCallbackCountAtomicInteger.incrementAndGet();
				return (localTime == null || ((localTime.getHour() % 2) == 0) );
			});
		});
		int lCallbackCount = 0;

		// set time to 12:30:00
		TestUtil.runThenWaitForPaintPulse( () -> {
			localTimePicker.setLocalTime(LocalTime.of(12, 30, 00));			
		});
		
		// move the hour slider: even hour is accepted
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("20:30", localTimePicker.getLocalTime().toString());

		// move the hour slider: odd hour is not accepted
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(15,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("20:30", localTimePicker.getLocalTime().toString());
		
		// move the hour slider: even hour is accepted
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(30,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals(++lCallbackCount, lCallbackCountAtomicInteger.get()); 
		Assert.assertEquals("22:30", localTimePicker.getLocalTime().toString());
	}
}
