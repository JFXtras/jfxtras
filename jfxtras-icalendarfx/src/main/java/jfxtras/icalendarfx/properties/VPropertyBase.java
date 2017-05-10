package jfxtras.icalendarfx.properties;

import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.VParentBase;
import jfxtras.icalendarfx.content.SingleLineContent;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.parameters.VParameter;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.StringConverter;

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
public abstract class VPropertyBase<T,U> extends VParentBase<U> implements VProperty<T>
{
    private VParent myParent;
    @Override
    public void setParent(VParent parent)
    {
    	myParent = parent;
	}
    @Override
    public VParent getParent()
    {
    	return myParent;
	}
    
    /**
     * PROPERTY VALUE
     * 
     * Example: for the property content LOCATION:The park the property
     * value is the string "The park"
     */
    @Override
    public T getValue()
    {
    	return value;
	}
    private T value; // initialized in constructor
    @Override
    public void setValue(T value)
    {
        this.value = value;
    }
    public U withValue(T value)
    {
    	setValue(value);
    	return (U) this;
	} // in constructor

    /** The propery's value converted by string converted to content string */
    protected String valueContent()
    {
        /* default code below works for all properties with a single value.  Properties with multiple embedded values,
         * such as RequestStatus, require an overridden method */
    	String value = null;
    	if (getValue() == null)
		{
    		value = getUnknownValue();
		} else if (getConverter().toString(getValue()) == null)
		{
			value = getUnknownValue();
		} else
		{
	        value = getConverter().toString(getValue());			
		}
        return (value == null) ? "" : value;
    }

    // class of value.  If collection, returns type of element instead. 
    // Used to verify value class is allowed for the property type
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
    	if (propertyName == null)
    	{
    		return propertyType.toString();
    	}
        return propertyName;
    }
    protected String propertyName;
    
//    /**
//     * PROPERTY TYPE
//     * 
//     *  The enumerated type of the property.
//     *  Some essential methods are in the enumerated type.
//     */
//    public PropertyType propertyType()
//    {
//    	return propertyType;
//	}
    final private VPropertyElement propertyType;
    
    /*
     * Unknown values
     * contains exact string for unknown property value
     */
    private String unknownValue;
    protected String getUnknownValue()
    {
    	return unknownValue;
    }
    private void setUnknownValue(String value)
    {
    	unknownValue = value;
	}
    
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
    final protected ValueType defaultValueType;
    final protected Collection<ValueType> allowedValueTypes;
    
    @Override
    public ValueParameter getValueType() { return valueType; }
    private ValueParameter valueType;
    @Override
    public void setValueType(ValueParameter valueType)
    {
        if (valueType == null || isValueTypeValid(valueType.getValue()))
        {
        	orderChild(this.valueType, valueType);
            this.valueType = valueType;
            valueParamenterConverter(valueType); // convert new value
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + valueType.getValue() + ", allowed = " + allowedValueTypes);
        }
    }
    public void setValueType(ValueType value)
    {
        setValueType(new ValueParameter(value));
    }
    public U withValueType(ValueType value)
    {
        setValueType(value);
        return (U) this;
    } 

    // Synch value with type produced by string converter
    private void valueParamenterConverter(ValueParameter newValueParameter)
    {
        if (! isCustomConverter())
        {
            // Convert property value string, if present
            if (modifiedValue() != null)
            {
                String modifiedValue = modifiedValue();
				T newPropValue = getConverter().fromString(modifiedValue);
                this.value = newPropValue;
            }
        }
        
        // verify value class is allowed
        if (newValueParameter != null && getValueClass() != null) // && ! newValue.getValue().allowedClasses().contains(getValueClass()))
        {
            boolean isMatch = newValueParameter.getValue().allowedClasses()
                    .stream()
                    .map(c -> getValueClass().isAssignableFrom(c))
                    .findAny()
                    .isPresent();
            if (! isMatch)
            {
                throw new IllegalArgumentException("Value class " + getValueClass().getSimpleName() +
                        " doesn't match allowed value classes: " + newValueParameter.getValue().allowedClasses());
            }
        }    	
    }
    
    /**
     * <h2>NON-STANDARD PARAMETERS</h2>
     * 
     * <p>x-param     = x-name "=" param-value *("," param-value)<br>
     ; A non-standard, experimental parameter.</p>
     */
    private List<NonStandardParameter> nonStandardParams;
    @Override
    public List<NonStandardParameter> getNonStandard()
    {
        return nonStandardParams;
    }
    @Override
    public void setNonStandard(List<NonStandardParameter> nonStandardParams)
    {
    	if (this.nonStandardParams != null)
    	{
    		this.nonStandardParams.forEach(e -> orderChild(e, null)); // remove old elements
    	}
        this.nonStandardParams = nonStandardParams;
    	if (nonStandardParams != null)
    	{
        	nonStandardParams.forEach(e -> orderChild(e));
    	}
    }
    /**
     * Sets the value of the {@link #NonStandardParameter()}
     * 
     * @return - this class for chaining
     */
    public U withNonStandard(List<NonStandardParameter> nonStandardParams)
    {
    	if (getNonStandard() == null)
    	{
        	setNonStandard(new ArrayList<>());
    	}
    	getNonStandard().addAll(nonStandardParams);
    	if (nonStandardParams != null)
    	{
    		nonStandardParams.forEach(c -> orderChild(c));
    	}
        return (U) this;
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
        List<NonStandardParameter> list = Arrays.stream(nonStandardParams)
                .map(c -> NonStandardParameter.parse(c))
                .collect(Collectors.toList());
        return withNonStandard(list);
    }
    /**
     * Sets the value of the {@link #NonStandardParameter()} from a vararg of {@link NonStandardParameter} objects.
     * 
     * @return - this class for chaining
     */    
    public U withNonStandard(NonStandardParameter...nonStandardParams)
    {
    	return withNonStandard(Arrays.asList(nonStandardParams));
    }
    
    // property value as string - kept if string converter changes the value can change
    // needed to make subsequent conversions if value type changes.
    protected String actualValueContent = null;
    // Note: in subclasses additional text can be concatenated to string (e.g. ZonedDateTime classes add time zone as prefix)
    protected String modifiedValue()
    {
    	if (actualValueContent == null)
    	{
    		T value2 = getValue();
			if (value2 != null)
    		{
    			return getConverter().toString(getValue());
    		}
    		return null;
    	} else
    	{
			return actualValueContent;
    	}
	}
    
    
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
            ValueType valueType = (getValueType() == null) ? defaultValueType : getValueType().getValue();
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
    
//    protected VPropertyBase(Collection<ValueType> allowedValueTypes, ValueType defaultValueType)
    protected VPropertyBase()
    {
    	super();
    	propertyType = VPropertyElement.fromClass(getClass());
    	this.allowedValueTypes = propertyType.allowedValueTypes();
    	this.defaultValueType = propertyType.defaultValueType();
//    	this.allowedValueTypes = VPropertyElement.propertyAllowedValueTypes(getClass());
//    	this.defaultValueType = VPropertyElement.defaultValueType(getClass());
//    	this.defaultValueType = defaultValueType;
//        propertyType = PropertyType.enumFromClass(getClass());
//        if (propertyType != PropertyType.NON_STANDARD)
//        {
//            setPropertyName(propertyType.toString());
//        }
        contentLineGenerator  = new SingleLineContent(
                orderer,
                (Void) -> name(),
                50);
    }

    // Used by Attachment
    public VPropertyBase(Class<T> valueClass, String contentLine)
    {
        this();
        this.valueClass = valueClass;
        setConverterByClass(valueClass);
        parseContent(contentLine);
    }

    // copy constructor
    public VPropertyBase(VPropertyBase<T,U> source)
    {
    	this();
        setConverter(source.getConverter());
        T valueCopy = copyValue(source.getValue());
        setValue(valueCopy);
        source.copyChildrenInto(this);
        setParent(source.getParent());
    }
    
    // constructor with only value
    public VPropertyBase(T value)
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
    
    /**
     * Handle non-standard property name
     */
    @Override
	protected List<Message> parseContent(String unfoldedContent)
    {
    	List<Message> messages = new ArrayList<>();
    	String propertyName = elementName(unfoldedContent);
    	boolean isNameless = propertyName == null;
    	if (isNameless)
    	{
    		unfoldedContent = ICalendarUtilities.PROPERTY_VALUE_KEY + unfoldedContent; // add delimiter to designate content as value
    	} else if (propertyName.startsWith(VPropertyElement.NON_STANDARD_PROPERTY.toString()))
        {
            ((NonStandardProperty) this).setPropertyName(propertyName);
        }
    	ICalendarUtilities.parseInlineElementsToListPair(unfoldedContent)
	        .stream()
//	        .peek(System.out::println)
	        .forEach(entry -> processInLineChild(messages, entry.getKey(), entry.getValue(), VParameter.class));

    	return messages;
    }
    
	@Override
	protected void processInLineChild(
			List<Message> messages, 
			String childName, 
			String content,
			Class<? extends VElement> singleLineChildClass)
	{
    	if (childName == ICalendarUtilities.PROPERTY_VALUE_KEY)
    	{
    		if (content != null)
    		{
	            try {
	            	actualValueContent = content;
	            	T value = getConverter().fromString(modifiedValue());
	                if (value == null)
	                {
	                    setUnknownValue(content);
	                } else
	                {
	                    setValue(value);
	                    if (value.toString() == "UNKNOWN") // enum name indicating unknown value
	                    {
	                        setUnknownValue(content);
	                    }
	                }
	            } catch (IllegalArgumentException | DateTimeException e)
	            {
	    			Message message = new Message(this,
	    					"Invalid element:" + e.getMessage() + modifiedValue(),
	    					MessageEffect.MESSAGE_ONLY);
	    			messages.add(message);
	            }
    		}
    	} else
    	{
    		super.processInLineChild(messages, childName, content, singleLineChildClass);
    	}
	}
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getValue() == null)
        {
//            errors.add(name() + " value is null.  The property MUST have a value.");  // Google uses empty properties
        }
        
        final ValueType valueType;
        if (getValueType() != null)
        {
            valueType = getValueType().getValue();
            boolean isValueTypeOK = isValueTypeValid(valueType);
            if (! isValueTypeOK)
            {
                errors.add(name() + " value type " + getValueType().getValue() + " is not supported.  Supported types include:" +
                        allowedValueTypes.stream().map(v -> v.toString()).collect(Collectors.joining(",")));
            }
        } else
        {
            // use default valueType
            valueType = defaultValueType;
        }
        
        List<String> valueTypeErrorList = valueType.createErrorList(getValue());
        if (valueTypeErrorList != null)
        {
            errors.addAll(valueTypeErrorList);
        }
        return errors;
    }
    
    /* test if value type is valid */
    private boolean isValueTypeValid(ValueType value)
    {
        boolean isValueTypeOK = allowedValueTypes.contains(value);
        boolean isUnknownType = value.equals(ValueType.UNKNOWN);
//        boolean isNonStandardProperty = propertyType().equals(PropertyType.NON_STANDARD);
        return (isValueTypeOK || isUnknownType);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(":" + valueContent());
        // return folded line
        return ICalendarUtilities.foldLine(builder).toString();
    }
    
    @Override // Note: can't check equality of parents - causes stack overflow
    public boolean equals(Object obj)
    {
        boolean childrenEquals = super.equals(obj);
        if (! childrenEquals) return false;
        VPropertyBase<?,?> testObj = (VPropertyBase<?,?>) obj;
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
        hash = prime * hash + ((actualValueContent == null) ? 0 : actualValueContent.hashCode());
        hash = prime * hash + ((unknownValue == null) ? 0 : unknownValue.hashCode());
        hash = prime * hash + ((value == null) ? 0 : value.hashCode());
        return hash;
    }
}
