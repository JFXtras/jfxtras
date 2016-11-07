package jfxtras.icalendarfx.properties.component.timezone;

import java.time.ZoneOffset;

import jfxtras.icalendarfx.components.StandardTime;

/**
 * TZOFFSETFROM
 * Time Zone Offset From
 * RFC 5545, 3.8.3.3, page 104
 * 
 * This property specifies the offset that is in use prior to this time zone observance.
 * 
 * EXAMPLES:
 * TZOFFSETFROM:-0500
 * TZOFFSETFROM:+1345
 * 
 * @author David Bal
 * @see DaylightSavingsTime
 * @see StandardTime
 */
public class TimeZoneOffsetFrom extends PropertyBaseZoneOffset<TimeZoneOffsetFrom>
{    
//    public TimeZoneOffsetFrom(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    public TimeZoneOffsetFrom(TimeZoneOffsetFrom source)
    {
        super(source);
    }
    
    public TimeZoneOffsetFrom(ZoneOffset value)
    {
        super(value);
    }
    
    public TimeZoneOffsetFrom()
    {
        super();
    }

    public static TimeZoneOffsetFrom parse(String propertyContent)
    {
        TimeZoneOffsetFrom property = new TimeZoneOffsetFrom();
        property.parseContent(propertyContent);
        return property;
    }
}
