package jfxtras.icalendarfx.properties.component.timezone;

import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.properties.PropBaseLanguage;

/**
 * TZNAME
 * Time Zone Name
 * RFC 5545, 3.8.3.2, page 103
 * 
 * This property specifies the customary designation for a time zone description.
 * 
 * EXAMPLES:
 * TZNAME:EST
 * TZNAME;LANGUAGE=fr-CA:HN
 * 
 * @author David Bal
 * @see DaylightSavingsTime
 * @see StandardTime
 */
public class TimeZoneName extends PropBaseLanguage<String, TimeZoneName>
{
//    public TimeZoneName(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    public TimeZoneName(TimeZoneName source)
    {
        super(source);
    }

    public TimeZoneName()
    {
        super();
    }
    
    public static TimeZoneName parse(String propertyContent)
    {
        TimeZoneName property = new TimeZoneName();
        property.parseContent(propertyContent);
        return property;
    }
}
