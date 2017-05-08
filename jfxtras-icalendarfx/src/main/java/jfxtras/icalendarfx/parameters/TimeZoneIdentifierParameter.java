package jfxtras.icalendarfx.parameters;

import java.time.ZoneId;

import jfxtras.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;

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
public class TimeZoneIdentifierParameter extends VParameterBase<TimeZoneIdentifierParameter, ZoneId>
{
	private static final StringConverter<ZoneId> CONVERTER = new StringConverter<ZoneId>()
    {
        @Override
        public String toString(ZoneId object)
        {
            return object.toString();
        }

        @Override
        public ZoneId fromString(String string)
        {
            return ZoneId.of(string);
        }
    };
    
    public TimeZoneIdentifierParameter()
    {
        super(CONVERTER);
    }
    
    public TimeZoneIdentifierParameter(ZoneId value)
    {
        super(value, CONVERTER);
    }
    
    public TimeZoneIdentifierParameter(TimeZoneIdentifierParameter source)
    {
        super(source, CONVERTER);
    }
    
    public static TimeZoneIdentifierParameter parse(String content)
    {
    	return TimeZoneIdentifierParameter.parse(new TimeZoneIdentifierParameter(), content);
    }
}
