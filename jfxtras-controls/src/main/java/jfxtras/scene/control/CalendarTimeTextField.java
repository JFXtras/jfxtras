/**
 * CalendarTimeTextField.java
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

package jfxtras.scene.control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javafx.beans.property.BooleanProperty;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * A textField with displays a calendar (time) with a icon to popup the CalendarTimePicker
 * The calendar is (and should) be treated as immutable. That means the setter is not used, but when a value is changed a new instance (clone) is put in the calendar property.
 * 
 * To change the icon use:
 * .CalendarTimeTextField .icon  {
 *     -fx-image: url("AlternateCalendarIcon.jpg");
 * }
 * 
 * @author Tom Eugelink
 */
public class CalendarTimeTextField extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTimeTextField()
	{
		construct();
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(CalendarTimeTextField.class.getSimpleName());
		
		// this is apparently needed for good focus behavior
		setFocusTraversable(false);
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return CalendarTimeTextField.class.getResource("/jfxtras/internal/scene/control/" + CalendarTimeTextField.class.getSimpleName() + ".css").toExternalForm();
	}
	
	@Override public Skin<?> createDefaultSkin() {
		return new jfxtras.internal.scene.control.skin.CalendarTimeTextFieldSkin(this); 
	}
	
	// ==================================================================================================================
	// PROPERTIES

	/** Id */
	public CalendarTimeTextField withId(String value) { setId(value); return this; }

	/** Calendar: */
	public ObjectProperty<Calendar> calendarProperty() { return calendarObjectProperty; }
	final private ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar", null);
	public Calendar getCalendar() { return calendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { calendarObjectProperty.setValue(value); }
	public CalendarTimeTextField withCalendar(Calendar value) { setCalendar(value); return this; }

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault())
	{
		public void set(Locale value)
		{
			super.set(value);
			if (dateFormatManual == false)
			{
				setDateFormat( null ); // forces a resetting
			}
		}
	};
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public CalendarTimeTextField withLocale(Locale value) { setLocale(value); return this; } 
	
	/** 
	 * The DateFormat used to render/parse the date in the textfield.
	 * It is allow to show time as well for example by SimpleDateFormat.getDateTimeInstance().
	 */
	public ObjectProperty<DateFormat> dateFormatProperty() { return dateFormatObjectProperty; }
	final private ObjectProperty<DateFormat> dateFormatObjectProperty = new SimpleObjectProperty<DateFormat>(this, "dateFormat", SimpleDateFormat.getTimeInstance(DateFormat.SHORT, getLocale()))
	{
		public void set(DateFormat value)
		{
			if (value != null) {
				String lFormattedDate = value.format(DATE_WITH_TIME);
				// the date has 000 for milliseconds, so that will never generate a "1"
				if (lFormattedDate.contains("1")) throw new IllegalArgumentException("The date format may only show time");
			}
			super.set( value != null ? value : SimpleDateFormat.getTimeInstance(DateFormat.SHORT, getLocale()));
			dateFormatManual = (value != null);
		}
	};
	public DateFormat getDateFormat() { return dateFormatObjectProperty.getValue(); }
	public void setDateFormat(DateFormat value) { dateFormatObjectProperty.setValue(value); }
	public CalendarTimeTextField withDateFormat(DateFormat value) { setDateFormat(value); return this; }
	private final static Date DATE_WITH_TIME = new GregorianCalendar(1111,0,1,9,33,44).getTime();
	private boolean dateFormatManual = false;

	/** MinuteStep */
	public ObjectProperty<Integer> minuteStepProperty() { return minuteStepProperty; }
	final private SimpleObjectProperty<Integer> minuteStepProperty = new SimpleObjectProperty<Integer>(this, "minuteStep", 1);
	public Integer getMinuteStep() { return minuteStepProperty.getValue(); }
	public void setMinuteStep(Integer value) { minuteStepProperty.setValue(value); }
	public CalendarTimeTextField withMinuteStep(Integer value) { setMinuteStep(value); return this; } 

	/** SecondStep */
	public ObjectProperty<Integer> secondStepProperty() { return secondStepProperty; }
	final private SimpleObjectProperty<Integer> secondStepProperty = new SimpleObjectProperty<Integer>(this, "secondStep", 1);
	public Integer getSecondStep() { return secondStepProperty.getValue(); }
	public void setSecondStep(Integer value) { secondStepProperty.setValue(value); }
	public CalendarTimeTextField withSecondStep(Integer value) { setSecondStep(value); return this; }
	
	/** PromptText: */
	public ObjectProperty<String> promptTextProperty() { return promptTextObjectProperty; }
	final private ObjectProperty<String> promptTextObjectProperty = new SimpleObjectProperty<String>(this, "promptText", null);
	public String getPromptText() { return promptTextObjectProperty.get(); }
	public void setPromptText(String value) { promptTextObjectProperty.set(value); }
	public CalendarTimeTextField withPromptText(String value) { setPromptText(value); return this; }

	/** parse error callback:
	 * If something did not parse correctly, you may handle it. 
	 * Otherwise the exception will be logged on the console.
	 */
	public ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackProperty() { return parseErrorCallbackObjectProperty; }
	final private ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackObjectProperty = new SimpleObjectProperty<Callback<Throwable, Void>>(this, "parseErrorCallback", null);
	public Callback<Throwable, Void> getParseErrorCallback() { return this.parseErrorCallbackObjectProperty.getValue(); }
	public void setParseErrorCallback(Callback<Throwable, Void> value) { this.parseErrorCallbackObjectProperty.setValue(value); }
	public CalendarTimeTextField withParseErrorCallback(Callback<Throwable, Void> value) { setParseErrorCallback(value); return this; }

	/** DateFormats: a list of alternate dateFormats used for parsing only */
	public ListProperty<DateFormat> dateFormatsProperty() { return dateFormatsProperty; }
	ListProperty<DateFormat> dateFormatsProperty = new SimpleListProperty<DateFormat>(javafx.collections.FXCollections.observableList(new ArrayList<DateFormat>()));
	public ObservableList<DateFormat> getDateFormats() { return dateFormatsProperty.getValue(); }
	public void setDateFormats(ObservableList<DateFormat> value) { dateFormatsProperty.setValue(value); }
	public CalendarTimeTextField withDateFormat(ObservableList<DateFormat> value) { setDateFormats(value); return this; }


       /**
     * Represents the current state of the Picker popup, and whether it is
     * currently visible on screen.
     */
    public BooleanProperty pickerShowingProperty() { return pickerShowingProperty; }
    final private BooleanProperty pickerShowingProperty = new SimpleBooleanProperty();
    public boolean isPickerShowing() { return pickerShowingProperty.get(); }
    public void setPickerShowing(boolean value) { pickerShowingProperty.set(value); }
	// ==================================================================================================================
	// EVENTS
	
	// ==================================================================================================================
	// BEHAVIOR
	
}
