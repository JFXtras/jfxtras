package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public enum FrequencyType
{
    YEARLY (ChronoUnit.YEARS),
    MONTHLY (ChronoUnit.MONTHS),
    WEEKLY (ChronoUnit.WEEKS),
    DAILY (ChronoUnit.DAYS),
    HOURLY (ChronoUnit.HOURS),
    MINUTELY (ChronoUnit.MINUTES),
    SECONDLY (ChronoUnit.SECONDS);
    
    ChronoUnit chronoUnit;
    FrequencyType(ChronoUnit chronoUnit)
    {
        this.chronoUnit = chronoUnit;
    }
    public ChronoUnit getChronoUnit() { return chronoUnit; }

    // Map to match up string name to enum
    private static Map<String, FrequencyType> propertyFromNameMap = makePropertiesFromNameMap();
    private static Map<String, FrequencyType> makePropertiesFromNameMap()
    {
        Map<String, FrequencyType> map = new HashMap<>();
        FrequencyType[] values = FrequencyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    /** get enum from name */
    public static FrequencyType propertyFromName(String propertyName)
    {
        return propertyFromNameMap.get(propertyName.toUpperCase());
    }
}
