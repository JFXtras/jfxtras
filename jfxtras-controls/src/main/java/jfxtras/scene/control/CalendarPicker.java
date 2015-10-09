/**
 * CalendarPicker.java
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

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * // These are used for the includes
 * :control: CalendarPicker 
 * :control_instance: calendarPicker 
 * :calendar: calendar
 * :calendars: calendars
 * :calendar_class: Calendar
 * :calendars_class: Calendars
 * 
 * = CalendarPicker 
 * CalendarPicker is a control for selecting one, multiple or a range of dates, possibly including time. 
 * The name CalendarPicker is because it uses Java's Calendar (as opposed to Date) in its API to do so, mainly because Calendar holds Locale information and thus the days of the week can be rendered correctly.
 * 
 * include::jfxtras-controls/src/main/asciidoc/scene/control/CalendarPicker_properties.adoc[]
 * include::jfxtras-controls/src/main/asciidoc/scene/control/CalendarPicker_modeProperty.adoc[]
 * - The showTime property enables the embedded time picker, so the time part of a Calendar can be set as well. This is only possible in SINGLE mode.
 * 
 * == Callback
 * include::jfxtras-controls/src/main/asciidoc/scene/control/CalendarPicker_callbacks.adoc[]
 * 
 * == Immutability
 * include::jfxtras-controls/src/main/asciidoc/scene/control/Calendar_immutability.adoc[]
 * 
 * @author Tom Eugelink
 */
public class CalendarPicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPicker()
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
		this.getStyleClass().add(CalendarPicker.class.getSimpleName());
		
		// construct properties
		constructCalendar();
		constructCalendars();
		constructDisplayedCalendar();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return  CalendarPicker.class.getResource("/jfxtras/internal/scene/control/" + CalendarPicker.class.getSimpleName() + ".css").toExternalForm();
	}
	
	@Override public Skin<?> createDefaultSkin() {
		return new jfxtras.internal.scene.control.skin.CalendarPickerControlSkin(this); 
	}

	// ==================================================================================================================
	// PROPERTIES

	/** Id: for a fluent API */
	public CalendarPicker withId(String value) { setId(value); return this; }

	/** Calendar: the selected date, or when in RANGE or MULTIPLE mode, the last selected date. */
	public ObjectProperty<Calendar> calendarProperty() { return calendarObjectProperty; }
	final private ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar")
	{
		public void set(Calendar value)
		{
			if (value == null && getAllowNull() == false) {
				throw new NullPointerException("Null not allowed");
			}
			super.set(value);
		}
	};
	public Calendar getCalendar() { return calendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { calendarObjectProperty.setValue(value); }
	public CalendarPicker withCalendar(Calendar value) { setCalendar(value); return this; } 
	// construct property
	private void constructCalendar()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				if (modifyingCalendersAtomicInteger.get() == 0) {
					// if the new value is set to null, remove the old value
					if (newValue != null && calendars().contains(newValue) == false) {
						calendars().add(newValue);
					}
					if (oldValue != null) {
						calendars().remove(oldValue);
					}
				}
			} 
		});
	}

	/** Calendars: a list of all selected calendars. */
	public ObservableList<Calendar> calendars() { return calendars; }
	final private ObservableList<Calendar> calendars =  javafx.collections.FXCollections.observableArrayList();
	// construct property
	private void constructCalendars()
	{
		// make sure the singled out calendar is 
		calendars.addListener(new ListChangeListener<Calendar>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> change)
			{
				modifyingCalendersAtomicInteger.addAndGet(1);
				try {
					// if this is an add
					while (change.next()) {
						for (Calendar lCalendar : change.getAddedSubList()) {
							setCalendar( lCalendar );
						}
						for (Calendar lCalendar : change.getRemoved()) {
							// if the calendar to be removed is the active one
							if (lCalendar.equals(getCalendar())) {
								// if there are other left
								if (calendars().size() > 0) {
									// select the first
									setCalendar( calendars().get(0) );
								}
								else  {
									// clear it
									setCalendar(null);
								}
							}
						}
					}
				}
				finally {
					modifyingCalendersAtomicInteger.addAndGet(-1);
				}
			}
		});
	}
	final private AtomicInteger modifyingCalendersAtomicInteger = new AtomicInteger(0);

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	volatile private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public CalendarPicker withLocale(Locale value) { setLocale(value); return this; } 
	
	/** Mode: single, range or multiple. */
	public ObjectProperty<Mode> modeProperty() { return modeObjectProperty; }
	final private SimpleObjectProperty<Mode> modeObjectProperty = new SimpleObjectProperty<Mode>(this, "mode", Mode.SINGLE)
	{
		public void set(Mode value)
		{
			if (value == null) throw new NullPointerException("Null not allowed");
			super.set(value);
		}
	};
	public enum Mode { SINGLE, MULTIPLE, RANGE };
	public Mode getMode() { return modeObjectProperty.getValue(); }
	public void setMode(Mode value) { modeObjectProperty.setValue(value); }
	public CalendarPicker withMode(Mode value) { setMode(value); return this; } 

	/** ShowTime: enable the specifying of the time part in a Calendar. Only applicable in SINGLE mode. */
	public ObjectProperty<Boolean> showTimeProperty() { return showTimeObjectProperty; }
	volatile private ObjectProperty<Boolean> showTimeObjectProperty = new SimpleObjectProperty<Boolean>(this, "showTime", false);
	public Boolean getShowTime() { return showTimeObjectProperty.getValue(); }
	public void setShowTime(Boolean value) { showTimeObjectProperty.setValue(value); }
	public CalendarPicker withShowTime(Boolean value) { setShowTime(value); return this; }

	/** AllowNull: indicates if no selected date (resulting in null in the calendar property) is an allowed state. */
    public BooleanProperty allowNullProperty() { return allowNullProperty; }
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
    public boolean getAllowNull() { return allowNullProperty.get(); }
    public void setAllowNull(boolean allowNull) { allowNullProperty.set(allowNull); }
    public CalendarPicker withAllowNull(boolean value) { setAllowNull(value); return this; }

	/** disabledCalendars: a list of dates that cannot be selected. */
	public ObservableList<Calendar> disabledCalendars() { return disabledCalendars; }
	final private ObservableList<Calendar> disabledCalendars =  javafx.collections.FXCollections.observableArrayList();

	/** highlightedCalendars: a list of dates that are rendered with the highlight class added. This can then be styled using CSS. */
	public ObservableList<Calendar> highlightedCalendars() { return highlightedCalendars; }
	final private ObservableList<Calendar> highlightedCalendars =  javafx.collections.FXCollections.observableArrayList();

	/** calendarRangeCallback: 
	 * This callback allows a developer to limit the amount of calendars put in any of the collections like highlighted or disabled.
	 * It is called just before a new range is being displayed, so the developer can change the values in the collections. 
	 */
	public ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackProperty() { return calendarRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<CalendarRange, Void>> calendarRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<CalendarRange, Void>>(this, "calendarRangeCallback", null);
	public Callback<CalendarRange, Void> getCalendarRangeCallback() { return this.calendarRangeCallbackObjectProperty.getValue(); }
	public void setCalendarRangeCallback(Callback<CalendarRange, Void> value) { this.calendarRangeCallbackObjectProperty.setValue(value); }
	public CalendarPicker withCalendarRangeCallback(Callback<CalendarRange, Void> value) { setCalendarRangeCallback(value); return this; }
		
	/**
	 * A Calendar range
	 */
	static public class CalendarRange
	{
		public CalendarRange(Calendar start, Calendar end)
		{
			this.start = start;
			this.end = end;
		}
		
		public Calendar getStartCalendar() { return start; }
		final Calendar start;
		
		public Calendar getEndCalendar() { return end; }
		final Calendar end; 
	}

	/**
	 * DisplayedCalendar:
	 * You may set this value, but it is also overwritten by other logic and the skin. Do not assume you have total control.
	 * The calendar should not be modified using any of its add or set methods (it should be considered immutable)
	 */
	public ObjectProperty<Calendar> displayedCalendar() { return displayedCalendarObjectProperty; }
	volatile private ObjectProperty<Calendar> displayedCalendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "displayedCalendar");
	public Calendar getDisplayedCalendar() { return displayedCalendarObjectProperty.getValue(); }
	public void setDisplayedCalendar(Calendar value) { displayedCalendarObjectProperty.setValue(value); }
	public CalendarPicker withDisplayedCalendar(Calendar value) { setDisplayedCalendar(value); return this; }
	private void constructDisplayedCalendar()
	{
		// init here, so deriveDisplayedCalendar in the skin will modify it accordingly
		setDisplayedCalendar(Calendar.getInstance(getLocale()));
	}
	
	/** valueValidationCallback: 
	 * This callback allows a developer deny or accept a value just prior before it gets added.
	 * Returning true will allow the value.
	 */
	public ObjectProperty<Callback<Calendar, Boolean>> valueValidationCallbackProperty() { return valueValidationCallbackObjectProperty; }
	final private ObjectProperty<Callback<Calendar, Boolean>> valueValidationCallbackObjectProperty = new SimpleObjectProperty<Callback<Calendar, Boolean>>(this, "valueValidationCallback", null);
	public Callback<Calendar, Boolean> getValueValidationCallback() { return this.valueValidationCallbackObjectProperty.getValue(); }
	public void setValueValidationCallback(Callback<Calendar, Boolean> value) { this.valueValidationCallbackObjectProperty.setValue(value); }
	public CalendarPicker withValueValidationCallback(Callback<Calendar, Boolean> value) { setValueValidationCallback(value); return this; }
}
