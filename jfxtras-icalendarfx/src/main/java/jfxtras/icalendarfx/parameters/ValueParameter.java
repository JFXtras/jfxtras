package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.properties.ValueType;

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
//    private String unknownValue;
    
    public ValueParameter(ValueParameter source)
    {
        super(source);
    }
    
    public ValueParameter(ValueType value)
    {
        super(value);
    }
    
    public ValueParameter()
    {
        super();
    }
    
//    @Override
//    public List<String> parseContent(String content)
//    {
//        setValue(ValueType.enumFromName(content));
//        if (getValue() == ValueType.UNKNOWN)
//        {
//            unknownValue = content;
//        }
//        return errors();
//    } 

    public static ValueParameter parse(String content)
    {
        ValueParameter parameter = new ValueParameter();
        parameter.parseContent(content);
        return parameter;
    }
}
