/**
 * LocalDateTimeTextFieldTest.java
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jfxtras.scene.control.LocalDateTimePicker;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

import org.junit.Assert;
import org.junit.Test;

import static org.loadui.testfx.GuiTest.find;

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
        box.getChildren().add(localDateTimeTextField);
        Button lButton = new Button("focus helper");
        lButton.setId("focusHelper");
        box.getChildren().add(lButton);

        return box;
    }
    private LocalDateTimeTextField localDateTimeTextField = null;

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
        click(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        // click today
        click(".today");

        // click today
        click(".accept-icon");

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
        click(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        //The popup should be displayed.
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        click(".today");

        // click today
        click(".accept-icon");

        // now should be the value in the textfield
        Assert.assertEquals(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), localDateTimeTextField.getLocalDateTime());
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
        click(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        //The popup should be displayed.
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        click(".today");

        // click close
        click(".close-icon");

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
        click(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        click(".today");

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
        click(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());
        Assert.assertTrue(localDateTimeTextField.isPickerShowing());

        // click today
        click(".today");

        // move focus away
        click("#focusHelper");

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
        click(".icon");
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
        click(".icon");
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
        click("#focusHelper");

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
        click(".icon");

        // click the 1st of January
        click("#2013-01-01");

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
        click(localDateTimeTextField).type("2014-12-31 00-00-00");

        // move focus away
        click("#focusHelper");

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
}
