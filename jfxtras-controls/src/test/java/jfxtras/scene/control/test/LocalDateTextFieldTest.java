/**
 * LocalDateTextFieldTest.java
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
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * Created by Samir Hadzic on 21-05-14.
 */
public class LocalDateTextFieldTest extends JFXtrasGuiTest {

    /**
     *
     */
    public Parent getRootNode() {
        Locale.setDefault(Locale.ENGLISH);

        HBox box = new HBox();
        localDateTextField = new LocalDateTextField();
        localDateTextField.setParseErrorCallback( throwable -> { 
        	parseErrorThrowable = throwable;
        	System.out.println("Parse exception caught: " + throwable);
        	return null;
        });
        box.getChildren().add(localDateTextField);
        Button lButton = new Button("focus helper");
        lButton.setId("focusHelper");
        box.getChildren().add(lButton);

        return box;
    }
    private LocalDateTextField localDateTextField = null;
    private Throwable parseErrorThrowable = null;

    /**
     *
     */
    @Test
    public void defaultModeIsNull() {
        // default value is null
        Assert.assertNull(localDateTextField.getLocalDate());
    }

    /**
     *
     */
    @Test
    public void textfieldIsDisabledWhenPopupIsOpen() {
        // default value is null
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTextField.isPickerShowing());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(find(".text-field").isDisabled());

        // click today
        clickOn(".today");

        // now should be the value in the textfield
        Assert.assertFalse(find(".text-field").isDisabled());
        Assert.assertFalse(localDateTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void selectDateInPopup() {
        // default value is null
        Assert.assertNull(localDateTextField.getLocalDate());
        Assert.assertFalse(localDateTextField.isPickerShowing());

        // open the popup
        clickOn(".icon");

        //The popup should be displayed.
        Assert.assertTrue(localDateTextField.isPickerShowing());

        // click today
        clickOn(".today");

        // now should be the value in the textfield
        Assert.assertEquals(LocalDate.now(), localDateTextField.getLocalDate());

        //The popup should be hidden.
        Assert.assertFalse(localDateTextField.isPickerShowing());
    }

    @Test
    public void checkIfDisabledDateIsForwardedToThePicker() {
    	// make today disabled   
    	localDateTextField.disabledLocalDates().add(LocalDate.now());
        
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
        	localDateTextField.disabledLocalDates().add(LocalDate.now());
        });
        
        // make sure that today in the picker is disabled
        Assert.assertTrue(find(".today").isDisabled());
    }

    @Test
    public void checkIfHighlightedDateIsForwardedToThePicker() {
    	// make today Highlighted   
    	localDateTextField.highlightedLocalDates().add(LocalDate.now());
        
        // open the picker
        clickOn(".icon");

        // make sure that today in the picker is Highlighted
        Assert.assertTrue(find(".today").getStyleClass().contains("highlight"));
    }

    @Test
    public void checkIfHighlightedDateIsForwardedToThePickerAtRuntime() {
        // open the picker
        clickOn(".icon");
        TestUtil.waitForPaintPulse();
        
        // make sure that today in the picker is enabled
        Assert.assertFalse(find(".today").getStyleClass().contains("highlight"));
        
    	// make today Highlighted   
        TestUtil.runThenWaitForPaintPulse(() -> {
        	localDateTextField.highlightedLocalDates().add(LocalDate.now());
        });
        
        // make sure that today in the picker is Highlighted
        Assert.assertTrue(find(".today").getStyleClass().contains("highlight"));
    }
    
    /**
     *
     */
    @Test
    public void popupWithCalendarSet() {
        LocalDate localDate = LocalDate.of(2013, 1, 1);

        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setLocalDate(localDate);
        });

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(localDateTextField.isPickerShowing());

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
        Assert.assertFalse(localDateTextField.isPickerShowing());

        // open the popup
        clickOn(".icon");
        Assert.assertTrue(localDateTextField.isPickerShowing());

        // popup should be open
        assertPopupIsVisible(find(".text-field"));

        // send esc
        press(KeyCode.ESCAPE);

        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void nullAllowed() {
        // default value is null
        Assert.assertNull(localDateTextField.getLocalDate());

        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setAllowNull(false);
        });

        Assert.assertNotNull(localDateTextField.getLocalDate());
    }

    /**
     *
     */
    @Test
    public void nullNotAllowed() {
        // set null not allowed
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setAllowNull(false);
        });
        AtomicBoolean lParseErrorCallbackWasCalled = new AtomicBoolean(false);
        localDateTextField.setParseErrorCallback((throwable) -> {
            lParseErrorCallbackWasCalled.set(true);
            return null;
        });

        // then clear the textfield
        clear(localDateTextField);

        // move focus away
        clickOn("#focusHelper");

        // check for result
        Assert.assertTrue(lParseErrorCallbackWasCalled.get());
    }

    /**
     *
     */
    @Test
    public void nullAllowedAndUnselectInPopup() {
        // set a value
        LocalDate localDate = LocalDate.of(2013, 1, 1);
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setLocalDate(localDate);
        });

        // default value is not null
        Assert.assertEquals(localDate, localDateTextField.getLocalDate());

        // open the popup
        clickOn(".icon");

        // click the 1st of January
        clickOn("#2013-01-01");

        // value is null
        Assert.assertNull(localDateTextField.getLocalDate());
    }

    /**
     *
     */
    @Test
    public void nullNotAllowedAndAttemptUnselectInPopup() {
        // set a value
        LocalDate localDate = LocalDate.of(2013, 1, 1);
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setLocalDate(localDate);
            localDateTextField.setAllowNull(false);
        });

        // default value is not null
        Assert.assertEquals(localDate, localDateTextField.getLocalDate());

        // open the popup
        clickOn(".icon");

        // click the 1st of January
        clickOn("#2013-01-01");

        // value is still not null
        Assert.assertEquals(localDate, localDateTextField.getLocalDate());
    }

    /**
     *
     */
    @Test
    public void typeValue() {
        // default value is null
        Assert.assertNull(localDateTextField.getLocalDate());

        // type value
        clickOn(localDateTextField).write(localDateTextField.getDateTimeFormatter().format(LocalDate.of(2014, 12, 31)));

        // move focus away
        clickOn("#focusHelper");

        // now should be the value in the textfield
        Assert.assertEquals("2014-12-31", TestUtil.quickFormatLocalDateAsDate(localDateTextField.getLocalDate()));
    }

    /**
     *
     */
    @Test
    public void typeValueUsingAdditonalDateFormatter() {
        // default value is null
        Assert.assertNull(localDateTextField.getLocalDate());

        // add a second formatter
        localDateTextField.dateTimeFormattersProperty().add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // type value
        clickOn(localDateTextField).write("2014-12-31");

        // move focus away
        clickOn("#focusHelper");
        // for some reason the focus is not moved always
        clickOn(".CalendarTextField");
        clickOn("#focusHelper");

        // now should be the value in the textfield
        Assert.assertEquals("2014-12-31", TestUtil.quickFormatLocalDateAsDate(localDateTextField.getLocalDate()));
    }

    /**
     *
     */
    @Test
    public void setValue() {
        // default value is null
        Assert.assertNull(localDateTextField.getLocalDate());

        // set value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.dateTimeFormatterProperty().set(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            localDateTextField.setLocalDate(LocalDate.of(2014, 12, 31));
        });

        // now should be the value in the textfield
        Assert.assertEquals("2014-12-31", localDateTextField.getText());
    }

    /**
     *
     */
    @Test
    public void popupIsClosedWhenReselectingSameDateInNotNullMode() {
        LocalDate localDate = LocalDate.of(2013, 1, 1);

        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setLocalDate(localDate);
            localDateTextField.setAllowNull(false);
        });

        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTextField.isPickerShowing());

        // open the popup
        clickOn(".icon");

        // popup should be closed
        assertPopupIsVisible(find(".text-field"));
        Assert.assertTrue(localDateTextField.isPickerShowing());

        // reselect 1st of January
        clickOn("#2013-01-01");

        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTextField.isPickerShowing());
    }

    /**
     *
     */
    @Test
    public void openPopupAndCloseWithProperty() {
        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTextField.isPickerShowing());

        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setPickerShowing(true);
        });

        // popup should be open
        assertPopupIsVisible(find(".text-field"));
        Assert.assertTrue(localDateTextField.isPickerShowing());

        // Close the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setPickerShowing(false);
        });

        // popup should be closed
        assertPopupIsNotVisible(find(".text-field"));
        Assert.assertFalse(localDateTextField.isPickerShowing());
    }

    @Test
    public void setDisplayedCalendar() {
        LocalDate localDate = LocalDate.of(2013, 1, 1);
        localDateTextField.setDisplayedLocalDate(localDate);

        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setPickerShowing(true);
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
            localDateTextField.requestFocus();
        });

        //TextField should be now
        Assert.assertTrue(find(".text-field").isFocused());
    }
    
    @Test
    public void selectAll() {
        LocalDate localDate = LocalDate.of(2013, 1, 1);

        // set a value
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setLocalDate(localDate);
        });
        
        TextField textField = (TextField)find(".text-field");
        Assert.assertFalse(textField.getText().isEmpty());

        localDateTextField.selectAll();

        Assert.assertEquals(textField.getText(), textField.getSelectedText());
    }
    
    @Test
    public void inputAfterEscape() {
        // open the popup
        TestUtil.runThenWaitForPaintPulse(() -> {
            localDateTextField.setPickerShowing(true);
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
        Assert.assertEquals(((TextField) find(".text-field")).getText(), localDateTextField.getText());
        Assert.assertEquals("2", localDateTextField.getText());
    }
    
    @Test
    public void textFieldSetText() {
        Assert.assertTrue(find(".text-field").isFocused());

        localDateTextField.setText("2");

        // no error should have been thrown
        Assert.assertNull(parseErrorThrowable);
        
        //We should have the same value everywhere.
        Assert.assertEquals(((TextField) find(".text-field")).getText(), localDateTextField.getText());
        Assert.assertEquals("2", localDateTextField.getText());
    }
    
     @Test
    public void textFieldSetTextEnter() {
        Assert.assertTrue(find(".text-field").isFocused());

        localDateTextField.setText("2");
        
        //We try to validate
        type(KeyCode.ENTER);

        // an error should have been thrown
        Assert.assertTrue(parseErrorThrowable.getMessage().contains("Text '2' could not be parsed"));
        
        //We should have the same value everywhere.
        Assert.assertEquals(((TextField) find(".text-field")).getText(), localDateTextField.getText());
        Assert.assertTrue(localDateTextField.getText().isEmpty());
    }
}
