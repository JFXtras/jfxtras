/**
 * LocalTimeTextField.java
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.LocalTimeTextFieldSkin;

/**
 * LocalTime (JSR-310) text field component.
 * 
 * @author Tom Eugelink
 */
public class LocalTimeTextField extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalTimeTextField()
	{
		construct();
	}

	/**
	 * 
	 * @param localTime
	 */
	public LocalTimeTextField(LocalTime localTime)
	{
		construct();
		setLocalTime(localTime);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
	}

	@Override public Skin createDefaultSkin() {
		return new LocalTimeTextFieldSkin(this); 
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalTime: */
	public ObjectProperty<LocalTime> localTimeProperty() { return localTimeObjectProperty; }
	private final ObjectProperty<LocalTime> localTimeObjectProperty = new SimpleObjectProperty<LocalTime>(this, "localTime");
	public LocalTime getLocalTime() { return localTimeObjectProperty.getValue(); }
	public void setLocalTime(LocalTime value) { localTimeObjectProperty.setValue(value); }
	public LocalTimeTextField withLocalTime(LocalTime value) { setLocalTime(value); return this; } 

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault(), "locale", Locale.getDefault()) 
	{
		public void set(Locale value) {
			super.set(value);
			if (dateFormatManual == false) {
				setDateTimeFormatter( null );
			}
		}
	};
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public LocalTimeTextField withLocale(Locale value) { setLocale(value); return this; } 
	
	/** 
	 * The DateTimeFormatter used to render/parse the date in the textfield.
	 */
	public ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty() { return dateTimeFormatterObjectProperty; }
	final private ObjectProperty<DateTimeFormatter> dateTimeFormatterObjectProperty = new SimpleObjectProperty<DateTimeFormatter>(this, "dateTimeFormatter", DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(getLocale()) )
	{
		public void set(DateTimeFormatter value)
		{
			super.set( value != null ? value : DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(getLocale()));
			dateFormatManual = (value != null);
		}
	};
	private boolean dateFormatManual = false;
	public DateTimeFormatter getDateTimeFormatter() { return dateTimeFormatterObjectProperty.getValue(); }
	public void setDateTimeFormatter(DateTimeFormatter value) { dateTimeFormatterObjectProperty.setValue(value); }
	public LocalTimeTextField withDateTimeFormatter(DateTimeFormatter value) { setDateTimeFormatter(value); return this; }
	
	/** DateTimeFormatters: a list of alternate dateFormats used for parsing only */
	public ListProperty<DateTimeFormatter> dateTimeFormattersProperty() { return dateTimeFormattersProperty; }
	ListProperty<DateTimeFormatter> dateTimeFormattersProperty = new SimpleListProperty<DateTimeFormatter>(javafx.collections.FXCollections.observableList(new ArrayList<DateTimeFormatter>()));
	public ObservableList<DateTimeFormatter> getDateTimeFormatters() { return dateTimeFormattersProperty.getValue(); }
	public void setDateTimeFormatters(ObservableList<DateTimeFormatter> value) { dateTimeFormattersProperty.setValue(value); }
	public LocalTimeTextField withDateTimeFormatter(ObservableList<DateTimeFormatter> value) { setDateTimeFormatters(value); return this; }

	/** PromptText: */
	public ObjectProperty<String> promptTextProperty() { return promptTextObjectProperty; }
	final private ObjectProperty<String> promptTextObjectProperty = new SimpleObjectProperty<String>(this, "promptText", null);
	public String getPromptText() { return promptTextObjectProperty.get(); }
	public void setPromptText(String value) { promptTextObjectProperty.set(value); }
	public LocalTimeTextField withPromptText(String value) { setPromptText(value); return this; }

	/** parse error callback:
	 * If something did not parse correctly, you may handle it. 
	 * Otherwise the exception will be logged on the console.
	 */
	public ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackProperty() { return parseErrorCallbackObjectProperty; }
	final private ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackObjectProperty = new SimpleObjectProperty<Callback<Throwable, Void>>(this, "parseErrorCallback", null);
	public Callback<Throwable, Void> getParseErrorCallback() { return this.parseErrorCallbackObjectProperty.getValue(); }
	public void setParseErrorCallback(Callback<Throwable, Void> value) { this.parseErrorCallbackObjectProperty.setValue(value); }
	public LocalTimeTextField withParseErrorCallback(Callback<Throwable, Void> value) { setParseErrorCallback(value); return this; }

	
	// ==================================================================================================================
	// SUPPORT
}
