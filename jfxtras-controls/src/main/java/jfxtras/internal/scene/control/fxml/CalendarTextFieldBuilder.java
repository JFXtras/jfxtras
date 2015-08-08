/**
 * CalendarTextFieldBuilder.java
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.fxml.BuilderService;
import jfxtras.scene.control.CalendarTextField;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarTextFieldBuilder extends AbstractBuilder implements BuilderService<CalendarTextField>
{
	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { iLocale = Locale.forLanguageTag(value); }
	private Locale iLocale = null;

	/** PromptText */
	public String getPromptText() { return null; } // dummy, just to make it Java Bean compatible
	public void setPromptText(String value) { iPromptText = value; }
	private String iPromptText = null;

	/** DateFormat */
	public String getDateFormat() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateFormat(String value) { iDateFormat = value; }
	private String iDateFormat = null;

	/** DateFormats */
	public String getDateFormats() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateFormats(String value) 
	{  
		String[] lParts = value.split(",");
		iDateFormats = new ArrayList<>();
		for (String lPart : lParts) 
		{
			iDateFormats.add( lPart.trim() );
		}
	}
	private List<String> iDateFormats = null;

	/**
	 * Implementation of Builder interface
	 */
	@Override
	public CalendarTextField build()
	{
		Locale lLocale = (iLocale == null ? Locale.getDefault() : iLocale);
		CalendarTextField lCalendarTextField = new CalendarTextField();
		if (iDateFormat != null) lCalendarTextField.setDateFormat(new SimpleDateFormat(iDateFormat, lLocale));
		if (iLocale != null) lCalendarTextField.setLocale(iLocale);
		if (iPromptText != null) lCalendarTextField.setPromptText(iPromptText);
		if (iDateFormats != null) 
		{
			ObservableList<DateFormat> lDateFormats = FXCollections.observableArrayList();
			for (String lPart : iDateFormats) 
			{
				lDateFormats.add( new SimpleDateFormat(lPart.trim(), lLocale) );
			}
			lCalendarTextField.setDateFormats(lDateFormats);
		}
		applyCommonProperties(lCalendarTextField);
		return lCalendarTextField;
	}
	
	/**
	 * Implementation of BuilderService interface
	 */
	@Override
	public boolean isBuilderFor(Class<?> clazz)
	{
		return CalendarTextField.class.isAssignableFrom(clazz);
	}
}
