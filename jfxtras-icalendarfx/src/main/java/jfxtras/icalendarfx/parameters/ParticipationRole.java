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
