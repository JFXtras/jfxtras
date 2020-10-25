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
package jfxtras.icalendarfx.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.ParticipationStatus;
import jfxtras.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * PARTSTAT
 * Participation Status
 * RFC 5545, 3.2.12, page 22
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class ParticipationStatus extends ParameterEnumBasedWithUnknown<ParticipationStatus, ParticipationStatusType>
{
	private static final StringConverter<ParticipationStatusType> CONVERTER = new StringConverter<ParticipationStatusType>()
    {
        @Override
        public String toString(ParticipationStatusType object)
        {
            return object.toString();
        }

        @Override
        public ParticipationStatusType fromString(String string)
        {
            return ParticipationStatusType.enumFromName(string.toUpperCase());
        }
    };
    
    /** Set NEEDS-ACTION as default value */
    public ParticipationStatus()
    {
        super(ParticipationStatusType.NEEDS_ACTION, CONVERTER); // default value
    }
  
    public ParticipationStatus(ParticipationStatusType value)
    {
        super(value, CONVERTER);
    }
    
    public ParticipationStatus(ParticipationStatus source)
    {
        super(source, CONVERTER);
    }
    
    public enum ParticipationStatusType
    {
        NEEDS_ACTION (Arrays.asList("NEEDS-ACTION", "NEEDS_ACTION")),  // VEvent, VTodo, VJournal - DEFAULT VALUE
        ACCEPTED (Arrays.asList("ACCEPTED")),          // VEvent, VTodo, VJournal
        COMPLETED (Arrays.asList("COMPLETED")),        // VTodo
        DECLINED (Arrays.asList("DECLINED")),          // VEvent, VTodo, VJournal
        IN_PROCESS (Arrays.asList("IN-PROCESS", "IN_PROCESS")),      // VTodo
        TENTATIVE (Arrays.asList("TENTATIVE")),        // VEvent, VTodo
        DELEGATED (Arrays.asList("DELEGATED")),        // VEvent, VTodo
        UNKNOWN (Arrays.asList("UNKNOWN"));
        
        private static Map<String, ParticipationStatusType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, ParticipationStatusType> makeEnumFromNameMap()
        { // map with multiple names for each type
            Map<String, ParticipationStatusType> map = new HashMap<>();
            Arrays.stream(ParticipationStatusType.values())
                    .forEach(r -> r.names.stream().forEach(n -> map.put(n, r)));
            return map;
        }

        /** get enum from name */
        public static ParticipationStatusType enumFromName(String propertyName)
        {
            ParticipationStatusType type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
        
        private List<String> names;
        @Override public String toString() { return names.get(0); } // name at index 0 is the correct name from RFC 5545
        ParticipationStatusType(List<String> names)
        {
            this.names = names;
        }
    }
    
    public static ParticipationStatus parse(String content)
    {
    	return ParticipationStatus.parse(new ParticipationStatus(), content);
    }
}