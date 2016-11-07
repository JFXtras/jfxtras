package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * PERCENT-COMPLETE
 * RFC 5545 iCalendar 3.8.1.8. page 88
 * 
 * This property is used by an assignee or delegatee of a
 * to-do to convey the percent completion of a to-do to the "Organizer".
 * The property value is a positive integer between 0 and
 * 100.  A value of "0" indicates the to-do has not yet been started.
 * A value of "100" indicates that the to-do has been completed.
 * 
 * Example:  The following is an example of this property to show 39% completion:
 * PERCENT-COMPLETE:39
 *  
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VTodo
 */
public class PercentComplete extends PropertyBase<Integer, PercentComplete>
{
    public PercentComplete(PercentComplete source)
    {
        super(source);
    }
    
    public PercentComplete(Integer value)
    {
        super(value);
    }

    public PercentComplete()
    {
        super();
    }
    
    public static PercentComplete parse(String propertyContent)
    {
        PercentComplete property = new PercentComplete();
        property.parseContent(propertyContent);
        return property;
    }
}
