package jfxtras.icalendarfx.properties;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import jfxtras.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.icalendarfx.parameters.VParameterElement;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.PropBaseDateTime;
import jfxtras.icalendarfx.properties.PropDateTime;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Abstract class for all non-UTC date-time classes
 * Contains the time zone identifier parameter
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DateTimeStart
 * @see DateTimeEnd
 * @see RecurrenceId
 */
public abstract class PropBaseDateTime<T,U> extends VPropertyBase<T,U> implements PropDateTime<T>
{
    // reference to property value if T is not instance of Collection, or an element if T is a Collection
    private Object myElement;
    
    /**
     * TZID
     * Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * 
     * Examples:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    @Override
    public TimeZoneIdentifierParameter getTimeZoneIdentifier() { return timeZoneIdentifier; }
    private TimeZoneIdentifierParameter timeZoneIdentifier;
    @Override
    public void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        if ((myElement == null) || (myElement instanceof ZonedDateTime))
        {
        	orderChild(this.timeZoneIdentifier, timeZoneIdentifier);
            this.timeZoneIdentifier = timeZoneIdentifier;
        } else
        {
            throw new DateTimeException(VParameterElement.TIME_ZONE_IDENTIFIER.name() + " can't be set for date-time of type " + getValue().getClass().getSimpleName());
        }
    }
    public void setTimeZoneIdentifier(String value)
    {
    	setTimeZoneIdentifier(TimeZoneIdentifierParameter.parse(value));
	}
    public void setTimeZoneIdentifier(ZoneId zone)
    {
    	setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone));
	}
    public U withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
    	setTimeZoneIdentifier(timeZoneIdentifier);
    	return (U) this;
	}
    public U withTimeZoneIdentifier(ZoneId zone)
    {
    	setTimeZoneIdentifier(zone);
    	return (U) this;
	}
    public U withTimeZoneIdentifier(String content)
    {
    	setTimeZoneIdentifier(content);
    	return (U) this;
	}        
    
    /*
     * CONSTRUCTORS
     */
    protected PropBaseDateTime()
    {
        super();
    }
    
    public PropBaseDateTime(T temporal)
    {
        super(temporal);
    }
    
    public PropBaseDateTime(PropBaseDateTime<T,U> source)
    {
        super(source);
    }
    
    /**
     * append time zone to front of time for parsing in DATE_ZONED_DATE_TIME parse method
     * @see ValueType
     */
    @Override
	protected String modifiedValue()
    {
        String timeZone = (getTimeZoneIdentifier() != null) ? "[" + getTimeZoneIdentifier().getValue().toString() + "]" : "";
    	String modifiedValue = super.modifiedValue();
		return (modifiedValue != null) ? timeZone + modifiedValue : null;
	}

    @Override
    public void setValue(T value)
    {
        super.setValue(value);
        if (value instanceof Collection)
        {
            Collection<?> collection = (Collection<?>) value;
            myElement = (collection.isEmpty()) ? null : collection.iterator().next();
        } else if (value instanceof Temporal)
        {
            myElement = value;
        } else
        {
            throw new DateTimeException("Unsupported type:" + value.getClass().getSimpleName());            
        }

        if (myElement != null)
        {
            if (myElement instanceof ZonedDateTime)
            {
                ZoneId zone = ((ZonedDateTime) myElement).getZone();
                if (! zone.equals(ZoneId.of("Z")))
                {
                    if (getValueType() != null && getValueType().getValue() == ValueType.DATE)
                	{
                    	setValueType((ValueParameter) null); // reset value type if previously set to DATE
                	}
                    if (getTimeZoneIdentifier() == null)
                    {
                    	setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone));
                    }
                }
            } else if ((myElement instanceof LocalDateTime) || (myElement instanceof LocalDate))
            {
                if (getTimeZoneIdentifier() != null)
                {
                    throw new DateTimeException("Only ZonedDateTime is permitted when specifying a Time Zone Identifier (" + myElement.getClass().getSimpleName() + ")");                            
                }
                if (getValueType() == null)
                {
	                if (myElement instanceof LocalDate)
	                {
	                    setValueType(ValueType.DATE); // must set value parameter to force output of VALUE=DATE
	                } else
	                {
	                    if (getValueType() != null && getValueType().getValue() == ValueType.DATE) setValueType((ValueParameter) null); // reset value type if previously set to DATE
	                }
                }
            } else
            {
                throw new DateTimeException("Unsupported Temporal type:" + value.getClass().getSimpleName());            
            }
        }
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getValue() != null && getValueType() != null)
        {
            if (getValue() instanceof LocalDate)
            {
                if (getValueType().getValue() != ValueType.DATE)
                {
                    errors.add(name() + "'s value (" + getValue() + ") doesn't match its value type (" + getValueType().getValue()
                            + ").  For that value, the required value type is " + ValueType.DATE);
                }
            } else if (getValue() instanceof LocalDateTime || getValue() instanceof ZonedDateTime)
            {
                if (getValueType().getValue() != ValueType.DATE_TIME)
                {
                    errors.add(name() + "'s value (" + getValue() + ") doesn't match its value type (" + getValueType().getValue()
                            + ").  For that value, the required value type is " + ValueType.DATE_TIME);
                }                
            }
        }
        return errors;
    }

}
