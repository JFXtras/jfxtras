/**
 * LocalDateTimeTextFieldTest.java
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
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * Created by Samir Hadzic on 21-05-14.
 */
public class LocalDateTimeTextFieldTest extends JFXtrasGuiTest {

    /**
     *
     */
    public Parent getRootNode() {
        Locale.setDefault(Locale.ENGLISH);

        HBox box = new HBox();
        localDateTimeTextField = new LocalDateTimeTextField();
        localDateTimeTextField.setParseErrorCallback( throwable -> { 
        	parseErrorThrowable = throwable;
        	System.out.println("Parse exception caught: " + throwable);
        	return null;
        });
        box.getChildren().add(localDateTimeTextField);
        Button lButton = new Button("focus helper");
        lButton.setId("focusHelper");
        box.getChildren().add(lButton);

        return box;
    }
    private LocalDateTimeTextField localDateTimeTextField = null;
    private Throwable parseErrorThrowable = null;

    /**
     *
     */
    @Test
    public void defaultModeIsNull() {
        // default value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
    }

    /**
     *
     */
    @Test
    public void textfieldIsDisabledWhenPopupIsOpenDateTime() {
        // switch to datetime mode
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        });

        // default value is null
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        // click today
        clickOn(".today");

        // click today
        clickOn(".accept-icon");

        // now should be the value in the textfield
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void selectDateTimeInPopup() {
        // switch to datetime mode
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        });

        // default value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        //The popup should be displayed.
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        clickOn(".today");

        // close popup
        clickOn(".accept-icon");

        // now should be the value in the textfield
		Assert.assertEquals(LocalDateTime.now().withSecond(0).withNano(0), localDateTimeTextField.getLocalDateTime().withSecond(0).withNano(0)); // this fails when a new minute turns between .today and this assert
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void cancelDateTimeInPopup() {
        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        });

        // start value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        //The popup should be displayed.
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        clickOn(".today");

        // click close
        clickOn(".close-icon");

        // should still be null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void cancelDateTimeInPopupByEscape() {
        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        });

        // start value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        clickOn(".today");

        // send esc
        press(KeyCode.ESCAPE);

        // should still be null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    @Test
    public void checkIfDisabledDateIsForwardedToThePicker() {
    	// make today disabled   
    	localDateTimeTextField.disabledLocalDateTimes().add(LocalDateTime.now());
        
        // open the picker
        clickOn(".icon");

        // make sure that today in the picker is disabled
        Assert.assertTrue(find(".today").isDisabled());
    }

    @Test
    public void checkIfDisabledDateIsForwardedToThePickerAtRuntime() {
        // open the picker
        clickOn(".icon");
        TestUtil.waitForPaintPulse();
        
        // make sure that today in the picker is enabled
        Assert.assertFalse(find(".today").isDisabled());
        
    	// make today disabled   
        TestUtil.runThenWaitForPaintPulse(() -> {
        	localDateTimeTextField.disabledLocalDateTimes().add(LocalDateTime.now());
        });
        
        // make sure that today in the picker is disabled
        Assert.assertTrue(find(".today").isDisabled());
    }

    @Test
    public void checkIfHighlightedDateIsForwardedToThePicker() {
    	// make today highlighted   
    	localDateTimeTextField.highlightedLocalDateTimes().add(LocalDateTime.now());
        
        // open the picker
        clickOn(".icon");

        // make sure that today in the picker is highlighted
        Assert.assertTrue(find(".today").getStyleClass().contains("highlight"));
    }

    @Test
    public void checkIfHighlightedDateIsForwardedToThePickerAtRuntime() {
        // open the picker
        clickOn(".icon");
        TestUtil.waitForPaintPulse();
        
        // make sure that today in the picker is enabled
        Assert.assertFalse(find(".today").getStyleClass().contains("highlight"));
        
    	// make today highlighted   
        TestUtil.runThenWaitForPaintPulse(() -> {
        	localDateTimeTextField.highlightedLocalDateTimes().add(LocalDateTime.now());
        });
        
        // make sure that today in the picker is highlighted
        Assert.assertTrue(find(".today").getStyleClass().contains("highlight"));
    }
    
    /**
     *
     */
    @Test
    public void cancelDateTimeInPopupByFocusLost() {
        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        });

        // start value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        clickOn(".today");

        // move focus away
        clickOn("#focusHelper");

        // should still be null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void popupWithCalendarSet() {
        LocalDateTime lCalendar = LocalDateTime.of(2013, 1, 1, 12, 00, 00);

        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setLocalDateTime(lCalendar);
        });

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // assert that the popup shows January 1st 2013 
        Assert.assertTrue(((ToggleButton) find("#2013-01-01")).isSelected());
        Assert.assertEquals("January", ((Label) find("#monthListSpinner .label")).getText());
        Assert.assertEquals("2013", ((Label) find("#yearListSpinner .label")).getText());
    }

    /**
     *
     */
    @Test
    public void openPopupAndCloseOnEscape() {
        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // popup should be open
        assertPopupIsVisible(find(".text-field"));

        // send esc
        press(KeyCode.ESCAPE);

        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void nullAllowed() {
        // default value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());

        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setAllowNull(false);
        });

        Assert.assertNotNull(localDateTimeTextField.getLocalDateTime());
    }

    /**
     *
     */
    @Test
    public void nullNotAllowed() {
        // set null not allowed
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setAllowNull(false);
        });
        AtomicBoolean lParseErrorCallbackWasCalled = new AtomicBoolean(false);
        localDateTimeTextField.setParseErrorCallback((throwable) -> {
            lParseErrorCallbackWasCalled.set(true);
            return null;
        });

        // then clear the textfield
        clear(localDateTimeTextField);

        // move focus away
        clickOn("#focusHelper");

        // check for result
        Assert.assertTrue(lParseErrorCallbackWasCalled.get());
    }

    /**
     *
     */
    @Test
    public void nullNotAllowedAndAttemptUnselectInPopup() {
        // set a value
        LocalDateTime lCalendar = LocalDateTime.of(2013, 1, 1, 12, 00, 00);
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setLocalDateTime(lCalendar);
            localDateTimeTextField.setAllowNull(false);
        });

        // default value is not null
        Assert.assertEquals(lCalendar, localDateTimeTextField.getLocalDateTime());

        // open the popup
        clickOn(".icon");

        // click the 1st of January
        clickOn("#2013-01-01");

        // value is still not null
        Assert.assertEquals(lCalendar, localDateTimeTextField.getLocalDateTime());
    }

    /**
     *
     */
    @Test
    public void typeValueUsingAdditonalDateFormatter() {
        // default value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());

        // add a second formatter
        localDateTimeTextField.dateTimeFormattersProperty().add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));

        // type value
        clickOn(localDateTimeTextField).write("2014-12-31 00-00-00");

        // move focus away
        clickOn("#focusHelper");

        // now should be the value in the textfield
        Assert.assertEquals("2014-12-31 00:00:00", TestUtil.quickFormatLocalDateTimeAsDate(localDateTimeTextField.getLocalDateTime()));
    }

    /**
     *
     */
    @Test
    public void setValue() {
        // default value is null
        Assert.assertNull(localDateTimeTextField.getLocalDateTime());

        // set value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            localDateTimeTextField.setLocalDateTime(LocalDateTime.of(2014, 12, 31, 0, 0, 0));
        });

        // now should be the value in the textfield
        Assert.assertEquals("2014-12-31 00:00:00", localDateTimeTextField.getText());
    }

    /**
     *
     */
    @Test
    public void openPopupAndCloseWithProperty() {
        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());

        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setPickerShowing(true);
        });

        // popup should be open
        assertPopupIsVisible(find(".text-field"));
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // Close the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setPickerShowing(false);
        });

        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTimeTextField.isPickerShowing());
    }

    @Test
    public void setDisplayedCalendar() {
        LocalDateTime lCalendar = LocalDateTime.of(2013, 1, 1, 12, 00, 00);
        localDateTimeTextField.setDisplayedLocalDateTime(lCalendar);

        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setPickerShowing(true);
        });

        assertFind("#2013-01-01");
    }
    
     
    @Test
    public void requestFocus() {
        //Give focus to the icon
        TestUtil.runAndWait(() -> {
            find(".icon").requestFocus();
        });

        //TextField should not be focused
        Assert.assertFalse(find(".text-field").isFocused());
        
        TestUtil.runAndWait(() -> {
            localDateTimeTextField.requestFocus();
        });

        //TextField should be now
        Assert.assertTrue(find(".text-field").isFocused());
    }
    
    @Test
    public void selectAll() {
        LocalDateTime lCalendar = LocalDateTime.of(2013, 1, 1, 12, 00, 00);

        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setLocalDateTime(lCalendar);
        });
        
        TextField textField = (TextField)find(".text-field");
        Assert.assertFalse(textField.getText().isEmpty());

        localDateTimeTextField.selectAll();

        Assert.assertEquals(textField.getText(), textField.getSelectedText());
    }
    
    @Test
    public void inputAfterEscape() {
        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTimeTextField.setPickerShowing(true);
        });
        type(KeyCode.ESCAPE);

        type(KeyCode.NUMPAD2);

        //TextField should be focused
        Assert.assertTrue(find(".text-field").isFocused());

        Assert.assertEquals("2", ((TextField) find(".text-field")).getText());
    }
    
     @Test
    public void textFieldGetText() {
        Assert.assertTrue(find(".text-field").isFocused());

        // Type 2
        type(KeyCode.NUMPAD2);

        //We should have the same value everywhere.
        Assert.assertEquals(((TextField) find(".text-field")).getText(), localDateTimeTextField.getText());
        Assert.assertEquals("2", localDateTimeTextField.getText());
    }
    
    @Test
    public void textFieldSetText() {
        Assert.assertTrue(find(".text-field").isFocused());

        localDateTimeTextField.setText("2");

        // no error should have been thrown
        Assert.assertNull(parseErrorThrowable);
        
        //We should have the same value everywhere.
        Assert.assertEquals(((TextField) find(".text-field")).getText(), localDateTimeTextField.getText());
        Assert.assertEquals("2", localDateTimeTextField.getText());
    }
    
     @Test
    public void textFieldSetTextEnter() {
        Assert.assertTrue(find(".text-field").isFocused());

        localDateTimeTextField.setText("2");
        
        //We try to validate
        type(KeyCode.ENTER);

        // an error should have been thrown
        Assert.assertTrue(parseErrorThrowable.getMessage().contains("Text '2' could not be parsed"));
        
        //We should have the same value everywhere.
        Assert.assertEquals(((TextField) find(".text-field")).getText(), localDateTimeTextField.getText());
        Assert.assertTrue(localDateTimeTextField.getText().isEmpty());
    }
}
