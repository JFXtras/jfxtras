package jfxtras.icalendarfx.parameters;

import java.time.ZoneId;

/**
 * TZID
 * Time Zone Identifier
 * RFC 5545, 3.2.19, page 27
 * 
 * To specify the identifier for the time zone definition for
 *  a time component in the property value.
 * 
 * Example:
 * DTSTART;TZID=America/New_York:19980119T020000
 * 
 * @author David Bal
 *
 */
public class TimeZoneIdentifierParameter extends ParameterBase<TimeZoneIdentifierParameter, ZoneId>
{    
    public TimeZoneIdentifierParameter()
    {
        super();
    }
    
    public TimeZoneIdentifierParameter(ZoneId value)
    {
        super(value);
    }
    
    public TimeZoneIdentifierParameter(TimeZoneIdentifierParameter source)
    {
        super(source);
    }

    public static TimeZoneIdentifierParameter parse(String content)
    {
        TimeZoneIdentifierParameter parameter = new TimeZoneIdentifierParameter();
        parameter.parseContent(content);
        return parameter;
    }
}
