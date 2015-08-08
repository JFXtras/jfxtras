/**
 * CalendarTextFieldTest.java
 *
 * Copyright (c) 2011-2015, JFXtras
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
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class CalendarTextFieldTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		HBox box = new HBox();
		calendarTextField = new CalendarTextField();
		box.getChildren().add(calendarTextField);
		Button lButton = new Button("focus helper");
		lButton.setId("focusHelper");
		box.getChildren().add(lButton);

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
	}

	/**
	 * 
	 */
	@Test
	public void textfieldIsDisabledWhenPopupIsOpen()
	{
		// default value is null
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
		
		// open the popup
		click(".icon");
		Assert.assertTrue(find(".text-field").isDisabled());
		
		// click today
		click(".today");
		
		// now should be the value in the textfield
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}

    @Test
    public void checkIfDisabledDateIsForwardedToThePicker() {
    	// make today disabled   
       calendarTextField.disabledCalendars().add(Calendar.getInstance());
        
        // open the picker
        click(".icon");

        // make sure that today in the picker is disabled
        Assert.assertTrue(find(".today").isDisabled());
    }

    @Test
    public void checkIfDisabledDateIsForwardedToThePickerAtRuntime() {
        // open the picker
        click(".icon");
        TestUtil.waitForPaintPulse();
        
        // make sure that today in the picker is enabled
        Assert.assertFalse(find(".today").isDisabled());
        
    	// make today highlighted   
        TestUtil.runThenWaitForPaintPulse(() -> {
        	calendarTextField.disabledCalendars().add(Calendar.getInstance());
        });
        
        // make sure that today in the picker is highlighted
        Assert.assertTrue(find(".today").isDisabled());
    }

    @Test
    public void checkIfHighlightedDateIsForwardedToThePicker() {
    	// make today highlighted   
       calendarTextField.highlightedCalendars().add(Calendar.getInstance());
        
        // open the picker
        click(".icon");

        // make sure that today in the picker is highlighted
        Assert.assertTrue(find(".today").getStyleClass().contains("highlight"));
    }

    @Test
    public void checkIfHighlightedDateIsForwardedToThePickerAtRuntime() {
        // open the picker
        click(".icon");
        TestUtil.waitForPaintPulse();
        
        // make sure that today in the picker is enabled
        Assert.assertFalse(find(".today").getStyleClass().contains("highlight"));
        
    	// make today disabled   
        TestUtil.runThenWaitForPaintPulse(() -> {
        	calendarTextField.highlightedCalendars().add(Calendar.getInstance());
        });
        
        // make sure that today in the picker is disabled
        Assert.assertTrue(find(".today").getStyleClass().contains("highlight"));
    }

    /**
	 * 
	 */
	@Test
	public void textfieldIsDisabledWhenPopupIsOpenDateTime()
	{
		// switch to datetime mode
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setShowTime(true);
			calendarTextField.dateFormatProperty().set(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		});
		
		// default value is null
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
		
		// open the popup
		click(".icon");
		Assert.assertTrue(find(".text-field").isDisabled());
		
		// click today
		click(".today");
		
		// click today
		click(".accept-icon");
		
		// now should be the value in the textfield
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}

	/**
	 * 
	 */
	@Test
	public void selectDateInPopup()
	{
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
        Assert.assertFalse(calendarTextField.isPickerShowing());
		
		// open the popup
		click(".icon");
                
                //The popup should be displayed.
		Assert.assertTrue(calendarTextField.isPickerShowing());
                
		// click today
		click(".today");
		
		// now should be the value in the textfield
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));
                
                //The popup should be hidden.
		Assert.assertFalse(calendarTextField.isPickerShowing());
        }

	/**
	 * 
	 */
	@Test
	public void selectDateTimeInPopup()
	{
		// switch to datetime mode
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setShowTime(true);
			calendarTextField.dateFormatProperty().set(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		});
		
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
		
		// open the popup
		click(".icon");
		Assert.assertTrue(find(".text-field").isDisabled());
                
                //The popup should be displayed.
		Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// click today
		click(".today");
		
		// click today
		click(".accept-icon");
		
		// now should be the value in the textfield
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}

	/**
	 * 
	 */
	@Test
	public void cancelDateTimeInPopup()
	{
		// set a value
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setShowTime(true);
			calendarTextField.dateFormatProperty().set(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		});
		
		// start value is null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
		
		// open the popup
		click(".icon");
		Assert.assertTrue(find(".text-field").isDisabled());
                
                //The popup should be displayed.
		Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// click today
		click(".today");
		
		// click close
		click(".close-icon");
		
		// should still be null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}

	/**
	 * 
	 */
	@Test
	public void cancelDateTimeInPopupByEscape()
	{
		// set a value
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setShowTime(true);
			calendarTextField.dateFormatProperty().set(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		});
		
		// start value is null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
		
		// open the popup
		click(".icon");
		Assert.assertTrue(find(".text-field").isDisabled());
		Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// click today
		click(".today");
		
		// send esc
		press(KeyCode.ESCAPE);
		
		// should still be null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}


	/**
	 * 
	 */
	@Test
	public void cancelDateTimeInPopupByFocusLost()
	{
		// set a value
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setShowTime(true);
			calendarTextField.dateFormatProperty().set(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		});
		
		// start value is null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
		
		// open the popup
		click(".icon");
		Assert.assertTrue(find(".text-field").isDisabled());
        Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// click today
		click(".today");
		
		// move focus away
		click("#focusHelper");
		
		// should still be null
		Assert.assertNull(calendarTextField.getCalendar());
		Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}

	/**
	 * 
	 */
	@Test
	public void popupWithCalendarSet()
	{
		Calendar lCalendar = new GregorianCalendar(2013, 0, 1, 12, 00, 00);
		
		// set a value
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setCalendar(lCalendar);
		});
		
		// open the popup
		click(".icon");
        Assert.assertTrue(calendarTextField.isPickerShowing());

		// assert that the popup shows January 1st 2013 
		Assert.assertTrue( ((ToggleButton)find("#2013-01-01")).isSelected() );
		Assert.assertEquals("January", ((Label)find("#monthListSpinner .label")).getText() );
		Assert.assertEquals("2013", ((Label)find("#yearListSpinner .label")).getText() );
	}
	
	/**
	 * 
	 */
	@Test
	public void openPopupAndCloseOnEscape()
	{
		// popup should be closed
		assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(calendarTextField.isPickerShowing());
		
		// open the popup
		click(".icon");
        Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// popup should be open
		assertPopupIsVisible(find(".text-field"));
		
		// send esc
		press(KeyCode.ESCAPE);
		
		// popup should be closed
		assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}


	/**
	 * 
	 */
	@Test
	public void nullAllowed()
	{
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
		
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setAllowNull(false);
		});
		
		Assert.assertNotNull(calendarTextField.getCalendar());
	}


	/**
	 * 
	 */
	@Test
	public void nullNotAllowed()
	{
		// set null not allowed
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setAllowNull(false);			
		});
		AtomicBoolean lParseErrorCallbackWasCalled = new AtomicBoolean(false);
		calendarTextField.setParseErrorCallback( (throwable) -> {
			lParseErrorCallbackWasCalled.set(true);
			return null;
		});
		
		// then clear the textfield
		clear(calendarTextField);
		
		// move focus away
		click("#focusHelper");
		
		// check for result
		Assert.assertTrue(lParseErrorCallbackWasCalled.get());
	}

	/**
	 * 
	 */
	@Test
	public void nullAllowedAndUnselectInPopup()
	{
		// set a value
		Calendar lCalendar = new GregorianCalendar(2013, 0, 1, 12, 00, 00);
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setCalendar(lCalendar);
		});
		
		// default value is not null
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(lCalendar), TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));

		// open the popup
		click(".icon");

		// click the 1st of January
		click("#2013-01-01");
		
		// value is null
		Assert.assertNull(calendarTextField.getCalendar());
	}

	/**
	 * 
	 */
	@Test
	public void nullNotAllowedAndAttemptUnselectInPopup()
	{
		// set a value
		Calendar lCalendar = new GregorianCalendar(2013, 0, 1, 12, 00, 00);
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setCalendar(lCalendar);
			calendarTextField.setAllowNull(false);
		});
		
		// default value is not null
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(lCalendar), TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));

		// open the popup
		click(".icon");

		// click the 1st of January
		click("#2013-01-01");
		
		// value is still not null
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(lCalendar), TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void typeValue()
	{
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
		
		// type value
		click(calendarTextField).type(calendarTextField.getDateFormat().format(new GregorianCalendar(2014, 11, 31).getTime()));
		
		// move focus away
		click("#focusHelper");
		
		// now should be the value in the textfield
		Assert.assertEquals("2014-12-31", TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void typeValueUsingAdditonalDateFormatter()
	{
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
		
		// add a second formatter
		calendarTextField.dateFormatsProperty().add(new SimpleDateFormat("yyyy-MM-dd"));
		
		// type value
		click(calendarTextField).type("2014-12-31");
		
		// move focus away
		click("#focusHelper");
		// for some reason the focus is not moved always
		click(".CalendarTextField");
		click("#focusHelper");
		
		// now should be the value in the textfield
		Assert.assertEquals("2014-12-31", TestUtil.quickFormatCalendarAsDate(calendarTextField.getCalendar()));
	}

	/**
	 * 
	 */
	@Test
	public void setValue()
	{
		// default value is null
		Assert.assertNull(calendarTextField.getCalendar());
		
		// set value
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.dateFormatProperty().set(new SimpleDateFormat("yyyy-MM-dd"));
			calendarTextField.setCalendar(new GregorianCalendar(2014, 11, 31));
		});
		
		// now should be the value in the textfield
		Assert.assertEquals("2014-12-31", calendarTextField.getText());
	}
	
	/**
	 * 
	 */
	@Test
	public void popupIsClosedWhenReselectingSameDateInNotNullMode()
	{
		Calendar lCalendar = new GregorianCalendar(2013, 0, 1, 12, 00, 00);
		
		// set a value
		TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setCalendar(lCalendar);
			calendarTextField.setAllowNull(false);
		});
		
		// popup should be closed
		assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(calendarTextField.isPickerShowing());
		
		// open the popup
		click(".icon");
		
		// popup should be closed
		assertPopupIsVisible(find(".text-field"));
        Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// reselect 1st of January
		click("#2013-01-01");
		
		// popup should be closed
		assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}
        
        /**
	 * 
	 */
	@Test
	public void openPopupAndCloseWithProperty()
	{
		// popup should be closed
		assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(calendarTextField.isPickerShowing());
		
		// open the popup
        TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setPickerShowing(true);
		});
                
		// popup should be open
		assertPopupIsVisible(find(".text-field"));
        Assert.assertTrue(calendarTextField.isPickerShowing());
		
		// Close the popup
        TestUtil.runThenWaitForPaintPulse( () -> {
			calendarTextField.setPickerShowing(false);
		});
		
		// popup should be closed
		assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(calendarTextField.isPickerShowing());
	}
        
    @Test
    public void setDisplayedCalendar() {
        Calendar lCalendar = new GregorianCalendar(2013, 0, 1, 12, 00, 00);
        calendarTextField.setDisplayedCalendar(lCalendar);

        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            calendarTextField.setPickerShowing(true);
        });

        assertFind("#2013-01-01");
    }
}
