package jfxtras.icalendarfx.properties.calendar;

import javafx.util.StringConverter;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;

/**
 * METHOD
 * RFC 5545, 3.7.2, page 77
 * 
 * This property defines the iCalendar object method associated with the calendar object.
 * 
 * No methods are defined by this specification.  This is the subject
 * of other specifications, such as the iCalendar Transport-
 * independent Interoperability Protocol (iTIP) defined by [2446bis]
 * 
 * Example:
 * METHOD:PUBLISH
 * 
 * @author David Bal
 * @see VCalendar
 */
public class Method extends PropertyBase<MethodType, Method> implements VElement
{
    private final static StringConverter<MethodType> CONVERTER = new StringConverter<MethodType>()
    {
        @Override
        public String toString(MethodType object)
        {
            return object.toString();
        }

        @Override
        public MethodType fromString(String string)
        {
            return MethodType.valueOf(string.toUpperCase());
        }
    };
    
    public Method(Method source)
    {
        super(source);
    }

    public Method(MethodType methodType)
    {
       super(methodType);
       setConverter(CONVERTER);
    }
    
    public Method()
    {
       super();
       setConverter(CONVERTER);
    }
    
    public static Method parse(String string)
    {
        Method property = new Method();
        property.parseContent(string);
        return property;
    }
    
    /** Method types from RFC 5546 */
    public enum MethodType
    {
        PUBLISH,
        REQUEST,
        REPLY,
        ADD,
        CANCEL,
        REFRESH,
        COUNTER,
        DECLINECOUNTER;
    }
}
