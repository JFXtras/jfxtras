/**
 * CalendarTextFieldSkin.java
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarPicker.CalendarRange;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimePicker;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.layout.VBox;
import jfxtras.util.NodeUtil;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class CalendarTextFieldSkin extends SkinBase<CalendarTextField>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTextFieldSkin(CalendarTextField control)
	{
		super(control);
		construct();
	}

	/*
	 * 
	 */
	private void construct()
	{
		// setup component
		createNodes();
                
		// react to value changes in the model
		getSkinnable().calendarProperty().addListener( (observableValue, oldValue, newValue) -> { refreshValue(); });
        getSkinnable().dateFormatProperty().addListener( (observableValue, oldValue, newValue) -> { refreshValue(); });
        getSkinnable().textProperty().addListener( (observable) -> {
        	parse(getSkinnable().getText());
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
		String s = c == null ? "" : getSkinnable().getDateFormat().format( c.getTime() );
		textField.setText(s);
		if (s.equals(getSkinnable().getText()) == false) {
			getSkinnable().setText(s);
		}
	}
	
	/**
	 * When the control is focus, forward the focus to the textfield
	 */
    private void initFocusSimulation() 
    {
    	getSkinnable().focusedProperty().addListener( (observableValue, wasFocused, isFocused) -> {
			if (isFocused) {
            	Platform.runLater( () -> {
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
		textField.setPrefColumnCount(20);
		textField.focusedProperty().addListener( (observable) -> {
			if (textField.isFocused() == false) {
				parse();
			}
			// update derived properties on our control 
			focusForwardingProperty.set(textField.focusedProperty().get());
		});
		textField.setOnAction( (actionEvent) ->  {
			parse();
		});
		textField.setOnKeyPressed( (keyEvent) ->  {
			if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN) {
				// parse the content
				parse();
				
				// get the calendar to modify
				Calendar lCalendar = getSkinnable().getCalendar();
				if (lCalendar == null) return;
				lCalendar = (Calendar)lCalendar.clone();
				
				// modify
				int lField = Calendar.DATE;
				if (keyEvent.isShiftDown() == false && keyEvent.isControlDown()) lField = Calendar.MONTH;
				if (keyEvent.isShiftDown() == false && keyEvent.isAltDown()) lField = Calendar.YEAR;
				if (keyEvent.isShiftDown() == true && keyEvent.isControlDown() &&  getSkinnable().getShowTime()) lField = Calendar.HOUR_OF_DAY;
				if (keyEvent.isShiftDown() == true && keyEvent.isAltDown() &&  getSkinnable().getShowTime()) lField = Calendar.MINUTE;
				lCalendar.add(lField, keyEvent.getCode() == KeyCode.UP ? 1 : -1);
				
				// set it
				getSkinnable().setCalendar(lCalendar);
			}
		});
		// bind the textField's tooltip to our (so it will show up) and give it a default value describing the mutation features
		textField.tooltipProperty().bindBidirectional(getSkinnable().tooltipProperty()); 
		if (getSkinnable().getTooltip() == null)
		{
			// TODO: internationalize the tooltip
			getSkinnable().setTooltip(new Tooltip("Type a date or use # for today, or +/-<number>[d|w|m|y] for delta's (for example: -3m for minus 3 months)\nUse cursor up and down plus optional shift (week), ctrl (month) or alt (year) for quick keyboard changes."));
		}
        textField.promptTextProperty().bind(getSkinnable().promptTextProperty());

		// the icon
        imageView = new ImageViewButton();
		imageView.getStyleClass().add("icon");
		imageView.setPickOnBounds(true);
		imageView.setOnMouseClicked( (evt) -> {
			if (textField.focusedProperty().get() == true) {
				parse();
			}
                        getSkinnable().setPickerShowing(true);
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
	}
	private TextField textField = null;
	private ImageView imageView = null;
	private GridPane gridPane = null;
	final public BooleanProperty focusForwardingProperty = new SimpleBooleanProperty();

        /**
	 * parse the contents that was typed in the textfield
	 */
	private void parse() {
		parse(textField.getText());
	}
	
	/**
	 * parse the text
	 */
	private void parse(String lText)
	{
		try
		{
			// process the text 			
			lText = lText.trim();
			if (lText.length() == 0) 
			{
				if (getSkinnable().getAllowNull() == false) {
					throw new IllegalArgumentException("Empty string would result in null and that is not allowed");
				}
				getSkinnable().setCalendar(null);
				return;
			}
			
			// starts with - means substract days
			if (lText.startsWith("-") || lText.startsWith("+"))
			{
				// + has problems 
				if (lText.startsWith("+")) lText = lText.substring(1);
				
				// special units Day, Week, Month, Year
				// TODO: internationalize?
				int lUnit = Calendar.DATE;
				if (lText.toLowerCase().endsWith("d")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.DATE; }
				if (lText.toLowerCase().endsWith("w")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.WEEK_OF_YEAR; }
				if (lText.toLowerCase().endsWith("m")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.MONTH; }
				if (lText.toLowerCase().endsWith("y")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.YEAR; }
				
				// parse the delta
				int lDelta = Integer.parseInt(lText);
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar().clone();
				lCalendar.add(lUnit, lDelta);
				
				// set the value
				getSkinnable().setCalendar(lCalendar);
			}
			else if (lText.equals("#"))
			{
				// set the value
				getSkinnable().setCalendar(Calendar.getInstance(getSkinnable().getLocale()));
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
						catch (java.text.ParseException | java.time.format.DateTimeParseException e2) {
							// we can safely ignore this, since we will fall back to the default formatter in the end
							// TODO: do we want a way to show these error messages? System.out.println(e2.getMessage());
						} 
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
	
        
        private void setupPopup(){
            // create a picker
		calendarPicker = new CalendarPicker();
		calendarPicker.setMode(CalendarPicker.Mode.SINGLE);
		calendarPicker.localeProperty().set(getSkinnable().localeProperty().get());
		calendarPicker.allowNullProperty().set(getSkinnable().allowNullProperty().get());
		calendarPicker.calendarProperty().set(getSkinnable().calendarProperty().get());
                DateTimeToCalendarHelper.sync(calendarPicker.disabledCalendars(), getSkinnable().disabledCalendars());
                DateTimeToCalendarHelper.sync(calendarPicker.highlightedCalendars(), getSkinnable().highlightedCalendars());
                calendarPicker.displayedCalendar().set(getSkinnable().getDisplayedCalendar());
		calendarPicker.setCalendarRangeCallback(new Callback<CalendarRange,Void>() {
			@Override
			public Void call(CalendarRange calendarRange) {
				Callback<CalendarRange, Void> lCallback = getSkinnable().getCalendarRangeCallback();
				if (lCallback == null) {
					return null;
				}
				return lCallback.call(calendarRange);
			}
		});
		calendarPicker.setValueValidationCallback(new Callback<Calendar, Boolean>() {
			@Override
			public Boolean call(Calendar calendar) {
				Callback<Calendar, Boolean> lCallback = getSkinnable().getValueValidationCallback();
				if (lCallback == null) {
					return true;
				}
				return lCallback.call(calendar);
			}
		});
		
		// TODO: replace with PopupControl because that is styleable (see C:/Users/user/Documents/openjfx/8.0rt/modules/controls/src/main/java/com/sun/javafx/scene/control/skin/ComboBoxPopupControl.java)
//			popup = new PopupControl() {
//
//	            @Override public Styleable getStyleableParent() {
//	                return CalendarTextFieldSkin.this.getSkinnable();
//	            }
//	            {
//	                setSkin(new Skin<Skinnable>() {
//	                    @Override public Skinnable getSkinnable() { return CalendarTextFieldSkin.this.getSkinnable(); }
//	                    @Override public Node getNode() { return lBorderPane; }
//	                    @Override public void dispose() { }
//	                });
//	                getScene().getRoot().impl_processCSS(true);
//	            }
//	        };
                popup = new Popup();
                popup.setAutoFix(true);
                popup.setAutoHide(true);
                popup.setHideOnEscape(true);
		BorderPane lBorderPane = new BorderPane() {
			// As of 1.8.0_40 CSS files are added in the scope of a control, the popup does not fall under the control, so the stylesheet must be reapplied 
			// When JFxtras is based on 1.8.0_40+: @Override 
			public String getUserAgentStylesheet() {
				return getSkinnable().getUserAgentStylesheet();
			}
		};
		lBorderPane.getStyleClass().add(this.getClass().getSimpleName() + "_popup");
		lBorderPane.setCenter(calendarPicker);
		calendarPicker.showTimeProperty().set(getSkinnable().getShowTime());
		
		// because the Java 8 DateTime classes use the CalendarPicker, we need to add some specific CSS classes here to support seamless CSS
		if (getSkinnable().getStyleClass().contains(LocalDateTextField.class.getSimpleName())) {
			calendarPicker.getStyleClass().addAll(LocalDatePicker.class.getSimpleName());
		}
		if (getSkinnable().getStyleClass().contains(LocalDateTimeTextField.class.getSimpleName())) {
			calendarPicker.getStyleClass().addAll(LocalDateTimePicker.class.getSimpleName());
		}
		
		// add a close and accept button if we're showing time
		if ( getSkinnable().getShowTime())
		{
			VBox lVBox = new VBox(); 
			lBorderPane.rightProperty().set(lVBox);
			
			ImageView lAcceptIconImageView = new ImageViewButton();
			lAcceptIconImageView.getStyleClass().addAll("accept-icon");
			lAcceptIconImageView.setPickOnBounds(true);
			lAcceptIconImageView.setOnMouseClicked( (mouseEvent) ->  {
				getSkinnable().calendarProperty().set(calendarPicker.calendarProperty().get());
				popup.hide(); 
			});
			lVBox.add(lAcceptIconImageView);
			
			ImageView lCloseIconImageView = new ImageViewButton();
			lCloseIconImageView.getStyleClass().addAll("close-icon");
			lCloseIconImageView.setPickOnBounds(true);
			lCloseIconImageView.setOnMouseClicked( (mouseEvent) ->  {
				popup.hide(); 
			});
			lVBox.add(lCloseIconImageView);
		}
		
		// if a value is selected in date mode, immediately close the popup
		calendarPicker.calendarProperty().addListener( (observable) -> {
			if (getSkinnable().getShowTime() == false && popup.isShowing()) {
				popup.hide(); 
			}
		});

		// when the popup is hidden 
		popup.setOnHiding( (windowEvent) -> {
			// and time is not shown, the value must be set into the textfield
			if ( getSkinnable().getShowTime() == false) {
				getSkinnable().calendarProperty().set(calendarPicker.calendarProperty().get());
			}
			// but at least the textfield must be enabled again
			textField.setDisable(false);
		});
		
		// add to popup
		popup.getContent().setAll(lBorderPane);
		
		// show it just below the textfield
		textField.setDisable(true);
                
            /**
             * If the popup is showing/hiding, we must notify the property of
             * the CalendarTextField so that they are always in sync.
             */
            popup.showingProperty().addListener(new ChangeListener<Boolean>() {

                @Override
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
        private CalendarPicker calendarPicker = null;

    /**
     * This is either called by the user clicking on the button, or
     * programmaticaly with {@link CalendarTextField#setPickerShowing(boolean)
     * }.
     */
    private void showPopup() {
        if(popup == null){
            setupPopup();
        }
        
        popup.show(textField, NodeUtil.screenX(getSkinnable()), NodeUtil.screenY(getSkinnable()) + textField.getHeight());

        // move the focus over		
        calendarPicker.requestFocus(); // TODO: not working
    }
}
