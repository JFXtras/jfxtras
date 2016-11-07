package jfxtras.icalendarfx.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;

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
    /** Set NEEDS-ACTION as default value */
    public ParticipationStatus()
    {
        super(ParticipationStatusType.NEEDS_ACTION); // default value
    }
  
    public ParticipationStatus(ParticipationStatusType value)
    {
        super(value);
    }
    
    public ParticipationStatus(ParticipationStatus source)
    {
        super(source);
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
        ParticipationStatus parameter = new ParticipationStatus();
        parameter.parseContent(content);
        return parameter;
    }
}