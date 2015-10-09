/**
 * CalendarTimeTextFieldSkin.java
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

package jfxtras.internal.scene.control.skin;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.scene.control.CalendarTimeTextField;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.util.NodeUtil;

/**
 * Allows editing the time in a text field.
 * Format is hardcoded to HH:MM:SS.mmm, but  
 * @author Tom Eugelink
 */
public class CalendarTimeTextFieldSkin extends SkinBase<CalendarTimeTextField>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTimeTextFieldSkin(CalendarTimeTextField control)
	{
		super(control);//, new CalendarTimeTextFieldBehavior(control));
		construct();
		// show where the skin is loaded from (for debugging in Ensemble) System.out.println("!!! " + this.getClass().getProtectionDomain().getCodeSource().getLocation());
	}

	/*
	 * 
	 */
	private void construct()
	{
		// setup component
		createNodes();
		
		// react to value changes in the model
		getSkinnable().calendarProperty().addListener( (observable) -> {
			refreshValue();
		});
		getSkinnable().dateFormatProperty().addListener( (observable) -> {			
			refreshValue();
		});
		refreshValue();
		
		// focus
		initFocusSimulation();
            

            /**
             * If the user is triggering the property, we must show the popup.
             * We cannot bind the property directly to the popup because the
             * popup property is read only. MoreOver, we want to show the popup
             * next to the TextField, that's why we need to call showPopup();
             */
            getSkinnable().pickerShowingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean showing) -> {
                if (showing) {
                    showPopup();
                } else if(popup != null){
                    popup.hide();
                }
            });
	}
	
	/*
	 * 
	 */
	private void refreshValue()
	{
		// write out to textfield
		Calendar c = getSkinnable().getCalendar();
		String s = (c == null ? "" : getSkinnable().getDateFormat().format(c.getTime()));
		textField.setText( s );
	}
	
	/**
	 * When the control is focus, forward the focus to the textfield
	 */
    private void initFocusSimulation() 
    {
    	getSkinnable().focusedProperty().addListener( (observable, wasFocused, isFocused) -> {
			if (isFocused) {
            	Platform.runLater( () ->  {
					textField.requestFocus();
				});
			}
		});
    }
	

	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// the main textField
		textField = new TextField();
		textField.focusedProperty().addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable arg0)
			{
				if (textField.isFocused() == false) 
				{
					parse();
				}
			}
		});
		textField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent evt)
			{
				parse();
			}
		});
		textField.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent keyEvent)
			{
				if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN)
				{
					// parse the content
					parse();
					
					// get the calendar to modify
					Calendar lCalendar = (Calendar)getSkinnable().getCalendar().clone();
					
					// modify
					if (keyEvent.isControlDown()) lCalendar.add(Calendar.HOUR_OF_DAY, keyEvent.getCode() == KeyCode.UP ? 1 : -1);
					else lCalendar.add(Calendar.MINUTE, keyEvent.getCode() == KeyCode.UP ? getSkinnable().getMinuteStep() : -1 * getSkinnable().getMinuteStep());
					
					// set it
					getSkinnable().setCalendar( CalendarTimePickerSkin.blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep()) );
				}
			}
		});
		// bind the textField's tooltip to our (so it will show up) and give it a default value describing the mutation features
		textField.tooltipProperty().bindBidirectional(getSkinnable().tooltipProperty()); 
		if (getSkinnable().getTooltip() == null)
		{
			// TODO: internationalize the tooltip
			getSkinnable().setTooltip(new Tooltip("Type a time or use # for now, or +/-<number>[h|m] for delta's (for example: -3m for minus 3 minutes)\nUse cursor up and down plus optional ctrl (hour) for quick keyboard changes."));
		}
        textField.promptTextProperty().bind(getSkinnable().promptTextProperty());

		// the icon
		imageView = new ImageViewButton();
		imageView.getStyleClass().add("icon");
		imageView.setPickOnBounds(true);
		imageView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				if (textField.focusedProperty().get() == true) 
				{
					parse();
				}
				getSkinnable().setPickerShowing(true);
			}
		});

		// construct a gridpane: one row, two columns
		gridPane = new GridPane();
		gridPane.setHgap(3);
		gridPane.add(textField, 0, 0);
		gridPane.add(imageView, 1, 0);
		ColumnConstraints column0 = new ColumnConstraints(100, 10, Double.MAX_VALUE);
		column0.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().addAll(column0); // first column gets any extra width
		
		// add to self
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(gridPane);
		
		// prep the picker
		calendarTimePicker = new CalendarTimePicker();
		// bind our properties to the picker's 
		//Bindings.bindBidirectional(calendarTimePicker.calendarProperty(), getSkinnable().calendarProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		Bindings.bindBidirectional(calendarTimePicker.minuteStepProperty(), getSkinnable().minuteStepProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		Bindings.bindBidirectional(calendarTimePicker.secondStepProperty(), getSkinnable().secondStepProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
	}
	private TextField textField = null;
	private ImageView imageView = null;
	private GridPane gridPane = null;
	private CalendarTimePicker calendarTimePicker = null;
	
	/**
	 * parse the contents that was typed in the textfield
	 */
	private void parse()
	{
		try
		{
			// get the text to parse
			String lText = textField.getText();
			lText = lText.trim();
			if (lText.length() == 0) 
			{
				getSkinnable().setCalendar(null);
				return;
			}
			
			// starts with - means substract days
			if (lText.startsWith("-") || lText.startsWith("+"))
			{
				// + has problems 
				if (lText.startsWith("+")) lText = lText.substring(1);
				
				// special units hour, minute
				// TODO: internationalize?
				int lUnit = Calendar.DATE;
				if (lText.toLowerCase().endsWith("m")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.MINUTE; }
				if (lText.toLowerCase().endsWith("h")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.HOUR_OF_DAY; }
				
				// parse the delta
				int lDelta = Integer.parseInt(lText);
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar().clone(); 
				lCalendar.add(lUnit, lDelta);
				
				// set the value
				getSkinnable().setCalendar( CalendarTimePickerSkin.blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep()) );
			}
			else if (lText.equals("#"))
			{
				// set the value
				getSkinnable().setCalendar( CalendarTimePickerSkin.blockMinutesToStep(Calendar.getInstance(getSkinnable().getLocale()), getSkinnable().getMinuteStep()) ); 
			}
			else
			{
				try
				{
					Calendar lCalendar = getSkinnable().getCalendar();
					Date lDate = null;
					// First we're going to try the parsers in the list.
					// The user is free to decide to sequence here, if we would try the default first, that would not be the case.
					for (DateFormat lDateFormat : getSkinnable().getDateFormats())
					{
						try
						{
							// parse using the formatter
							lDate = lDateFormat.parse( lText );
							break; // exit the for loop
						}
						catch (java.text.ParseException e2) {} // we can safely ignore this, since we will fall back to the default formatter in the end
					}
					if (lDate == null) 
					{
						// parse using the default formatter
						lDate = getSkinnable().getDateFormat().parse( lText );
					}
					
					// set the value (the parse with the default formatter either succeeded or threw an exception, skipping this code)
					lCalendar = Calendar.getInstance(getSkinnable().getLocale()); 
					lCalendar.setTime(lDate);
					getSkinnable().setCalendar(lCalendar);
				}
				finally
				{
					// always refresh
					refreshValue();
				}
			}
		}
		catch (Throwable t) 
		{ 
			// handle the exception
			// TODO: implement a default handler (show in popup / validation icon) 
			if (getSkinnable().getParseErrorCallback() != null) {
				getSkinnable().getParseErrorCallback().call(t);
			}
			else {
				t.printStackTrace();
			}
		} 
	}
	
        
       private void setupPopup() {
        popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        // add the timepicker
        BorderPane lBorderPane = new BorderPane() {
            // As of 1.8.0_40 CSS files are added in the scope of a control, the popup does not fall under the control, so the stylesheet must be reapplied 
            // When JFxtras is based on 1.8.0_40+: @Override 
            public String getUserAgentStylesheet() {
                return getSkinnable().getUserAgentStylesheet();
            }
        };
        lBorderPane.getStyleClass().add(this.getClass().getSimpleName() + "_popup");
        lBorderPane.setCenter(calendarTimePicker);

        // add a close button
        ImageView lImageView = new ImageViewButton();
        lImageView.getStyleClass().addAll("close-icon");
        lImageView.setPickOnBounds(true);
        lImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                popup.hide();
                popup = null;
                getSkinnable().calendarProperty().set(calendarTimePicker.calendarProperty().get());
            }
        });
        lBorderPane.rightProperty().set(lImageView);

        // add pane
        popup.getContent().add(lBorderPane);
        popup.setOnShown((event) -> {
            ((CalendarTimePickerSkin) calendarTimePicker.getSkin()).labelDateFormatProperty().set(getSkinnable().getDateFormat());
        });

        /**
         * If the popup is showing/hiding, we must notify the property of the
         * CalendarTextField so that they are always in sync.
         */
        popup.showingProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean showing) {
                getSkinnable().setPickerShowing(showing);
                if (!showing) {
                    popup.showingProperty().removeListener(this);
                    popup = null;
                }
            }
        });
    }
    private Popup popup = null;
    private CalendarTimePicker calendarPicker = null;
	   /*
     * 
     */

    private void showPopup() {

        if(popup == null){
            setupPopup();
        }
        // show it just below the textfield
        calendarTimePicker.calendarProperty().set(getSkinnable().calendarProperty().get());
        popup.show(textField, NodeUtil.screenX(getSkinnable()), NodeUtil.screenY(getSkinnable()) + textField.getHeight());

		// move the focus over
        // TODO: not working
        //TimePicker.requestFocus();
    }
}
