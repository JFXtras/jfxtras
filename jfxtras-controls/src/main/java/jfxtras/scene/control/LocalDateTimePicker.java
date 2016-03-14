/**
 * LocalDateTimePicker.java
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

import java.time.LocalDateTime;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.LocalDateTimePickerSkin;

/**
 * // These are used for the includes 
 * :control: LocalDateTimePicker 
 * :control_instance: LocalDateTimePicker 
 * :calendar: localDateTime
 * :calendars: localDateTimes
 * :calendar_class: LocalDateTime
 * :calendars_class: LocalDateTimes
 * 
 * = LocalDateTimePicker 
 * LocalDatePicker is a control for selecting one LocalDateTime (JSR-310). 
 * The name LocalDatePicker is because it uses Java's LocalDateTime (JSR-310) (as opposed to Date) in its API to do so.
 * 
 *include::src/main/asciidoc/scene/control/CalendarPicker_properties.adoc[]
 * 
 * == Callback
 *include::src/main/asciidoc/scene/control/CalendarPicker_callbacks.adoc[]
 */
public class LocalDateTimePicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDateTimePicker()
	{
		construct();
	}

	/**
	 * 
	 * @param localDateTime
	 */
	public LocalDateTimePicker(LocalDateTime localDateTime)
	{
		construct();
		setLocalDateTime(localDateTime);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
	}

	@Override public Skin createDefaultSkin() {
		return new LocalDateTimePickerSkin(this);
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDateTime: */
	public ObjectProperty<LocalDateTime> localDateTimeProperty() { return localDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> localDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "localDateTime");
	public LocalDateTime getLocalDateTime() { return localDateTimeObjectProperty.getValue(); }
	public void setLocalDateTime(LocalDateTime value) { localDateTimeObjectProperty.setValue(value); }
	public LocalDateTimePicker withLocalDateTime(LocalDateTime value) { setLocalDateTime(value); return this; }
	
	/** DisplayedLocalDateTime: */
	public ObjectProperty<LocalDateTime> displayedLocalDateTimeProperty() { return displayedLocalDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> displayedLocalDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "displayedLocalDateTime", LocalDateTime.now());
	public LocalDateTime getDisplayedLocalDateTime() { return displayedLocalDateTimeObjectProperty.getValue(); }
	public void setDisplayedLocalDateTime(LocalDateTime value) { displayedLocalDateTimeObjectProperty.setValue(value); }
	public LocalDateTimePicker withDisplayedLocalDateTime(LocalDateTime value) { setDisplayedLocalDateTime(value); return this; }

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	volatile private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public LocalDateTimePicker withLocale(Locale value) { setLocale(value); return this; } 

	/** AllowNull: indicates if no selected date (resulting in null in the localDateTime property) is an allowed state. */
    public BooleanProperty allowNullProperty() { return allowNullProperty; }
    volatile private BooleanProperty allowNullProperty = new SimpleBooleanProperty(this, "allowNull", true);
    public boolean getAllowNull() { return allowNullProperty.get(); }
    public void setAllowNull(boolean allowNull) { allowNullProperty.set(allowNull); }
    public LocalDateTimePicker withAllowNull(boolean value) { setAllowNull(value); return this; }

	/** HighlightedLocalDateTimes: a list of dates that are rendered with the highlight class added. This can then be styled using CSS. */
	public ObservableList<LocalDateTime> highlightedLocalDateTimes() { return highlightedLocalDateTimes; }
	private final ObservableList<LocalDateTime> highlightedLocalDateTimes =  javafx.collections.FXCollections.observableArrayList();

	/** DisabledLocalDateTimes: a list of dates that cannot be selected. */
	public ObservableList<LocalDateTime> disabledLocalDateTimes() { return disabledLocalDateTimes; }
	private final ObservableList<LocalDateTime> disabledLocalDateTimes =  javafx.collections.FXCollections.observableArrayList();

	/** localDateTimeRangeCallback: 
	 * This callback allows a developer to limit the amount of calendars put in any of the collections.
	 * It is called just before a new range is being displayed, so the developer can change the values in the collections like highlighted or disabled. 
	 */
	public ObjectProperty<Callback<LocalDateTimeRange, Void>> LocalDateTimeRangeCallbackProperty() { return localDateTimeRangeCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDateTimeRange, Void>> localDateTimeRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTimeRange, Void>>(this, "localDateTimeRangeCallback", null);
	public Callback<LocalDateTimeRange, Void> getLocalDateTimeRangeCallback() { return this.localDateTimeRangeCallbackObjectProperty.getValue(); }
	public void setLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) { this.localDateTimeRangeCallbackObjectProperty.setValue(value); }
	public LocalDateTimePicker withLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) { setLocalDateTimeRangeCallback(value); return this; }
	
	/**
	 * A Calendar range
	 */
	static public class LocalDateTimeRange
	{
		public LocalDateTimeRange(LocalDateTime start, LocalDateTime end)
		{
			this.start = start;
			this.end = end;
		}
		
		public LocalDateTime getStartLocalDateTime() { return start; }
		final LocalDateTime start;
		
		public LocalDateTime getEndLocalDateTime() { return end; }
		final LocalDateTime end; 
	}

	/** valueValidationCallback: 
	 * This callback allows a developer deny or accept a value just prior before it gets added.
	 * Returning true will allow the value.
	 */
	public ObjectProperty<Callback<LocalDateTime, Boolean>> valueValidationCallbackProperty() { return valueValidationCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDateTime, Boolean>> valueValidationCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTime, Boolean>>(this, "valueValidationCallback", null);
	public Callback<LocalDateTime, Boolean> getValueValidationCallback() { return this.valueValidationCallbackObjectProperty.getValue(); }
	public void setValueValidationCallback(Callback<LocalDateTime, Boolean> value) { this.valueValidationCallbackObjectProperty.setValue(value); }
	public LocalDateTimePicker withValueValidationCallback(Callback<LocalDateTime, Boolean> value) { setValueValidationCallback(value); return this; }

	
	// ==================================================================================================================
	// SUPPORT

}
