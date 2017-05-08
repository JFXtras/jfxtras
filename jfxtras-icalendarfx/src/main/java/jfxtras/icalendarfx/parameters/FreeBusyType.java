package jfxtras.icalendarfx.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.icalendarfx.parameters.FreeBusyType;
import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.icalendarfx.properties.component.time.FreeBusyTime;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * FBTYPE
 * Free/Busy Time Type
 * RFC 5545, 3.2.9, page 20
 * 
 * To specify the free or busy time type.
 * 
 * Example:
 * FREEBUSY;FBTYPE=BUSY:19980415T133000Z/19980415T170000Z
 * 
 * @author David Bal
 * @see FreeBusyTime
 */
public class FreeBusyType extends ParameterEnumBasedWithUnknown<FreeBusyType, FreeBusyTypeEnum>
{
	private static final StringConverter<FreeBusyTypeEnum> CONVERTER = new StringConverter<FreeBusyTypeEnum>()
    {
        @Override
        public String toString(FreeBusyTypeEnum object)
        {
            return object.toString();
        }

        @Override
        public FreeBusyTypeEnum fromString(String string)
        {
            return FreeBusyTypeEnum.enumFromName(string.toUpperCase());
        }
    };
    
    /** set BUSY as default FreeBusy type value */
    public FreeBusyType()
    {
        super(FreeBusyTypeEnum.BUSY, CONVERTER); // default value
    }
  
    public FreeBusyType(FreeBusyTypeEnum value)
    {
        super(value, CONVERTER);
    }
    
    public FreeBusyType(FreeBusyType source)
    {
        super(source, CONVERTER);
    }
    
    public enum FreeBusyTypeEnum
    {
        FREE (Arrays.asList("FREE")), // the time interval is free for scheduling
        BUSY (Arrays.asList("BUSY")), // the time interval is busy because one or more events have been scheduled for that interval - THE DEFAULT
        BUSY_UNAVAILABLE (Arrays.asList("BUSY-UNAVAILABLE", "BUSY_UNAVAILABLE")), // the time interval is busy and that the interval can not be scheduled
        BUSY_TENTATIVE (Arrays.asList("BUSY-TENTATIVE", "BUSY_TENTATIVE")), // the time interval is busy because one or more events have been tentatively scheduled for that interval
        UNKNOWN (Arrays.asList("UNKNOWN"));

        private static Map<String, FreeBusyTypeEnum> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, FreeBusyTypeEnum> makeEnumFromNameMap()
        { // map with multiple names for each type
            Map<String, FreeBusyTypeEnum> map = new HashMap<>();
            Arrays.stream(FreeBusyTypeEnum.values())
                    .forEach(r -> r.names.stream().forEach(n -> map.put(n, r)));
            return map;
        }
        
        /** get enum from name */
        public static FreeBusyTypeEnum enumFromName(String propertyName)
        {
            FreeBusyTypeEnum type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
        
        private List<String> names;
        @Override public String toString() { return names.get(0); } // name at index 0 is the correct name from RFC 5545
        FreeBusyTypeEnum(List<String> names)
        {
            this.names = names;
        }
    }
    
    public static FreeBusyType parse(String content)
    {
    	return FreeBusyType.parse(new FreeBusyType(), content);
    }
}
