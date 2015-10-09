/**
 * CalendarPickerBuilder.java
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

package jfxtras.internal.scene.control.fxml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jfxtras.fxml.BuilderService;
import jfxtras.scene.control.CalendarPicker;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarPickerBuilder extends AbstractBuilder implements BuilderService<CalendarPicker>
{
	static final private SimpleDateFormat YMDSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static final private SimpleDateFormat YMDHMSSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { 
		this.locale = Locale.forLanguageTag(value); 
	}
	private Locale locale = null;

	/** displayedCalendar */
	public String getDisplayedCalendar() { return null; } // dummy, just to make it Java Bean compatible
	public void setDisplayedCalendar(String value) { 
		try {
			this.displayedCalendar = Calendar.getInstance();
			this.displayedCalendar.setTime( YMDSimpleDateFormat.parse(value) );
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	private Calendar displayedCalendar = null;


	/** calendar */
	public String getCalendar() { return null; } // dummy, just to make it Java Bean compatible
	public void setCalendar(String value) {
		this.calendarText = value;
	}
	private String calendarText = null;


	/** Mode */
	public String getMode() { return null; } // dummy, just to make it Java Bean compatible
	public void setMode(String value) { 
		this.mode = CalendarPicker.Mode.valueOf(value); 
	}
	private CalendarPicker.Mode mode = null;

	/** ShowTime */
	public String getShowTime() { return null; } // dummy, just to make it Java Bean compatible
	public void setShowTime(String value) { 
		this.showTime = Boolean.valueOf(value); 
	}
	private Boolean showTime = null;

	/** AllowNull */
	public String getAllowNull() { return null; } // dummy, just to make it Java Bean compatible
	public void setAllowNull(String value) { 
		this.allowNull = Boolean.valueOf(value); 
	}
	private Boolean allowNull = null;


	/**
	 * Implementation of Builder interface
	 */
	@Override
	public CalendarPicker build()
	{
		CalendarPicker lCalendarPicker = new CalendarPicker();
		if (showTime != null) {
			lCalendarPicker.setShowTime(showTime);
		}
		if (locale != null) {
			lCalendarPicker.setLocale(locale);
		}
		if (calendarText != null) {
			// we parse here and not in setCalendar because we need the value of showTime, and that may be specified after the calendar attribute
			try {
				Calendar lCalendar = Calendar.getInstance();
				if (lCalendarPicker.showTimeProperty().get()) {
					lCalendar.setTime( YMDHMSSimpleDateFormat.parse(calendarText) );
				}
				else {
					lCalendar.setTime( YMDSimpleDateFormat.parse(calendarText) );
				}
				lCalendarPicker.setCalendar(lCalendar);
			}
			catch (ParseException e) {
				throw new RuntimeException(e);
			}

		}
		if (displayedCalendar != null) {
			lCalendarPicker.setDisplayedCalendar(displayedCalendar);
		}
		if (mode != null) {
			lCalendarPicker.setMode(mode);
		}
		if (allowNull != null) {
			lCalendarPicker.setAllowNull(allowNull);
		}
		applyCommonProperties(lCalendarPicker);
		return lCalendarPicker;
	}
	
	/**
	 * Implementation of BuilderService interface
	 */
	@Override
	public boolean isBuilderFor(Class<?> clazz)
	{
		return CalendarPicker.class.isAssignableFrom(clazz);
	}
}
