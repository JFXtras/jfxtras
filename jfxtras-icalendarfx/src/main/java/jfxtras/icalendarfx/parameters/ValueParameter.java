package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * VALUE
 * Value Date Types
 * RFC 5545 iCalendar 3.2.10 page 29
 * 
 * To explicitly specify the value type format for a property value.
 * 
 *  Example:
 *  DTSTART;VALUE=DATE:20160307
 *   
 * @author David Bal
 *
 */
public class ValueParameter extends ParameterEnumBasedWithUnknown<ValueParameter, ValueType>
{
	private static final StringConverter<ValueType> CONVERTER = new StringConverter<ValueType>()
    {
        @Override
        public String toString(ValueType object)
        {
            return object.toString();
        }

        @Override
        public ValueType fromString(String string)
        {
            return ValueType.enumFromName(string.toUpperCase());
        }
    };
    
    public ValueParameter(ValueParameter source)
    {
        super(source, CONVERTER);
    }
    
    public ValueParameter(ValueType value)
    {
        super(value, CONVERTER);
    }
    
    public ValueParameter()
    {
        super(CONVERTER);
    }
    
    public static ValueParameter parse(String content)
    {
    	return ValueParameter.parse(new ValueParameter(), content);
    }
}
