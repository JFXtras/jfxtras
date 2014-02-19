/**
 * CalendarTextFieldCaspianSkin.java
 *
 * Copyright (c) 2011-2014, JFXtras
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
import java.util.GregorianCalendar;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimePicker;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.util.NodeUtil;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class CalendarTextFieldCaspianSkin extends SkinBase<CalendarTextField>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTextFieldCaspianSkin(CalendarTextField control)
	{
		super(control);//, new CalendarTextFieldBehavior(control));
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
		getSkinnable().calendarProperty().addListener( (observableValue, oldValue, newValue) -> { refreshValue(); });
        getSkinnable().dateFormatProperty().addListener( (observableValue, oldValue, newValue) -> { refreshValue(); });
		refreshValue();
		
		// focus
		initFocusSimulation();
	}
	
	/*
	 * 
	 */
	private void refreshValue()
	{
		// write out to textfield
		Calendar c = getSkinnable().getCalendar();
		String s = c == null ? "" : getSkinnable().getDateFormat().format( c.getTime() );
		textField.setText( s );
	}
	
	/**
	 * When the control is focus, forward the focus to the textfield
	 */
    private void initFocusSimulation() 
    {
    	getSkinnable().focusedProperty().addListener(new ChangeListener<Boolean>() 
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean wasFocused, Boolean isFocused) 
			{
				if (isFocused) 
				{
                	Platform.runLater(new Runnable() 
                	{
						@Override
						public void run() 
						{
							textField.requestFocus();
						}
					});
				}
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
					Calendar lCalendar = getSkinnable().getCalendar();
					if (lCalendar == null) return;
					lCalendar = (Calendar)lCalendar.clone();
					
					// modify
					int lField = Calendar.DATE;
					if (keyEvent.isShiftDown() == false && keyEvent.isControlDown()) lField = Calendar.MONTH;
					if (keyEvent.isShiftDown() == false && keyEvent.isAltDown()) lField = Calendar.YEAR;
					if (keyEvent.isShiftDown() == true && keyEvent.isControlDown() && isShowingTime()) lField = Calendar.HOUR_OF_DAY;
					if (keyEvent.isShiftDown() == true && keyEvent.isAltDown() && isShowingTime()) lField = Calendar.MINUTE;
					lCalendar.add(lField, keyEvent.getCode() == KeyCode.UP ? 1 : -1);
					
					// set it
					getSkinnable().setCalendar(lCalendar);
				}
			}
		});
		// bind the textField's tooltip to our (so it will show up) and give it a default value describing the mutation features
		textField.tooltipProperty().bindBidirectional(getSkinnable().tooltipProperty()); 
		if (getSkinnable().getTooltip() == null)
		{
			// TODO: internationalize the tooltip
			getSkinnable().setTooltip(new Tooltip("Type a date or use # for today, or +/-<number>[d|w|m|y] for delta's (for example: -3m for minus 3 months)/nUse cursor up and down plus optional shift (week), ctrl (month) or alt (year) for quick keyboard changes."));
		}
        textField.promptTextProperty().bind(getSkinnable().promptTextProperty());

		// the icon
        imageView = new ImageView();
		imageView.getStyleClass().add("icon");
		imageView.setPickOnBounds(true);
		imageView.setOnMouseClicked( (evt) -> {
			if (textField.focusedProperty().get() == true) {
				parse();
			}
			showPopup(evt);
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
		calendarPicker = new CalendarPicker();
		calendarPicker.setMode(CalendarPicker.Mode.SINGLE);
		// bind our properties to the picker's 
		Bindings.bindBidirectional(calendarPicker.localeProperty(), getSkinnable().localeProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		Bindings.bindBidirectional(calendarPicker.calendarProperty(), getSkinnable().calendarProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		calendarPicker.calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observable, Calendar oldValue, Calendar newValue)
			{
				if (popup != null && isShowingTime() == false) 
				{
					popup.hide(); popup = null;
				}
			}
		});
		
		// close icon
		closeIconImage = new Image(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + "CloseWindowIcon.png"));
	}
	private TextField textField = null;
	private ImageView imageView = null;
	private GridPane gridPane = null;
	private CalendarPicker calendarPicker = null;
	private Image closeIconImage = null;

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
				
				// special units Day, Week, Month, Year
				// TODO: internationalize?
				int lUnit = Calendar.DATE;
				if (lText.toLowerCase().endsWith("d")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.DATE; }
				if (lText.toLowerCase().endsWith("w")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.WEEK_OF_YEAR; }
				if (lText.toLowerCase().endsWith("m")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.MONTH; }
				if (lText.toLowerCase().endsWith("y")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.YEAR; }
				
				// parse the delta
				int lDelta = Integer.parseInt(lText);
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar().clone(); // TODO locale
				lCalendar.add(lUnit, lDelta);
				
				// set the value
				getSkinnable().setCalendar(lCalendar);
			}
			else if (lText.equals("#"))
			{
				// set the value
				getSkinnable().setCalendar(Calendar.getInstance()); // TODO locale
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
					lCalendar = Calendar.getInstance(); // TODO: how to get the correct locale
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
	
	/**
	 * Detect if the control is showing time or not
	 * This is done by formatting a date with contains a time and checking the formatted string if the values for time are present 
	 * @return
	 */
	private boolean isShowingTime()
	{
		String lDateAsString = getSkinnable().dateFormatProperty().get().format(DATE_WITH_TIME);
		return lDateAsString.contains("2");
	}
	private final static Date DATE_WITH_TIME = new GregorianCalendar(1111,0,1,2,2,2).getTime();
	
	/*
	 * 
	 */
	private void showPopup(MouseEvent evt)
	{
		// create popup
		if (popup == null)
		{
			// TODO: replace with PopupControl because that is styleable (see C:/Users/user/Documents/openjfx/8.0rt/modules/controls/src/main/java/com/sun/javafx/scene/control/skin/ComboBoxPopupControl.java)
//			popup = new PopupControl() {
//
//	            @Override public Styleable getStyleableParent() {
//	                return CalendarTextFieldCaspianSkin.this.getSkinnable();
//	            }
//	            {
//	                setSkin(new Skin<Skinnable>() {
//	                    @Override public Skinnable getSkinnable() { return CalendarTextFieldCaspianSkin.this.getSkinnable(); }
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
			BorderPane lBorderPane = new BorderPane();
			lBorderPane.getStyleClass().add(this.getClass().getSimpleName() + "_popup");
			lBorderPane.setCenter(calendarPicker);
			calendarPicker.showTimeProperty().set( isShowingTime() );
			
			// because the Java 8 DateTime classes use the CalendarPicker, we need to add some specific CSS classes here to support seamless CSS
			if (getSkinnable().getStyleClass().contains(LocalDateTextField.class.getSimpleName())) {
				calendarPicker.getStyleClass().addAll(LocalDatePicker.class.getSimpleName());
			}
			if (getSkinnable().getStyleClass().contains(LocalDateTimeTextField.class.getSimpleName())) {
				calendarPicker.getStyleClass().addAll(LocalDateTimePicker.class.getSimpleName());
			}
			
			// add a close button
			if (isShowingTime() == true)
			{
				ImageView lImageView = new ImageView(closeIconImage);
				lImageView.setPickOnBounds(true);
				lImageView.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override public void handle(MouseEvent evt)
					{
						popup.hide(); popup = null;
					}
				});
				lBorderPane.rightProperty().set(lImageView);
			}
			
			// add to popup
			popup.getContent().add(lBorderPane);
		}
		
		// show it just below the textfield
		popup.show(textField, NodeUtil.screenX(getSkinnable()), NodeUtil.screenY(getSkinnable()) + textField.getHeight());

		// move the focus over
		// TODO: not working
		calendarPicker.requestFocus();
	}
	private Popup popup = null;
}
