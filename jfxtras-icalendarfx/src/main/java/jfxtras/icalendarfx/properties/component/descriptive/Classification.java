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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * CLASS
 * Classification
 * RFC 5545, 3.8.1.3, page 82
 * 
 * This property defines the access classification for a calendar component.
 * 
 * Example:
 * CLASS:PUBLIC
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Classification extends VPropertyBase<ClassificationType, Classification>
{
    // TODO - ADD X-VALUE
    private final static StringConverter<ClassificationType> CONVERTER = new StringConverter<ClassificationType>()
    {
        @Override
        public String toString(ClassificationType object)
        {
            // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
            return (object == ClassificationType.UNKNOWN) ? null: object.toString();
        }

        @Override
        public ClassificationType fromString(String string)
        {
            return ClassificationType.valueOf2(string);
        }
    };
    
    public Classification(ClassificationType type)
    {
        super();
        setConverter(CONVERTER);
        setValue(type);
    }
    
    public Classification(Classification source)
    {
        super(source);
    }
    
    public Classification()
    {
        super();
        setConverter(CONVERTER);
        setValue(ClassificationType.PUBLIC); // default value
    }
    
    public enum ClassificationType
    {
        PUBLIC,
        PRIVATE,
        CONFIDENTIAL,
        UNKNOWN; // must treat as PRIVATE
        
        static ClassificationType valueOf2(String value)
        {
            try
            {
                return valueOf(value);
            } catch (IllegalArgumentException e)
            {
                return UNKNOWN;
            }
        }
    }

    public static Classification parse(String propertyContent)
    {
        Classification property = new Classification();
        property.parseContent(propertyContent);
        return property;
    }
}
