package jfxtras.icalendarfx.properties.component.timezone;

import java.net.URI;

import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.properties.VPropertyBase;

/**
 * TZURL
 * Time Zone URL
 * RFC 5545, 3.8.3.5, page 106
 * 
 * This property provides a means for a "VTIMEZONE" component
 * to point to a network location that can be used to retrieve an up-
 * to-date version of itself
 * 
 * EXAMPLES:
 * TZURL:http://timezones.example.org/tz/America-Los_Angeles.ics
 * 
 * @author David Bal
 * @see VTimeZone
 */
public class TimeZoneURL extends VPropertyBase<URI,TimeZoneURL>
{
    public TimeZoneURL(TimeZoneURL source)
    {
        super(source);
    }
    
    public TimeZoneURL(URI value)
    {
        super(value);
    }
    
    public TimeZoneURL()
    {
        super();
    }

    public static TimeZoneURL parse(String content)
    {
    	return TimeZoneURL.parse(new TimeZoneURL(), content);
    }
}
