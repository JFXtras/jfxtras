package jfxtras.icalendarfx.parameters;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.VChild;

/**
 * Every parameter requires the following methods:
 * toContentLine - make iCalendar string
 * getValue - return parameters value
 * isEqualsTo - checks equality between two parameters
 * parse - convert string into parameter - this method is in ParameterEnum
 * 
 * @author David Bal
 * @param <T> - parameter value type
 *
 */
public interface Parameter<T> extends Comparable<Parameter<T>>, VChild
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
     * Returns the enumerated type for the parameter as it would appear in the iCalendar content line
     * Examples:
     * VALUE
     * TZID
     * 
     * @return - the parameter type
     */
    ParameterType parameterType();
    
    /**
     * return parameter name-value pair string separated by an "="
     * for example:
     * LANGUAGE=en-US
     */
    @Override
    String toContent();
    
    /*
     * Utility methods
     */    
    static String extractValue(String content)
    {
        int equalsIndex = content.indexOf('=');
        final String valueString;
        if (equalsIndex > 0)
        {
            String name = content.substring(0, equalsIndex);
            boolean hasName1 = ParameterType.enumFromName(name.toUpperCase()) != null;
//            boolean hasName2 = (IANAParameter.getRegisteredIANAParameters() != null) ? IANAParameter.getRegisteredIANAParameters().contains(name.toUpperCase()) : false;
            valueString = (hasName1) ? content.substring(equalsIndex+1) : content;    
        } else
        {
            valueString = content;
        }
        return valueString;
    }
    
    static String extractName(String content)
    {
        int equalsIndex = content.indexOf('=');
        if (equalsIndex > 0)
        {
            String name = content.substring(0, equalsIndex);
            boolean hasName1 = ParameterType.enumFromName(name.toUpperCase()) != null;
//            boolean hasName2 = IANAParameter.getRegisteredIANAParameters().contains(name.toUpperCase());
            return (hasName1) ? name : null;
        }
        return null;
    }
    
}
