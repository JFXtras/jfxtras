package jfxtras.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.parameters.ValueParameter;

/**
 * top-level interface for all iCalendar properties
 * 
 * @author David Bal
 * @see PropertyType - enum of all supported Properties
 * @see PropertyBase
 *
 * @param <T> - type of value stored in Property
 */
public interface Property<T> extends VParent, VChild, Comparable<Property<T>>
{    
//    /**
//     * Property Name
//     * 
//     * The name of the property, such as DESCRIPTION
//     * Remains the default value from {@link #PropertyEnum} unless set by a non-standard property
//     * */
//    String name();
    /**
     * The value of the property.
     * 
     * For example, in the below property:
     * LOCATION;LANGUAGE=en:Bob's house
     * The value is the String "Bob's house"
     * 
     */
    T getValue();
    /** property value object property */
    ObjectProperty<T> valueProperty();    
    /** Set the value of the property */
    void setValue(T value);
        
    /**
     * VALUE
     * Value Date Types
     * RFC 5545 iCalendar 3.2.10 page 29
     * 
     * To explicitly specify the value type format for a property value.
     * 
     * Property value type.  Optional if default type is used.
     * Example:
     * VALUE=DATE
     */
    ValueParameter getValueType();
    /** property for value type */
    ObjectProperty<ValueParameter> valueTypeProperty();
    /** Set the value type */
    void setValueType(ValueParameter value);

    /**
     * <h2>Non-Standard Parameters</h2>
     * 
     * <p>x-param     = x-name "=" param-value *("," param-value)<br>
     ; A non-standard, experimental parameter.</p>
     */
    ObjectProperty<ObservableList<NonStandardParameter>> nonStandardParameter();
    ObservableList<NonStandardParameter> getNonStandard();
    void setNonStandard(ObservableList<NonStandardParameter> nonStandardParams);
    
//    /**
//     * IANA Registered Parameters
//     * 
//     *<p>Allows other properties registered
//     * with IANA to be specified in any calendar components.</p>
//     */
//    ObjectProperty<ObservableList<IANAParameter>> IANAParameter();
//    ObservableList<IANAParameter> getIana();
//    void setIana(ObservableList<IANAParameter> ianaParams);
    
//    // TODO - make non-standard parameter as regular parameter
//    /**
//     * Non-standard parameters
//     * 
//     * other-param, 3.2 RFC 5545 page 14
//     * the parameter name and value are combined into one object
//     */
//    @Deprecated
//    ObservableList<OtherParameter> getOtherParameters();
//    @Deprecated
//    void setOtherParameters(ObservableList<OtherParameter> otherParameters);
    
    /**
     * Returns the enumerated type for the property as it would appear in the iCalendar content line
     * Examples:
     * DESCRIPTION
     * UID
     * PRODID
     * 
     * @return - the property type
     */
    PropertyType propertyType();

//    /**
//     * Get the property's value string converter.  There is a default converter in ValueType associated
//     * with the default value type of the property.  For most value types that converter is
//     * acceptable.  However, for the TEXT value type it often needs to be replaced.
//     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
//     * ZoneId.  A different converter is required to make the conversion to ZoneId.
//     * 
//     * @return the string converter for this property
//     */
//    StringConverter<T> getConverter();
//    /**
//     * Set the property's value string converter.  There is a default converter in ValueType associated
//     * with the default value type of the property.  For most value types that converter is
//     * acceptable.  However, for the TEXT value type it often needs to be replaced.
//     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
//     * ZoneId.  A different converter is required to make the conversion to ZoneId.
//     * This method can replace the default converter. 
//     */
//    void setConverter(StringConverter<T> converter);
}
