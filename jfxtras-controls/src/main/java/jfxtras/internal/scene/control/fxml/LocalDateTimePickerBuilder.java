/**
 * LocalDateTimePickerBuilder.java
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import jfxtras.fxml.BuilderService;
import jfxtras.scene.control.LocalDateTimePicker;

/**
 * @author Tom Eugelink
 *
 */
public class LocalDateTimePickerBuilder extends AbstractBuilder implements BuilderService<LocalDateTimePicker>
{
	static final private DateTimeFormatter YMDHMSDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { 
		this.locale = Locale.forLanguageTag(value); 
	}
	private Locale locale = null;

	/** displayedLocalDateTime */
	public String getDisplayedLocalDateTime() { return null; } // dummy, just to make it Java Bean compatible
	public void setDisplayedLocalDateTime(String value) { 
		this.displayedLocalDateTime = LocalDateTime.parse(value, YMDHMSDateTimeFormatter);
	}
	private LocalDateTime displayedLocalDateTime = null;

	/** localDateTime */
	public String getLocalDateTime() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocalDateTime(String value) {
		this.localDateTime = LocalDateTime.parse(value, YMDHMSDateTimeFormatter);
	}
	private LocalDateTime localDateTime = null;

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
	public LocalDateTimePicker build()
	{
		LocalDateTimePicker lLocalDateTimePicker = new LocalDateTimePicker();
		if (locale != null) {
			lLocalDateTimePicker.setLocale(locale);
		}
		if (localDateTime != null) {
			lLocalDateTimePicker.setLocalDateTime(localDateTime);
		}
		if (displayedLocalDateTime != null) {
			lLocalDateTimePicker.setDisplayedLocalDateTime(displayedLocalDateTime);
		}
		if (allowNull != null) {
			lLocalDateTimePicker.setAllowNull(allowNull);
		}
		applyCommonProperties(lLocalDateTimePicker);
		return lLocalDateTimePicker;
	}
	
	/**
	 * Implementation of BuilderService interface
	 */
	@Override
	public boolean isBuilderFor(Class<?> clazz)
	{
		return LocalDateTimePicker.class.isAssignableFrom(clazz);
	}
}
