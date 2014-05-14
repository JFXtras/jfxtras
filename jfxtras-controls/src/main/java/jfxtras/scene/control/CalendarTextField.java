/**
 * CalendarTextField.java
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

package jfxtras.scene.control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker.CalendarRange;

/**
 * A textField which displays a calendar (date) with a icon to popup the CalendarPicker
 * The calendar is (and should) be treated as immutable. That means the Calendar's setters are not used, but when a value is changed a new instance (clone) is put in the calendar property.
 * Features relative mutation options, like -1 or -1d for yesterday, -1m for minus one month, +1w, +2y. # is today.
 * 
 * To change the icon use:
 * .CalendarTextField .icon  {
 *     -fx-image: url("AlternateCalendarIcon.jpg");
 * }
 *
 * The textField can also show time by specifying a DateFormat accordingly, e.g. setDateFormat(SimpleDateFormat.getDateTimeInstance());
 * 
 * @author Tom Eugelink
 */
public class CalendarTextField extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTextField()
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
		this.getStyleClass().add(this.getClass().getSimpleName());
		
		// this is apparently needed for good focus behavior
		setFocusTraversable(false);
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/internal/scene/control/" + CalendarTextField.class.getSimpleName() + ".css").toExternalForm();
	}
	
	// ==================================================================================================================
	// PROPERTIES

	/** Id */
	public CalendarTextField withId(String value) { setId(value); return this; }

	/** Calendar: */
	public ObjectProperty<Calendar> calendarProperty() { return calendarObjectProperty; }
	final private ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar", null);
	public Calendar getCalendar() { return calendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { calendarObjectProperty.setValue(value); }
	public CalendarTextField withCalendar(Calendar value) { setCalendar(value); return this; }

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault())
	{
		public void set(Locale value)
		{
			super.set(value);
			if (dateFormatManual == false)
			{
				setDateFormat( SimpleDateFormat.getDateInstance(DateFormat.LONG, value) );
			}
		}
	};
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public CalendarTextField withLocale(Locale value) { setLocale(value); return this; } 
	
	/** 
	 * The DateFormat used to render/parse the date in the textfield.
	 * It is allow to show time as well for example by SimpleDateFormat.getDateTimeInstance().
	 */
	public ObjectProperty<DateFormat> dateFormatProperty() { return dateFormatObjectProperty; }
	final private ObjectProperty<DateFormat> dateFormatObjectProperty = new SimpleObjectProperty<DateFormat>(this, "dateFormat", SimpleDateFormat.getDateInstance(DateFormat.LONG, getLocale()))
	{
		public void set(DateFormat value)
		{
			super.set( value != null ? value : SimpleDateFormat.getDateInstance(DateFormat.LONG, getLocale()));
			dateFormatManual = (value != null);
		}
	};
	public DateFormat getDateFormat() { return dateFormatObjectProperty.getValue(); }
	public void setDateFormat(DateFormat value) { dateFormatObjectProperty.setValue(value); }
	public CalendarTextField withDateFormat(DateFormat value) { setDateFormat(value); return this; }
	private boolean dateFormatManual = false;
	
	/** PromptText: */
	public ObjectProperty<String> promptTextProperty() { return promptTextObjectProperty; }
	final private ObjectProperty<String> promptTextObjectProperty = new SimpleObjectProperty<String>(this, "promptText", null);
	public String getPromptText() { return promptTextObjectProperty.get(); }
	public void setPromptText(String value) { promptTextObjectProperty.set(value); }
	public CalendarTextField withPromptText(String value) { setPromptText(value); return this; }

	/** DateFormats: a list of alternate dateFormats used for parsing only */
	public ListProperty<DateFormat> dateFormatsProperty() { return dateFormatsProperty; }
	ListProperty<DateFormat> dateFormatsProperty = new SimpleListProperty<DateFormat>(javafx.collections.FXCollections.observableList(new ArrayList<DateFormat>()));
	public ObservableList<DateFormat> getDateFormats() { return dateFormatsProperty.getValue(); }
	public void setDateFormats(ObservableList<DateFormat> value) { dateFormatsProperty.setValue(value); }
	public CalendarTextField withDateFormat(ObservableList<DateFormat> value) { setDateFormats(value); return this; }

	/** parse error callback:
	 * If something did not parse correctly, you may handle it. 
	 * Otherwise the exception will be logged on the console.
	 */
	public ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackProperty() { return parseErrorCallbackObjectProperty; }
	final private ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackObjectProperty = new SimpleObjectProperty<Callback<Throwable, Void>>(this, "parseErrorCallback", null);
	public Callback<Throwable, Void> getParseErrorCallback() { return this.parseErrorCallbackObjectProperty.getValue(); }
	public void setParseErrorCallback(Callback<Throwable, Void> value) { this.parseErrorCallbackObjectProperty.setValue(value); }
	public CalendarTextField withParseErrorCallback(Callback<Throwable, Void> value) { setParseErrorCallback(value); return this; }

    /** disabledCalendars: */
	public ObservableList<Calendar> disabledCalendars() { return disabledCalendars; }
	final private ObservableList<Calendar> disabledCalendars =  javafx.collections.FXCollections.observableArrayList();

	/** highlightedCalendars: */
	public ObservableList<Calendar> highlightedCalendars() { return highlightedCalendars; }
	final private ObservableList<Calendar> highlightedCalendars =  javafx.collections.FXCollections.observableArrayList();

	/** calendarRangeCallback: 
	 * This callback allows a developer to limit the amount of calendars put in any of the collections.
	 * It is called just before a new range is being displayed, so the developer can change the values in the collections like highlighted or disabled. 
	 */
	public ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackProperty() { return calendarRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<CalendarRange, Void>>(this, "calendarRangeCallback", null);
	public Callback<CalendarRange, Void> getCalendarRangeCallback() { return this.calendarRangeCallbackObjectProperty.getValue(); }
	public void setCalendarRangeCallback(Callback<CalendarRange, Void> value) { this.calendarRangeCallbackObjectProperty.setValue(value); }
	public CalendarTextField withCalendarRangeCallback(Callback<CalendarRange, Void> value) { setCalendarRangeCallback(value); return this; }

	/** is null allowed */
    volatile private BooleanProperty allowNullProperty = new SimpleBooleanProperty(this, "allowNull", true)
    {
		public void set(boolean value)
		{
			super.set(value);
			if (value == false && getCalendar() == null)
			{
				setCalendar(Calendar.getInstance(getLocale()));
			}
		}
	};
    public BooleanProperty allowNullProperty() { return allowNullProperty; }
    public boolean getAllowNull() { return allowNullProperty.get(); }
    public void setAllowNull(boolean allowNull) { allowNullProperty.set(allowNull); }
    public CalendarTextField withAllowNull(boolean value) { setAllowNull(value); return this; }
	
	/** Text: */
	public ObjectProperty<String> textProperty() { return textObjectProperty; }
	final private ObjectProperty<String> textObjectProperty = new SimpleObjectProperty<String>(this, "text", null);
	public String getText() { return textObjectProperty.get(); }
	public void setText(String value) { textObjectProperty.set(value); }
	public CalendarTextField withText(String value) { setText(value); return this; }

    
	// ==================================================================================================================
	// EVENTS
	
	// ==================================================================================================================
	// BEHAVIOR
	
}
