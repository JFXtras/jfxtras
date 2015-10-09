/**
 * LocalTimePicker.java
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

import java.time.LocalTime;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.LocalTimePickerSkin;

/**
 * LocalTime (JSR-310) picker component.
 * 
 * @author Tom Eugelink
 */
public class LocalTimePicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalTimePicker()
	{
		construct();
	}

	/**
	 * 
	 * @param localTime
	 */
	public LocalTimePicker(LocalTime localTime)
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

	@Override public Skin<?> createDefaultSkin() {
		return new LocalTimePickerSkin(this); 
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalTime: */
	public ObjectProperty<LocalTime> localTimeProperty() { return localTimeObjectProperty; }
	private final ObjectProperty<LocalTime> localTimeObjectProperty = new SimpleObjectProperty<LocalTime>(this, "localTime", LocalTime.now());
	public LocalTime getLocalTime() { return localTimeObjectProperty.getValue(); }
	public void setLocalTime(LocalTime value) { localTimeObjectProperty.setValue(value); }
	public LocalTimePicker withLocalTime(LocalTime value) { setLocalTime(value); return this; } 

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	volatile private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public LocalTimePicker withLocale(Locale value) { setLocale(value); return this; } 

	/** MinuteStep */
	public ObjectProperty<Integer> minuteStepProperty() { return minuteStepProperty; }
	final private SimpleObjectProperty<Integer> minuteStepProperty = new SimpleObjectProperty<Integer>(this, "minuteStep", 1);
	public Integer getMinuteStep() { return minuteStepProperty.getValue(); }
	public void setMinuteStep(Integer value) { minuteStepProperty.setValue(value); }
	public LocalTimePicker withMinuteStep(Integer value) { setMinuteStep(value); return this; } 


	/** SecondStep */
	public ObjectProperty<Integer> secondStepProperty() { return secondStepProperty; }
	final private SimpleObjectProperty<Integer> secondStepProperty = new SimpleObjectProperty<Integer>(this, "secondStep", 1);
	public Integer getSecondStep() { return secondStepProperty.getValue(); }
	public void setSecondStep(Integer value) { secondStepProperty.setValue(value); }
	public LocalTimePicker withSecondStep(Integer value) { setSecondStep(value); return this; } 
	
	/** valueValidationCallback: 
	 * This callback allows a developer deny or accept a value just prior before it gets added.
	 * Returning true will allow the value.
	 */
	public ObjectProperty<Callback<LocalTime, Boolean>> valueValidationCallbackProperty() { return valueValidationCallbackObjectProperty; }
	final private ObjectProperty<Callback<LocalTime, Boolean>> valueValidationCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalTime, Boolean>>(this, "valueValidationCallback", null);
	public Callback<LocalTime, Boolean> getValueValidationCallback() { return this.valueValidationCallbackObjectProperty.getValue(); }
	public void setValueValidationCallback(Callback<LocalTime, Boolean> value) { this.valueValidationCallbackObjectProperty.setValue(value); }
	public LocalTimePicker withValueValidationCallback(Callback<LocalTime, Boolean> value) { setValueValidationCallback(value); return this; }

	// ==================================================================================================================
	// SUPPORT

}
