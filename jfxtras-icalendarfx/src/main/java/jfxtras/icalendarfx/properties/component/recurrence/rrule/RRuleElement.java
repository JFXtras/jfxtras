package jfxtras.icalendarfx.properties.component.recurrence.rrule;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VElement;

public interface RRuleElement<T> extends VElement, VChild
{
    /**
     * The value of the parameter.
     * 
     * For example, in the below parameter:
     * CN=John Doe
     * The value is the String "John Doe"
     * 
     * Note: the value's object must have an overridden toString method that complies
     * with iCalendar content line output.
     */
    T getValue();
    
    /** object property of parameter's value */
    ObjectProperty<T> valueProperty();
  
    /** Set the value of this parameter */  
    void setValue(T value);
    
    /**
     * Returns the enumerated type for the element as it would appear in the iCalendar content line
     * Examples:
     * FREQUENCY
     * UNTIL
     * 
     * @return - the element type
     */
    RRuleElementType elementType();
    
    /**
     * return element name-value pair string separated by an "="
     * for example:
     * FREQ=DAILY
     */
    @Override
    default String toContent()
    {
        return RRuleElementType.enumFromClass(getClass()).toString() + "=" + getValue().toString();
    }
}
