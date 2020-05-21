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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.FormatType;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * Format Type
 * FMTYPE
 * RFC 5545 iCalendar 3.2.8 page 19
 * 
 * To specify the content type of a referenced object.
 * 
 *  Example:
 *  ATTACH;FMTTYPE=application/msword:ftp://example.com/pub/docs/
 *   agenda.doc
 *   
 *   @see Attachment
 */
public class FormatType extends VParameterBase<FormatType, String>
{
	private static final StringConverter<String> CONVERTER = StringConverters.defaultStringConverterWithQuotes();

    public String getTypeName() { return typeName; }
    private String typeName;
    public void setTypeName(String typeName)
    {
    	this.typeName = typeName;
    	buildNewValue();
	}

    public String getSubtypeName() { return subtypeName; }
    private String subtypeName;
    public void setSubtypeName(String subtypeName)
    {
    	this.subtypeName = subtypeName;
    	buildNewValue();
	}

    // capture type and subtype names
    @Override
    public void setValue(String value)
    {
        int slashIndex = value.indexOf('/');
        if (slashIndex > 0)
        {
            setTypeName(value.substring(0, slashIndex));
            setSubtypeName(value.substring(slashIndex+1));
            super.setValue(value);            
        } else
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires both type-name / subtype-name as defined in RFC4288");
        }
    }
    
	private void buildNewValue()
    {
		if ((getTypeName() != null) && (getSubtypeName() != null))
		{
			String newValue = getTypeName() + "/" + getSubtypeName();
			super.setValue(newValue);
		}
    }

    /*
     * CONSTRUCTORS
     */  
    public FormatType()
    {
        super(CONVERTER);
    }

    public FormatType(FormatType source)
    {
        super(source, CONVERTER);
    }
    
    public static FormatType parse(String content)
    {
    	return FormatType.parse(new FormatType(), content);
    }
}
