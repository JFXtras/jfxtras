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
package jfxtras.icalendarfx.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.ParticipationRole;
import jfxtras.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * ROLE
 * Participation Role
 * RFC 5545, 3.2.16, page 25
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class ParticipationRole extends ParameterEnumBasedWithUnknown<ParticipationRole, ParticipationRoleType>
{
	private static final StringConverter<ParticipationRoleType> CONVERTER = new StringConverter<ParticipationRoleType>()
    {
        @Override
        public String toString(ParticipationRoleType object)
        {
            return object.toString();
        }

        @Override
        public ParticipationRoleType fromString(String string)
        {
            return ParticipationRoleType.enumFromName(string.toUpperCase());
        }
    };
    
    public ParticipationRole()
    {
        super(ParticipationRoleType.REQUIRED_PARTICIPANT, CONVERTER); // default value
    }
  
    public ParticipationRole(ParticipationRoleType value)
    {
        super(value, CONVERTER);
    }
    
    public ParticipationRole(ParticipationRole source)
    {
        super(source, CONVERTER);
    }
    
    public enum ParticipationRoleType
    {
        CHAIR (Arrays.asList("CHAIR")),
        REQUIRED_PARTICIPANT (Arrays.asList("REQ-PARTICIPANT", "REQ_PARTICIPANT")), // Yahoo calendar uses underscore
        OPTIONAL_PARTICIPANT (Arrays.asList("OPT-PARTICIPANT", "OPT_PARTICIPANT")),
        NON_PARTICIPANT (Arrays.asList("NON-PARTICIPANT", "NON_PARTICIPANT")),
        UNKNOWN (Arrays.asList("UNKNOWN"));
        
        private static Map<String, ParticipationRoleType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, ParticipationRoleType> makeEnumFromNameMap()
        { // map with multiple names for each type
            Map<String, ParticipationRoleType> map = new HashMap<>();
            Arrays.stream(ParticipationRoleType.values())
                    .forEach(r -> r.names.stream().forEach(n -> map.put(n, r)));
            return map;
        }
        /** get enum from name */
        public static ParticipationRoleType enumFromName(String propertyName)
        {
            ParticipationRoleType type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
        
        private List<String> names;
        @Override public String toString() { return names.get(0); } // name at index 0 is the correct name from RFC 5545
        ParticipationRoleType(List<String> names)
        {
            this.names = names;
        }
    }
    
    public static ParticipationRole parse(String content)
    {
    	return ParticipationRole.parse(new ParticipationRole(), content);
    }
}
