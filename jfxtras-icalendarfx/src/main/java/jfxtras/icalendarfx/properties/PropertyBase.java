package jfxtras.icalendarfx.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.VParentBase;
import jfxtras.icalendarfx.content.SingleLineContent;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.parameters.Parameter;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.Method;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

/**
 * Base iCalendar property class
 * Contains property value, value parameter (ValueType) and other-parameters
 * Also contains several support methods used by other properties
 * 
 * concrete subclasses
 * @see UniqueIdentifier
 * @see CalendarScale
 * @see Method
 * @see ProductIdentifier
 * @see Version
 * 
 * @author David Bal
 *
 * @param <U> - type of implementing subclass
 * @param <T> - type of property value
 */
public abstract class PropertyBase<T,U> extends VParentBase implements Property<T>, Comparable<Property<T>>
{
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    /**
     * PROPERTY VALUE
     * 
     * Example: for the property content LOCATION:The park the property
     * value is the string "The park"
     */
    @Override
    public T getValue() { return value.get(); }
    @Override
    public ObjectProperty<T> valueProperty() { return value; }
    private ObjectProperty<T> value; // initialized in constructor
    @Override
    public void setValue(T value)
    {
        this.value.set(value);
    }
    public U withValue(T value) { setValue(value); return (U) this; } // in constructor

    /** The propery's value converted by string converted to content string */
    protected String valueContent()
    {
        /* default code below works for all properties with a single value.  Properties with multiple embedded values,
         * such as RequestStatus, require an overridden method */
        return (getConverter().toString(getValue()) == null) ? getUnknownValue() : getConverter().toString(getValue());
    }

    // class of value.  Used to verify value class is allowed for the property type
    private Class<T> valueClass;
    private Class<?> getValueClass()
    {
        if (valueClass == null)
        {
            if (getValue() != null)
            {
                if (getValue() instanceof Collection)
                {
                    if (! ((Collection<?>) getValue()).isEmpty())
                    {
                        return ((Collection<?>) getValue()).iterator().next().getClass();
                    } else
                    {
                        return null;
                    }
                }
                return getValue().getClass();
            }
            return null;
        } else
        {
            return valueClass;
        }
    }
    
    /** The name of the property, such as DESCRIPTION
     * Remains the default value unless set by a non-standard property*/
    @Override
    public String name()
    {
        return propertyName.get();
    }
    public ObjectProperty<String> propertyNameProperty() { return propertyName; }
    private ObjectProperty<String> propertyName =  new SimpleObjectProperty<String>();
    /** Set the name of the property.  Only allowed for non-standard and IANA properties */
    public void setPropertyName(String name)
    {
        if (propertyType().equals(PropertyType.NON_STANDARD))
        {
            if (name.substring(0, 2).toUpperCase().equals("X-"))
            {
                propertyName.set(name);
            } else
            {
                throw new RuntimeException("Non-standard properties must begin with X-");                
            }
//        } else if (propertyType().equals(PropertyType.IANA_PROPERTY))
//        {
//            if ((IANAProperty.getRegisteredIANAPropertys() != null) && IANAProperty.getRegisteredIANAPropertys().contains(name))
//            {
//                propertyName.set(name);
//            } else
//            {
//                throw new RuntimeException(name + " is not an IANA-registered property name.  The name can be registered by adding it to the IANAProperty.registeredIANAPropertys list");
//            }
        } else if (propertyType().toString().equals(name)) // let setting name to default value have no operation
        {
            propertyName.set(name);
        } else
        {
            throw new RuntimeException("Custom property names can only be set for non-standard and IANA-registered properties (" + name + ")");
        }
    }
    public U withPropertyName(String name) { setPropertyName(name); return (U) this; }
    
    /**
     * PROPERTY TYPE
     * 
     *  The enumerated type of the property.
     */
    @Override
    public PropertyType propertyType() { return propertyType; }
    final private PropertyType propertyType;
    
    /*
     * Unknown values
     * contains exact string for unknown property value
     */
    private String unknownValue;
    protected String getUnknownValue() { return unknownValue; }
    private void setUnknownValue(String value) { unknownValue = value; }
    
    /**
     * VALUE TYPE
     * Value Data Types
     * RFC 5545, 3.2.20, page 29
     * 
     * To specify the value for text values in a property or property parameter.
     * This parameter is optional for properties when the default value type is used.
     * 
     * Examples:
     * VALUE=DATE-TIME  (Date-Time is default value, so it isn't necessary to specify)
     * VALUE=DATE
     */
    @Override
    public ValueParameter getValueType() { return valueType.get(); }
    @Override
    public ObjectProperty<ValueParameter> valueTypeProperty() { return valueType; }
    private ObjectProperty<ValueParameter> valueType = new SimpleObjectProperty<>(this, ParameterType.VALUE_DATA_TYPES.toString());
    @Override
    public void setValueType(ValueParameter valueType)
    {
        if (valueType == null || isValueTypeValid(valueType.getValue()))
        {
            valueTypeProperty().set(valueType);
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + valueType.getValue() + ", allowed = " + propertyType().allowedValueTypes());
        }
    }
    public void setValueType(ValueType value)
    {
        setValueType(new ValueParameter(value));
    }
    public void setValueType(String value)
    {
        setValueType(ValueParameter.parse(value));
    }
    public U withValueType(ValueType value)
    {
        setValueType(value);
        return (U) this;
    } 
    public U withValueType(String value)
    {
        setValueType(value);
        return (U) this;
    }
    // Synch value with type produced by string converter
    private final ChangeListener<? super ValueParameter> valueParameterChangeListener = (observable, oldValue, newValue) ->
    {
        if (! isCustomConverter())
        {
            // Convert property value string, if present
            if (getPropertyValueString() != null)
            {
                T newPropValue = getConverter().fromString(getPropertyValueString());
                setValue(newPropValue);
            }
        }
        
        // verify value class is allowed
        if (newValue != null && getValueClass() != null) // && ! newValue.getValue().allowedClasses().contains(getValueClass()))
        {
            boolean isMatch = newValue.getValue().allowedClasses()
                    .stream()
                    .map(c -> getValueClass().isAssignableFrom(c))
                    .findAny()
                    .isPresent();
            if (! isMatch)
            {
                throw new IllegalArgumentException("Value class " + getValueClass().getSimpleName() +
                        " doesn't match allowed value classes: " + newValue.getValue().allowedClasses());
            }
        }
    };
    
    /**
     * <h2>NON-STANDARD PARAMETERS</h2>
     * 
     * <p>x-param     = x-name "=" param-value *("," param-value)<br>
     ; A non-standard, experimental parameter.</p>
     */
    @Override
    public ObjectProperty<ObservableList<NonStandardParameter>> nonStandardParameter()
    {
        if (nonStandardParams == null)
        {
            nonStandardParams = new SimpleObjectProperty<>(this, ParameterType.NON_STANDARD.toString());
        }
        return nonStandardParams;
    }
    private ObjectProperty<ObservableList<NonStandardParameter>> nonStandardParams;
    @Override
    public ObservableList<NonStandardParameter> getNonStandard()
    {
        return (nonStandardParams == null) ? null : nonStandardParams.get();
    }
    @Override
    public void setNonStandard(ObservableList<NonStandardParameter> nonStandardParams)
    {
        if (nonStandardParams != null)
        {
            orderer().registerSortOrderProperty(nonStandardParams);
        } else
        {
            orderer().unregisterSortOrderProperty(nonStandardParameter().get());
        }
        nonStandardParameter().set(nonStandardParams);
    }
    /**
     * NON-STANDARD PARAMETERS
     * 
     * Sets the value of the {@link #NonStandardParameter()} by parsing a vararg of
     * iCalendar content text representing individual {@link NonStandardParameter} objects.
     * 
     * @return - this class for chaining
     */
    public U withNonStandard(String...nonStandardParams)
    {
        List<NonStandardParameter> a = Arrays.stream(nonStandardParams)
                .map(c -> NonStandardParameter.parse(c))
                .collect(Collectors.toList());
        setNonStandard(FXCollections.observableArrayList(a));
        return (U) this;
    }
    /**
     * Sets the value of the {@link #NonStandardParameter()}
     * 
     * @return - this class for chaining
     */
    public U withNonStandard(ObservableList<NonStandardParameter> nonStandardParams)
    {
        setNonStandard(nonStandardParams);
        return (U) this;
    }
    /**
     * Sets the value of the {@link #NonStandardParameter()} from a vararg of {@link NonStandardParameter} objects.
     * 
     * @return - this class for chaining
     */    
    public U withNonStandard(NonStandardParameter...nonStandardParams)
    {
        setNonStandard(FXCollections.observableArrayList(nonStandardParams));
        return (U) this;
    }
    
//    /**
//     * IANA PARAMETERS
//     * 
//     *<p>Allows other properties registered
//     * with IANA to be specified in any calendar components.</p>
//     */
//    @Override
//    public ObjectProperty<ObservableList<IANAParameter>> IANAParameter()
//    {
//        if (ianaParams == null)
//        {
//            ianaParams = new SimpleObjectProperty<>(this, ParameterType.IANA_PARAMETER.toString());
//        }
//        return ianaParams;
//    }
//    @Override
//    public ObservableList<IANAParameter> getIana()
//    {
//        return (ianaParams == null) ? null : ianaParams.get();
//    }
//    private ObjectProperty<ObservableList<IANAParameter>> ianaParams;
//    @Override
//    public void setIana(ObservableList<IANAParameter> ianaParams)
//    {
//        if (ianaParams != null)
//        {
//            orderer().registerSortOrderProperty(ianaParams);
//        } else
//        {
//            orderer().unregisterSortOrderProperty(IANAParameter().get());
//        }
//        IANAParameter().set(ianaParams);
//    }
//    /**
//     * Sets the value of the {@link #IANAParameter()} by parsing a vararg of
//     * iCalendar content text representing individual {@link IANAParameter} objects.
//     * 
//     * @return - this class for chaining
//     */
//    public U withIana(String...ianaParams)
//    {
//        List<IANAParameter> a = Arrays.stream(ianaParams)
//                .map(c -> IANAParameter.parse(c))
//                .collect(Collectors.toList());
//        setIana(FXCollections.observableArrayList(a));
//        return (U) this;
//    }
//    /**
//     * Sets the value of the {@link #nonStandardProperty()}
//     * 
//     * @return - this class for chaining
//     */
//    public U withIana(ObservableList<IANAParameter> ianaParams)
//    {
//        setIana(ianaParams);
//        return (U) this;
//    }
//    /**
//     * Sets the value of the {@link #IANAParameter()} from a vararg of {@link IANAParameter} objects.
//     * 
//     * @return - this class for chaining
//     */    
//    public U withIana(IANAParameter...ianaParams)
//    {
//        setIana(FXCollections.observableArrayList(ianaParams));
//        return (U) this;
//    }

//    @Override
//    @Deprecated
//    public Callback<VChild, Void> copyIntoCallback()
//    {        
//        return (child) ->
//        {
//            ParameterType type = ParameterType.enumFromClass(child.getClass());
//            type.copyParameter((Parameter<?>) child, this);
//            return null;
//        };
//    }
    
    @Override
    public void copyInto(VParent destination)
    {
        super.copyInto(destination);
        PropertyBase<T,U> castDestination = (PropertyBase<T,U>) destination;
        castDestination.setConverter(getConverter());
        T valueCopy = copyValue(getValue());
        castDestination.setValue(valueCopy);
        castDestination.setPropertyName(name());
        childrenUnmodifiable().forEach((childSource) -> 
        {
            ParameterType type = ParameterType.enumFromClass(childSource.getClass());
            if (type != null)
            {
                type.copyParameter((Parameter<?>) childSource, (Property<?>) destination);
            } 
        });
    }
    
    // property value as string - kept if string converter changes the value can change
    private String propertyValueString = null;
    // Note: in subclasses additional text can be concatenated to string (e.g. ZonedDateTime classes add time zone as prefix)
    protected String getPropertyValueString() { return propertyValueString; }
    
    
    /**
     * STRING CONVERTER
     * 
     * Get the property's value string converter.  There is a default converter in ValueType associated
     * with the default value type of the property.  For most value types that converter is
     * acceptable.  However, for the TEXT value type it often needs to be replaced.
     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
     * ZoneId.  A different converter is required to make the conversion to ZoneId.
     */ 
    protected StringConverter<T> getConverter()
    {
        if (converter == null)
        {
            ValueType valueType = (getValueType() == null) ? propertyType.allowedValueTypes().get(0) : getValueType().getValue();
            return valueType.getConverter(); // use default converter assigned to value type if no customer converter assigned
        }
        return converter;
    }
    private StringConverter<T> converter;
    protected void setConverter(StringConverter<T> converter) { this.converter = converter; }
    private boolean isCustomConverter()
    {
        return converter != null;
    }
    
    /*
     * CONSTRUCTORS
     */
    
    protected PropertyBase()
    {
        orderer().registerSortOrderProperty(valueTypeProperty());
        propertyType = PropertyType.enumFromClass(getClass());
        // test propertyType.toString()
        if (propertyType != PropertyType.NON_STANDARD)
        {
            setPropertyName(propertyType.toString());
        }
        value = new SimpleObjectProperty<T>(this, propertyType.toString());
        valueTypeProperty().addListener(valueParameterChangeListener); // keeps value synched with value type
        setContentLineGenerator(new SingleLineContent(
                orderer(),
                propertyNameProperty(),
                50));
    }

    public PropertyBase(Class<T> valueClass, String contentLine)
    {
        this();
        this.valueClass = valueClass;
        setConverterByClass(valueClass);
        parseContent(contentLine);
    }

    // copy constructor
    public PropertyBase(PropertyBase<T,U> source)
    {
        this();
        setConverter(source.getConverter());
        source.copyInto(this);
//        copyChildrenFrom(source);
        T valueCopy = copyValue(source.getValue());
        setValue(valueCopy);
        setPropertyName(source.name());
    }
    
    // constructor with only value parameter
    public PropertyBase(T value)
    {
        this();
        setValue(value);
    }

    // return a copy of the value
    protected T copyValue(T source)
    {
        return source; // for mutable values override in subclasses
    }
    
    // Set converter when using constructor with class parameter
    protected void setConverterByClass(Class<T> valueClass)
    {
        // do nothing - hook to override in subclass for functionality
    }
    
    /** Parse content line into calendar property */
    @Override
    public List<String> parseContent(String unfoldedContent)
    {
        // perform tests, make changes if necessary
        final String propertyValue;
        List<Integer> indices = new ArrayList<>();
//        System.out.println("content222:" + contentLine + " " + this.getClass().getSimpleName());
        indices.add(unfoldedContent.indexOf(':'));
        indices.add(unfoldedContent.indexOf(';'));
        Optional<Integer> hasPropertyName = indices
                .stream()
                .filter(v -> v > 0)
                .min(Comparator.naturalOrder());
        if (hasPropertyName.isPresent())
        {
            int endNameIndex = hasPropertyName.get();
            String propertyName = (endNameIndex > 0) ? unfoldedContent.subSequence(0, endNameIndex).toString().toUpperCase() : null;
            boolean isMatch = propertyName.equals(propertyType.toString());
            boolean isNonStandardProperty = propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString());
//            boolean isIANAProperty = propertyType.equals(PropertyType.IANA_PROPERTY);
            if (isMatch || isNonStandardProperty)
            {
                if (isNonStandardProperty)
                {
                    setPropertyName(unfoldedContent.substring(0,endNameIndex));
                }
                propertyValue = unfoldedContent.substring(endNameIndex, unfoldedContent.length()); // strip off property name
            } else
            {
                if (PropertyType.enumFromName(propertyName) == null)
                {
                    propertyValue = ":" + unfoldedContent; // doesn't match a known property name, assume its all a property value
                } else
                {
                    throw new IllegalArgumentException("Property name " + propertyName + " doesn't match class " +
                            getClass().getSimpleName() + ".  Property name associated with class " + 
                            getClass().getSimpleName() + " is " +  propertyType.toString());
                }
            }
        } else
        {
            propertyValue = ":" + unfoldedContent;
        }
        
        // parse parameters
        List<Pair<String, String>> list = ICalendarUtilities.contentToParameterListPair(propertyValue);
        list.stream()
            .forEach(entry ->
            {
                ParameterType parameterType = ParameterType.enumFromName(entry.getKey());
                boolean isAllowed = propertyType().allowedParameters().contains(parameterType);
                if (parameterType != null && isAllowed)
                {
                    Object existingParemeter = parameterType.getParameter(this);
                    if (existingParemeter == null || existingParemeter instanceof List)
                    {
                        parameterType.parse(this, entry.getKey() + "=" + entry.getValue());
                    } else
                    {
                        throw new IllegalArgumentException(parameterType + " can only occur once in a calendar component");
                    }
                } else if (entry.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY)
                {
                 // save property value
                    propertyValueString = entry.getValue();
                    T value = getConverter().fromString(getPropertyValueString());
                    if (value == null)
                    {
                        setUnknownValue(propertyValueString);
                    } else
                    {
                        setValue(value);
                        if (value.toString() == "UNKNOWN") // enum name indicating unknown value
                        {
                            setUnknownValue(propertyValueString);
                        }
                    }
                }
                // else if ((entry.getKey() != null) && (entry.getValue() != null))
//                { // unknown parameter - store as String in other parameter
//                    if ((IANAParameter.getRegisteredIANAParameters() != null) && IANAParameter.getRegisteredIANAParameters().contains(entry.getKey()))
//                    {
//                        ParameterType.IANA_PARAMETER.parse(this, entry.getKey() + "=" + entry.getValue());
//                    } else
//                    {
//                        // ignore unrecognized parameter (RFC 5545, 3.2 Property Parameters, page 14)
//                    }
//                } // if parameter doesn't contain both a key and a value it is ignored
            });
        
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + propertyType().toString() + " content line:" + System.lineSeparator()
                    + errors().stream().collect(Collectors.joining(System.lineSeparator())));
        }
        return errors();
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getValue() == null)
        {
            errors.add(name() + " value is null.  The property MUST have a value."); 
        }
        final ValueType valueType;
        if (getValueType() != null)
        {
            valueType = getValueType().getValue();
            boolean isValueTypeOK = isValueTypeValid(valueType);
            if (! isValueTypeOK)
            {
                errors.add(name() + " value type " + getValueType().getValue() + " is not supported.  Supported types include:" +
                        propertyType().allowedValueTypes().stream().map(v -> v.toString()).collect(Collectors.joining(",")));
            }
        } else
        {
            // use default valueType
            valueType = propertyType().allowedValueTypes().get(0);
        }
        List<String> createErrorList = valueType.createErrorList(getValue());
        if (createErrorList != null)
        {
            errors.addAll(createErrorList);
        }
        orderer().elementSortOrderMap().forEach((key, value) -> errors.addAll(key.errors()));
        return errors;
    }
    
    /* test if value type is valid */
    private boolean isValueTypeValid(ValueType value)
    {
        boolean isValueTypeOK = propertyType().allowedValueTypes().contains(value);
        boolean isUnknownType = value.equals(ValueType.UNKNOWN);
        boolean isNonStandardProperty = propertyType().equals(PropertyType.NON_STANDARD);
        return (isValueTypeOK || isUnknownType || isNonStandardProperty);
    }

    @Override
    public String toContent()
    {
        StringBuilder builder = new StringBuilder(super.toContent());
        builder.append(":" + valueContent());
        // return folded line
        return ICalendarUtilities.foldLine(builder).toString();
    }
    
    @Override
    public String toString()
    {
        return super.toString() + "," + toContent();
    }
    
    @Override // Note: can't check equality of parents - causes stack overflow
    public boolean equals(Object obj)
    {
        boolean childrenEquals = super.equals(obj);
        if (! childrenEquals) return false;
        PropertyBase<?,?> testObj = (PropertyBase<?,?>) obj;
        boolean valueEquals = (getValue() == null) ? testObj.getValue() == null : getValue().equals(testObj.getValue());
        if (! valueEquals) return false;
        boolean nameEquals = name().equals(testObj.name());
        return nameEquals;
    }

    @Override // Note: can't check hashCode of parents - causes stack overflow
    public int hashCode()
    {
        int hash = super.hashCode();
        final int prime = 31;
        hash = prime * hash + ((converter == null) ? 0 : converter.hashCode());
        hash = prime * hash + ((propertyName == null) ? 0 : propertyName.hashCode());
        hash = prime * hash + ((propertyValueString == null) ? 0 : propertyValueString.hashCode());
        hash = prime * hash + ((unknownValue == null) ? 0 : unknownValue.hashCode());
        hash = prime * hash + ((value == null) ? 0 : value.hashCode());
        return hash;
    }

    @Override
    public int compareTo(Property<T> otherProperty)
    {
        return Integer.compare(propertyType().ordinal(), otherProperty.propertyType().ordinal());
    }
}
