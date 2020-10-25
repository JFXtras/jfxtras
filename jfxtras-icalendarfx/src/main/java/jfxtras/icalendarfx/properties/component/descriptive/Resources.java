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
package jfxtras.icalendarfx.properties.component.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseAltText;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * RESOURCES
 * RFC 5545 iCalendar 3.8.1.10. page 91
 * 
 * This property defines the equipment or resources
 * anticipated for an activity specified by a calendar component.
 * 
 * Examples:
 * RESOURCES:EASEL,PROJECTOR,VCR
 * RESOURCES;LANGUAGE=fr:Nettoyeur haute pression
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 */
public class Resources extends PropBaseAltText<List<String>, Resources>
{
    private final static StringConverter<List<String>> CONVERTER = new StringConverter<List<String>>()
    {
        @Override
        public String toString(List<String> object)
        {
            return object.stream()
                    .map(v -> ValueType.TEXT.getConverter().toString(v)) // escape special characters
                    .collect(Collectors.joining(","));
        }

        @Override
        public List<String> fromString(String string)
        {
            return new ArrayList<>(Arrays.stream(string.replace("\\,", "~~").split(",")) // change comma escape sequence to avoid splitting by it
                    .map(s -> s.replace("~~", "\\,"))
                    .map(v -> (String) ValueType.TEXT.getConverter().fromString(v)) // unescape special characters
                    .collect(Collectors.toList()));
        }
    };

    public Resources(List<String> values)
    {
        this();
        setValue(values);
    }
    
    /** Constructor with varargs of property values 
     * Note: Do not use to parse the content line.  Use static parse method instead.*/
    public Resources(String...values)
    {
        this();
        setValue(new ArrayList<>(Arrays.asList(values)));
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    public Resources()
    {
        super();
        setConverter(CONVERTER);
    }

    public static Resources parse(String content)
    {
    	return Resources.parse(new Resources(), content);
    }
    
    @Override
    protected List<String> copyValue(List<String> source)
    {
        return new ArrayList<String>(source);
    }
}
