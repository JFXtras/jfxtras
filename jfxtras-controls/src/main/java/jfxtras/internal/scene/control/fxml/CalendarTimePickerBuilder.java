/**
 * CalendarTimePickerBuilder.java
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
import jfxtras.scene.control.CalendarTimePicker;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarTimePickerBuilder extends AbstractBuilder implements BuilderService<CalendarTimePicker>
{
	static final private SimpleDateFormat HMSSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");

	/** calendar */
	public String getCalendar() { return null; } // dummy, just to make it Java Bean compatible
	public void setCalendar(String value) {
		try {
			Calendar lCalendar = Calendar.getInstance();
			lCalendar.setTime( HMSSimpleDateFormat.parse(value) );
			this.calendar = lCalendar;
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	private Calendar calendar = null;

	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { 
		this.locale = Locale.forLanguageTag(value); 
	}
	private Locale locale = null;

	/** MinuteStep */
	public Integer getMinuteStep() { return null; } // dummy, just to make it Java Bean compatible
	public void setMinuteStep(Integer value) { 
		this.minuteStep = Integer.valueOf(value); 
	}
	private Integer minuteStep = null;

	/** SecondStep */
	public Integer getSecondStep() { return null; } // dummy, just to make it Java Bean compatible
	public void setSecondStep(Integer value) { 
		this.secondStep = Integer.valueOf(value); 
	}
	private Integer secondStep = null;

	/**
	 * Implementation of Builder interface
	 */
	@Override
	public CalendarTimePicker build()
	{
		CalendarTimePicker lCalendarTimePicker = new CalendarTimePicker();
		if (locale != null) {
			lCalendarTimePicker.setLocale(locale);
		}
		if (calendar != null) {
			lCalendarTimePicker.setCalendar(calendar);
		}
		if (minuteStep != null) {
			lCalendarTimePicker.setMinuteStep(minuteStep);
		}
		if (secondStep != null) {
			lCalendarTimePicker.setSecondStep(secondStep);
		}
		applyCommonProperties(lCalendarTimePicker);
		return lCalendarTimePicker;
	}
	
	/**
	 * Implementation of BuilderService interface
	 */
	@Override
	public boolean isBuilderFor(Class<?> clazz)
	{
		return CalendarTimePicker.class.isAssignableFrom(clazz);
	}
}
