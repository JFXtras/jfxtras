/**
 * LocalTimeTextFieldBuilder.java
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

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.fxml.BuilderService;
import jfxtras.scene.control.LocalTimeTextField;

/**
 * @author Tom Eugelink
 *
 */
public class LocalTimeTextFieldBuilder extends AbstractLocalDateTimeAPITextFieldBuilder implements BuilderService<LocalTimeTextField>
{
	/**
	 * Implementation of Builder interface
	 */
	@Override
	public LocalTimeTextField build()
	{		
		Locale lLocale = (iLocale == null ? Locale.getDefault() : iLocale);
		LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
		if (iDateTimeFormatter != null) lLocalTimeTextField.setDateTimeFormatter( DateTimeFormatter.ofPattern(iDateTimeFormatter).withLocale(lLocale));
		//if (iLocale != null) lLocalTimeTextField.setLocale(iLocale);
		if (iPromptText != null) lLocalTimeTextField.setPromptText(iPromptText);
		if (iDateTimeFormatters != null) 
		{
			ObservableList<DateTimeFormatter> lDateTimeFormatters = FXCollections.observableArrayList();
			for (String lPart : iDateTimeFormatters) 
			{
				lDateTimeFormatters.add( DateTimeFormatter.ofPattern(lPart.trim()).withLocale(lLocale) );
			}
			lLocalTimeTextField.setDateTimeFormatters(lDateTimeFormatters);
		}
		applyCommonProperties(lLocalTimeTextField);
		return lLocalTimeTextField;
	}
	
	/**
	 * Implementation of BuilderService interface
	 */
	@Override
	public boolean isBuilderFor(Class<?> clazz)
	{
		return LocalTimeTextField.class.isAssignableFrom(clazz);
	}
}
