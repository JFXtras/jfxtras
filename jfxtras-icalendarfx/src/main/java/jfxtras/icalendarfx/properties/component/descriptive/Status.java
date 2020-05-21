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
package jfxtras.icalendarfx.properties.component.descriptive;

import java.util.HashMap;
import java.util.Map;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.descriptive.Status;
import jfxtras.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * STATUS
 * RFC 5545 iCalendar 3.8.1.11. page 92
 * 
 * This property defines the overall status or confirmation for the calendar component.
 * 
 * Example:
 * STATUS:TENTATIVE
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Status extends VPropertyBase<StatusType, Status>
{
    private final static StringConverter<StatusType> CONVERTER = new StringConverter<StatusType>()
    {
        @Override
        public String toString(StatusType object)
        {
            return object.toString();
        }

        @Override
        public StatusType fromString(String string)
        {
            return StatusType.enumFromName(string);
        }
    };
    
    public Status(StatusType value)
    {
        this();
        setValue(value);
    }
    
    public Status(Status source)
    {
        super(source);
    }
    
    public Status()
    {
        super();
        setConverter(CONVERTER);
    }
    
    public static Status parse(String content)
    {
    	return Status.parse(new Status(), content);
    }
    
    public enum StatusType
    {
        TENTATIVE ("TENTATIVE"),
        CONFIRMED ("CONFIRMED"),
        CANCELLED ("CANCELLED"),
        NEEDS_ACTION ("NEEDS-ACTION"),
        COMPLETED ("COMPLETED"),
        IN_PROCESS ("IN-PROCESS"),
        DRAFT ("DRAFT"),
        FINAL ("FINAL");
        
        private static Map<String, StatusType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, StatusType> makeEnumFromNameMap()
        {
            Map<String, StatusType> map = new HashMap<>();
            StatusType[] values = StatusType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static StatusType enumFromName(String propertyName)
        {
            StatusType type = enumFromNameMap.get(propertyName.toUpperCase());
            if (type == null)
            {
                throw new IllegalArgumentException(propertyName + " is not a vaild StatusType");
            }
            return type;
        }
        
        private String name;
        @Override public String toString() { return name; }
        StatusType(String name)
        {
            this.name = name;
        }
    }
}
