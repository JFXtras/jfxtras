/**
 * LocalTimePicker.java
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
import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;

/**
 * LocalTime (JSR-310) picker component.
 * This is an extension of the CalendarPicker adding the new date API JSR-310.
 * Since Calendar will not be removed from the JDK, too many applications use it, this approach of extending CalendarPicker is the most flexible one. 
 * 
 * @author Tom Eugelink
 */
public class LocalTimePicker extends CalendarTimePicker
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
		// construct properties
		constructLocalTime();
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalTime: */
	public ObjectProperty<LocalTime> localDateProperty() { return localDateObjectProperty; }
	private final ObjectProperty<LocalTime> localDateObjectProperty = new SimpleObjectProperty<LocalTime>(this, "localDate");
	public LocalTime getLocalTime() { return localDateObjectProperty.getValue(); }
	public void setLocalTime(LocalTime value) { localDateObjectProperty.setValue(value); }
	public LocalTimePicker withLocalTime(LocalTime value) { setLocalTime(value); return this; } 
	private void constructLocalTime()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				localDateProperty().set(DateTimeToCalendarHelper.createLocalTimeFromCalendar(newValue));
			} 
		});
		
		// if the inherited value is changed, make sure calendar is updated
		localDateProperty().addListener(new ChangeListener<LocalTime>()
		{
			@Override
			public void changed(ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue)
			{
				calendarProperty().set( newValue == null ? null : DateTimeToCalendarHelper.createCalendarFromLocalTime(newValue));
			} 
		});
	}

	// ==================================================================================================================
	// SUPPORT

}
