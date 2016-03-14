/**
 * LocalDatePicker.java
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

import java.time.LocalDate;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.LocalDatePickerSkin;

/**
 * // These are used for the includes
 * :control: LocalDatePicker 
 * :control_instance: localDatePicker 
 * :calendar: localDate
 * :calendars: localDates
 * :calendar_class: LocalDate
 * :calendars_class: LocalDates
 * 
 * = LocalDatePicker 
 * LocalDatePicker is a control for selecting one, multiple or a range of dates. 
 * The name LocalDatePicker is because it uses Java's LocalDate (JSR-310) (as opposed to Date) in its API to do so.
 * 
 *include::src/main/asciidoc/scene/control/CalendarPicker_properties.adoc[]
 *include::src/main/asciidoc/scene/control/CalendarPicker_modeProperty.adoc[]
 * 
 * == Callback
 *include::src/main/asciidoc/scene/control/CalendarPicker_callbacks.adoc[]
 */
public class LocalDatePicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDatePicker()
	{
		construct();
	}

	/**
	 * 
	 * @param localDateTime
	 */
	public LocalDatePicker(LocalDate localDateTime)
	{
		construct();
		setLocalDate(localDateTime);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
	}

	@Override public Skin<LocalDatePicker> createDefaultSkin() {
		return new LocalDatePickerSkin(this);
	}

	
	// ==================================================================================================================
	// PROPERTIES
	
	
	/** Mode: single, range or multiple */
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
	public LocalDatePicker withMode(Mode value) { setMode(value); return this; } 

	/** LocalDate: the selected date, or when in RANGE or MULTIPLE mode, the last selected date.*/
	public ObjectProperty<LocalDate> localDateProperty() { return localDateObjectProperty; }
	private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	public LocalDate getLocalDate() { return localDateObjectProperty.getValue(); }
	public void setLocalDate(LocalDate value) { localDateObjectProperty.setValue(value); }
	public LocalDatePicker withLocalDate(LocalDate value) { setLocalDate(value); return this; }

	/** LocalDates: a list of all selected dates. */
	public ObservableList<LocalDate> localDates() { return localDates; }
	private final ObservableList<LocalDate> localDates =  javafx.collections.FXCollections.observableArrayList();

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	volatile private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public LocalDatePicker withLocale(Locale value) { setLocale(value); return this; } 

	/** AllowNull: indicates if no selected date (resulting in null in the localDate property) is an allowed state.*/
    public BooleanProperty allowNullProperty() { return allowNullProperty; }
    volatile private BooleanProperty allowNullProperty = new SimpleBooleanProperty(this, "allowNull", true);
    public boolean getAllowNull() { return allowNullProperty.get(); }
    public void setAllowNull(boolean allowNull) { allowNullProperty.set(allowNull); }
    public LocalDatePicker withAllowNull(boolean value) { setAllowNull(value); return this; }

	/** highlightedLocalDates: a list of dates that are rendered with the highlight class added. This can then be styled using CSS. */
	public ObservableList<LocalDate> highlightedLocalDates() { return highlightedLocalDates; }
	private final ObservableList<LocalDate> highlightedLocalDates =  javafx.collections.FXCollections.observableArrayList();

	/** disabledLocalDates: a list of dates that cannot be selected. */
	public ObservableList<LocalDate> disabledLocalDates() { return disabledLocalDates; }
	private final ObservableList<LocalDate> disabledLocalDates =  javafx.collections.FXCollections.observableArrayList();

	/** DisplayedLocalDate: */
	public ObjectProperty<LocalDate> displayedLocalDateProperty() { return displayedLocalDateObjectProperty; }
	private final ObjectProperty<LocalDate> displayedLocalDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "displayedLocalDate", LocalDate.now());
	public LocalDate getDisplayedLocalDate() { return displayedLocalDateObjectProperty.getValue(); }
	public void setDisplayedLocalDate(LocalDate value) { displayedLocalDateObjectProperty.setValue(value); }
	public LocalDatePicker withDisplayedLocalDate(LocalDate value) { setDisplayedLocalDate(value); return this; }

	/** localDateRangeCallback: 
	 * This callback allows a developer to limit the amount of calendars put in any of the collections.
	 * It is called just before a new range is being displayed, so the developer can change the values in the collections like highlighted or disabled. 
	 */
	public ObjectProperty<Callback<LocalDateRange, Void>> LocalDateRangeCallbackProperty() { return localDateRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDateRange, Void>> localDateRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateRange, Void>>(this, "localDateRangeCallback", null);
	public Callback<LocalDateRange, Void> getLocalDateRangeCallback() { return this.localDateRangeCallbackObjectProperty.getValue(); }
	public void setLocalDateRangeCallback(Callback<LocalDateRange, Void> value) { this.localDateRangeCallbackObjectProperty.setValue(value); }
	public LocalDatePicker withLocalDateRangeCallback(Callback<LocalDateRange, Void> value) { setLocalDateRangeCallback(value); return this; }
	
	/**
	 * A Calendar range
	 */
	static public class LocalDateRange
	{
		public LocalDateRange(LocalDate start, LocalDate end)
		{
			this.start = start;
			this.end = end;
		}
		
		public LocalDate getStartLocalDate() { return start; }
		final LocalDate start;
		
		public LocalDate getEndLocalDate() { return end; }
		final LocalDate end; 
	}
	
	/** valueValidationCallback: 
	 * This callback allows a developer deny or accept a value just prior before it gets added.
	 * Returning true will allow the value.
	 */
	public ObjectProperty<Callback<LocalDate, Boolean>> valueValidationCallbackProperty() { return valueValidationCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDate, Boolean>> valueValidationCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDate, Boolean>>(this, "valueValidationCallback", null);
	public Callback<LocalDate, Boolean> getValueValidationCallback() { return this.valueValidationCallbackObjectProperty.getValue(); }
	public void setValueValidationCallback(Callback<LocalDate, Boolean> value) { this.valueValidationCallbackObjectProperty.setValue(value); }
	public LocalDatePicker withValueValidationCallback(Callback<LocalDate, Boolean> value) { setValueValidationCallback(value); return this; }

	
	// ==================================================================================================================
	// SUPPORT

}
