/**
 * LocalDateTimeTextField.java
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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
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
import jfxtras.internal.scene.control.skin.LocalDateTimeTextFieldSkin;
import jfxtras.scene.control.LocalDateTimePicker.LocalDateTimeRange;


/**
 * // These are used for the includes (shared with LocalDatePicker and LocalDateTimePicker) 
 * :control: LocalDateTimeTextField 
 * :control_instance: localDateTimeTextField 
 * :calendar: localDateTime
 * :calendars: localDateTimes
 * :calendar_class: LocalDateTime
 * :calendars_class: LocalDateTimes
 * :dateFormat: dateTimeFormatter
 * :dateFormats: dateTimeFormaters
 * 
 * = LocalDateTimeTextField
 * include::jfxtras-controls/src/main/asciidoc/scene/control/CalendarTextField_properties.adoc[]
 * 
 * == Callback
 * include::jfxtras-controls/src/main/asciidoc/scene/control/CalendarTextField_callbacks.adoc[]
 * 
 * == Icon
 * include::jfxtras-controls/src/main/asciidoc/scene/control/CalendarTextField_icon.adoc[]
 */
public class LocalDateTimeTextField extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDateTimeTextField()
	{
		construct();
	}

	/**
	 * 
	 * @param localDateTime
	 */
	public LocalDateTimeTextField(LocalDateTime localDateTime)
	{
		construct();
		setLocalDateTime(localDateTime);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
            constructDisplayedLocalDateTime();
	}

	@Override public Skin createDefaultSkin() {
		return new LocalDateTimeTextFieldSkin(this);
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDateTime: the selected date. */
	public ObjectProperty<LocalDateTime> localDateTimeProperty() { return localDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> localDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "localDateTime");
	public LocalDateTime getLocalDateTime() { return localDateTimeObjectProperty.getValue(); }
	public void setLocalDateTime(LocalDateTime value) { localDateTimeObjectProperty.setValue(value); }
	public LocalDateTimeTextField withLocalDateTime(LocalDateTime value) { setLocalDateTime(value); return this; } 

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault())
	{
		public void set(Locale value) {
			super.set(value);
			if (dateFormatManual == false) {
				setDateTimeFormatter( DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale()) );
			}
		}
	};
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public LocalDateTimeTextField withLocale(Locale value) { setLocale(value); return this; } 

	/** 
	 * The DateTimeFormatter used to render/parse the date in the textfield.
	 */
	public ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty() { return dateTimeFormatterObjectProperty; }
	final private ObjectProperty<DateTimeFormatter> dateTimeFormatterObjectProperty = new SimpleObjectProperty<DateTimeFormatter>(this, "dateTimeFormatter", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale()) )
	{
		public void set(DateTimeFormatter value) {
			super.set( value != null ? value : DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale()));
			dateFormatManual = (value != null);
		}
	};
	private boolean dateFormatManual = false;
	public DateTimeFormatter getDateTimeFormatter() { return dateTimeFormatterObjectProperty.getValue(); }
	public void setDateTimeFormatter(DateTimeFormatter value) { dateTimeFormatterObjectProperty.setValue(value); }
	public LocalDateTimeTextField withDateTimeFormatter(DateTimeFormatter value) { setDateTimeFormatter(value); return this; }
	
	/** DateTimeFormatters: a list of alternate dateFormats used for parsing only */
	public ListProperty<DateTimeFormatter> dateTimeFormattersProperty() { return dateTimeFormattersProperty; }
	ListProperty<DateTimeFormatter> dateTimeFormattersProperty = new SimpleListProperty<DateTimeFormatter>(javafx.collections.FXCollections.observableList(new ArrayList<DateTimeFormatter>()));
	public ObservableList<DateTimeFormatter> getDateTimeFormatters() { return dateTimeFormattersProperty.getValue(); }
	public void setDateTimeFormatters(ObservableList<DateTimeFormatter> value) { dateTimeFormattersProperty.setValue(value); }
	public LocalDateTimeTextField withDateTimeFormatter(ObservableList<DateTimeFormatter> value) { setDateTimeFormatters(value); return this; }

	/** PromptText: */
	public ObjectProperty<String> promptTextProperty() { return promptTextObjectProperty; }
	final private ObjectProperty<String> promptTextObjectProperty = new SimpleObjectProperty<String>(this, "promptText", null);
	public String getPromptText() { return promptTextObjectProperty.get(); }
	public void setPromptText(String value) { promptTextObjectProperty.set(value); }
	public LocalDateTimeTextField withPromptText(String value) { setPromptText(value); return this; }

	/** parse error callback:
	 * If something did not parse correctly, you may handle it. 
	 * Otherwise the exception will be logged on the console.
	 */
	public ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackProperty() { return parseErrorCallbackObjectProperty; }
	final private ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackObjectProperty = new SimpleObjectProperty<Callback<Throwable, Void>>(this, "parseErrorCallback", null);
	public Callback<Throwable, Void> getParseErrorCallback() { return this.parseErrorCallbackObjectProperty.getValue(); }
	public void setParseErrorCallback(Callback<Throwable, Void> value) { this.parseErrorCallbackObjectProperty.setValue(value); }
	public LocalDateTimeTextField withParseErrorCallback(Callback<Throwable, Void> value) { setParseErrorCallback(value); return this; }
	
	/** highlightedLocalDateTimes: a list of dates that are rendered with the highlight class added. This can then be styled using CSS. */
	public ObservableList<LocalDateTime> highlightedLocalDateTimes() { return highlightedLocalDateTimes; }
	private final ObservableList<LocalDateTime> highlightedLocalDateTimes =  javafx.collections.FXCollections.observableArrayList();

	/** disabledLocalDateTimes: a list of dates that cannot be selected. */
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
	public LocalDateTimeTextField withLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) { setLocalDateTimeRangeCallback(value); return this; }
	
	/** valueValidationCallback: 
	 * This callback allows a developer deny or accept a value just prior before it gets added.
	 * Returning true will allow the value.
	 */
	public ObjectProperty<Callback<LocalDateTime, Boolean>> valueValidationCallbackProperty() { return valueValidationCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalDateTime, Boolean>> valueValidationCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTime, Boolean>>(this, "valueValidationCallback", null);
	public Callback<LocalDateTime, Boolean> getValueValidationCallback() { return this.valueValidationCallbackObjectProperty.getValue(); }
	public void setValueValidationCallback(Callback<LocalDateTime, Boolean> value) { this.valueValidationCallbackObjectProperty.setValue(value); }
	public LocalDateTimeTextField withValueValidationCallback(Callback<LocalDateTime, Boolean> value) { setValueValidationCallback(value); return this; }

    /**
	 * DisplayedLocalDateTime:
	 * You may set this value, but it is also overwritten by other logic and the skin. Do not assume you have total control.
	 * The localDateTime should not be modified using any of its add or set methods (it should be considered immutable)
	 */
	public ObjectProperty<LocalDateTime> displayedLocalDateTime() { return displayedLocalDateTimeObjectProperty; }
	volatile private ObjectProperty<LocalDateTime> displayedLocalDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "displayedLocalDateTimeRange");
	public LocalDateTime getDisplayedLocalDateTime() { return displayedLocalDateTimeObjectProperty.getValue(); }
	public void setDisplayedLocalDateTime(LocalDateTime value) { displayedLocalDateTimeObjectProperty.setValue(value); }
	public LocalDateTimeTextField withDisplayedLocalDateTime(LocalDateTime value) { setDisplayedLocalDateTime(value); return this; }
	private void constructDisplayedLocalDateTime()
	{
		// init here, so deriveDisplayedLocalDateTimeRange in the skin will modify it accordingly
		setDisplayedLocalDateTime(LocalDateTime.now());
	}
        
	/** AllowNull: indicates if no selected date (resulting in null in the calendar property) is an allowed state. */
    public BooleanProperty allowNullProperty() { return allowNullProperty; }
    volatile private BooleanProperty allowNullProperty = new SimpleBooleanProperty(this, "allowNull", true);
    public boolean getAllowNull() { return allowNullProperty.get(); }
    public void setAllowNull(boolean allowNull) { allowNullProperty.set(allowNull); }
    public LocalDateTimeTextField withAllowNull(boolean value) { setAllowNull(value); return this; }
	
	/** Text: */
	public ObjectProperty<String> textProperty() { return textObjectProperty; }
	final private ObjectProperty<String> textObjectProperty = new SimpleObjectProperty<String>(this, "text", null);
	public String getText() { return textObjectProperty.get(); }
	public void setText(String value) { textObjectProperty.set(value); }
	public LocalDateTimeTextField withText(String value) { setText(value); return this; }
	
        
         /**
     * Represents the current state of the Picker popup, and whether it is
     * currently visible on screen.
     */
    public BooleanProperty pickerShowingProperty() { return pickerShowingProperty; }
    final private BooleanProperty pickerShowingProperty = new SimpleBooleanProperty();
    public boolean isPickerShowing() { return pickerShowingProperty.get(); }
    public void setPickerShowing(boolean value) { pickerShowingProperty.set(value); }
	public LocalDateTimeTextField withPickerShowing(boolean value) { setPickerShowing(value); return this; }
}
