package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * PRIORITY
 * RFC 5545 iCalendar 3.8.1.9. page 89
 * 
 * This property defines the relative priority for a calendar component.
 * 
 * A value of 0 specifies an undefined priority.  A value of 1
 * is the highest priority.  A value of 2 is the second highest
 * priority.  Subsequent numbers specify a decreasing ordinal
 * priority.  A value of 9 is the lowest priority.
 *     
 * Example:
 * The following is an example of a property with the highest priority:
 * PRIORITY:1
 *  
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 */
public class Priority extends PropertyBase<Integer, Priority>
{
    public Priority(Priority source)
    {
        super(source);
    }
    
    public Priority(Integer value)
    {
        super(value);
    }
    
    public Priority()
    {
        super();
    }

    @Override
    public void setValue(Integer value)
    {
        if ((value < 0) || (value > 9))
        {
            throw new IllegalArgumentException("Priority must between 0 and 9");
        }
        super.setValue(value);
    }
    
    @Override
    public boolean isValid()
    {
        return ((getValue() < 0) || (getValue() > 9)) ? false : super.isValid();
    }

    public static Priority parse(String propertyContent)
    {
        Priority property = new Priority();
        property.parseContent(propertyContent);
        return property;
    }
}
