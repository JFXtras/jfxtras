/**
 * AbstractLocalDateTimeAPITextFieldBuilder.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Tom Eugelink
 *
 */
public class AbstractLocalDateTimeAPITextFieldBuilder extends AbstractBuilder
{
	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { iLocale = Locale.forLanguageTag(value); }
	protected Locale iLocale = null;

	/** PromptText */
	public String getPromptText() { return null; } // dummy, just to make it Java Bean compatible
	public void setPromptText(String value) { iPromptText = value; }
	protected String iPromptText = null;

	/** DateTimeFormatter */
	public String getDateTimeFormatter() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateTimeFormatter(String value) { iDateTimeFormatter = value; }
	protected String iDateTimeFormatter = null;

	/** DateTimeFormatters */
	public String getDateTimeFormatters() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateTimeFormatters(String value) 
	{  
		String[] lParts = value.split(",");
		iDateTimeFormatters = new ArrayList<>();
		for (String lPart : lParts) 
		{
			iDateTimeFormatters.add( lPart.trim() );
		}
	}
	protected List<String> iDateTimeFormatters = null;
}
