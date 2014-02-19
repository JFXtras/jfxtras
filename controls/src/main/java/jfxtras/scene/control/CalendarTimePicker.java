/**
 * CalendarTimePicker.java
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import jfxtras.internal.scene.control.skin.CalendarTimePickerSkin;

/**
 * TimePicker control
 * The calendar is (and should) be treated as immutable. That means the setter is not used, but when a value is changed a new instance (clone) is put in the calendar property.
 *  
 * @author Tom Eugelink
 */
public class CalendarTimePicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTimePicker()
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
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/internal/scene/control/" + CalendarTimePicker.class.getSimpleName() + ".css").toExternalForm();
	}
	
	// ==================================================================================================================
	// PROPERTIES

	/** Id */
	public CalendarTimePicker withId(String value) { setId(value); return this; }

	/** calendar: */
	public ObjectProperty<Calendar> calendarProperty() { return calendarObjectProperty; }
	final private ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar", Calendar.getInstance())
	{
		public void set(Calendar value)
		{
			value = CalendarTimePickerSkin.blockMinutesToStep(value, getMinuteStep());
			super.set(value);
		}		
	};
	public Calendar getCalendar() { return calendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { calendarObjectProperty.setValue(value); }
	public CalendarTimePicker withCalendar(Calendar value) { setCalendar(value); return this; } 

	/** MinuteStep */
	public ObjectProperty<Integer> minuteStepProperty() { return minuteStepProperty; }
	final private SimpleObjectProperty<Integer> minuteStepProperty = new SimpleObjectProperty<Integer>(this, "minuteStep", 1)
	{
		public void set(Integer value)
		{
			super.set(value);
			setCalendar( CalendarTimePickerSkin.blockMinutesToStep(getCalendar(), getMinuteStep()) );
		}		
	};
	public Integer getMinuteStep() { return minuteStepProperty.getValue(); }
	public void setMinuteStep(Integer value) { minuteStepProperty.setValue(value); }
	public CalendarTimePicker withMinuteStep(Integer value) { setMinuteStep(value); return this; } 

	/** ShowLabels */
	public ObjectProperty<Boolean> showLabelsProperty() { return showLabelsProperty; }
	final private SimpleObjectProperty<Boolean> showLabelsProperty = new SimpleObjectProperty<Boolean>(this, "showLabels", false)
	{
		public void set(Boolean value)
		{
			if (value == null) throw new NullPointerException("showLabels cannot be null");
			super.set(value);
		}		
	};
	public Boolean getShowLabels() { return showLabelsProperty.getValue(); }
	public void setShowLabels(Boolean value) { showLabelsProperty.setValue(value); }
	public CalendarTimePicker withShowLabels(Boolean value) { setShowLabels(value); return this; } 

	// TODO: add showTimeLabels, but the labels make the layout jump
	
	// ==================================================================================================================
	// SUPPORT
	

	/*
	 * 
	 */
	static public String quickFormatCalendar(Calendar value)
	{
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
}
