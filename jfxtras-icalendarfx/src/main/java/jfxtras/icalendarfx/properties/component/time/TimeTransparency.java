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
package jfxtras.icalendarfx.properties.component.time;

import java.util.HashMap;
import java.util.Map;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * TRANSP
 * Time Transparency
 * RFC 5545 iCalendar 3.8.2.7. page 101
 * 
 * This property defines whether or not an event is transparent to busy time searches.
 * Events that consume actual time SHOULD be recorded as OPAQUE.  Other
 * events, which do not take up time SHOULD be recorded as TRANSPARENT.
 *    
 * Example:
 * TRANSP:TRANSPARENT
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 */
public class TimeTransparency extends VPropertyBase<TimeTransparencyType, TimeTransparency>
{
    private final static StringConverter<TimeTransparencyType> CONVERTER = new StringConverter<TimeTransparencyType>()
    {
        @Override
        public String toString(TimeTransparencyType object)
        {
            return object.toString();
        }

        @Override
        public TimeTransparencyType fromString(String string)
        {
            return TimeTransparencyType.enumFromName(string);
        }
    };
    
    public TimeTransparency(TimeTransparencyType value)
    {
        super();
        setConverter(CONVERTER);
        setValue(value);
    }
    
    public TimeTransparency(TimeTransparency source)
    {
        super(source);
    }
    
    public TimeTransparency()
    {
        super();
        setConverter(CONVERTER);
        setValue(TimeTransparencyType.OPAQUE); // default value
    }
    
    public static TimeTransparency parse(String content)
    {
    	return TimeTransparency.parse(new TimeTransparency(), content);
    }
    
    public enum TimeTransparencyType
    {
        OPAQUE,
        TRANSPARENT;
//        UNKNOWN;
        
        private static Map<String, TimeTransparencyType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, TimeTransparencyType> makeEnumFromNameMap()
        {
            Map<String, TimeTransparencyType> map = new HashMap<>();
            TimeTransparencyType[] values = TimeTransparencyType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static TimeTransparencyType enumFromName(String propertyName)
        {
            TimeTransparencyType type = enumFromNameMap.get(propertyName.toUpperCase());
            if (type == null)
            {
                throw new IllegalArgumentException(propertyName + " is not a vaild TimeTransparencyType");
            }
            return type;
        }
    }
}
