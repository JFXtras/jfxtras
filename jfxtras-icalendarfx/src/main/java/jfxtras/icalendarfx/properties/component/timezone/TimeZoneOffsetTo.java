package jfxtras.icalendarfx.properties.component.timezone;

import java.time.ZoneOffset;

import jfxtras.icalendarfx.components.StandardTime;

/**
 * TZOFFSETTO
 * Time Zone Offset To
 * RFC 5545, 3.8.3.4, page 105
 * 
 * This property specifies the offset that is in use in this time zone observance
 * 
 * EXAMPLES:
 * TZOFFSETTO:-0400
 * TZOFFSETTO:+1245
 * 
 * @author David Bal
 * @see DaylightSavingsTime
 * @see StandardTime
 */
public class TimeZoneOffsetTo extends PropertyBaseZoneOffset<TimeZoneOffsetTo>
{    
//    public TimeZoneOffsetTo(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    public TimeZoneOffsetTo(TimeZoneOffsetTo source)
    {
        super(source);
    }
    
    public TimeZoneOffsetTo(ZoneOffset value)
    {
        super(value);
    }
    
    public TimeZoneOffsetTo()
    {
        super();
    }

    public static TimeZoneOffsetTo parse(String propertyContent)
    {
        TimeZoneOffsetTo property = new TimeZoneOffsetTo();
        property.parseContent(propertyContent);
        return property;
    }
}
