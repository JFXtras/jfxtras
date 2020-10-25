/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties;

import java.net.URI;

import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.properties.PropAltText;
import jfxtras.icalendarfx.properties.PropBaseAltText;
import jfxtras.icalendarfx.properties.PropBaseLanguage;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.relationship.Contact;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * Property with language, alternate text display, and a text-based value
 *  
 * @param <U>
 * 
 * concrete subclasses
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 */
public abstract class PropBaseAltText<T,U> extends PropBaseLanguage<T,U> implements PropAltText<T>
{
    /**
     * ALTREP : Alternate Text Representation
     * To specify an alternate text representation for the property value.
     * 
     * Example:
     * DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
     *  Project XYZ Review Meeting will include the following agenda
     *   items: (a) Market Overview\, (b) Finances\, (c) Project Man
     *  agement
     *
     *The "ALTREP" property parameter value might point to a "text/html"
     *content portion.
     *
     * Content-Type:text/html
     * Content-Id:<part3.msg.970415T083000@example.com>
     *
     * <html>
     *   <head>
     *    <title></title>
     *   </head>
     *   <body>
     *     <p>
     *       <b>Project XYZ Review Meeting</b> will include
     *       the following agenda items:
     *       <ol>
     *         <li>Market Overview</li>
     *         <li>Finances</li>
     *         <li>Project Management</li>
     *       </ol>
     *     </p>
     *   </body>
     * </html>
     */
    @Override
    public AlternateText getAlternateText() { return alternateText; }
    private AlternateText alternateText;
    @Override
    public void setAlternateText(AlternateText alternateText)
    {
    	orderChild(this.alternateText, alternateText);
    	this.alternateText = alternateText;
	}
    public void setAlternateText(String value)
    {
    	setAlternateText(AlternateText.parse(new AlternateText(), value));
	}
    public U withAlternateText(AlternateText altrep)
    {
    	setAlternateText(altrep);
    	return (U) this;
	}
    public U withAlternateText(URI value)
    {
    	setAlternateText(new AlternateText(value));
    	return (U) this;
	}
    public U withAlternateText(String content)
    {
    	setAlternateText(content);
    	return (U) this;
	}
    
    /*
     * CONSTRUCTORS
     */    
    // copy constructor
    public PropBaseAltText(PropBaseAltText<T,U> property)
    {
        super(property);
    }

    public PropBaseAltText(T value, StringConverter<T> converter)
    {
        super(value);
    }
    
    protected PropBaseAltText()
    {
        super();
    }
}
