package jfxtras.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Interface for {@link DateTimeEnd} property
 * 
 * @author David Bal
 *
 * @param <T> concrete subclass
 */
public interface VDateTimeEnd<T> extends VComponent
{
    /**
     *<p>This property specifies the date and time that a calendar
     * component ends.</p>
     * 
     *  *<p>Example:  The following is an example of this property:
     *<ul>
     *<li>DTEND:19960401T150000Z
     *<li>DTEND;VALUE=DATE:19980704
     *</ul>
     */
    ObjectProperty<DateTimeEnd> dateTimeEndProperty();
    DateTimeEnd getDateTimeEnd();
    default void setDateTimeEnd(String dtEnd)
    {
        if (getDateTimeEnd() == null)
        {
            setDateTimeEnd(DateTimeEnd.parse(dtEnd));
        } else
        {
            DateTimeEnd temp = DateTimeEnd.parse(dtEnd);
            if (temp.getValue().getClass().equals(getDateTimeEnd().getValue().getClass()))
            {
                getDateTimeEnd().setValue(temp.getValue());
            } else
            {
                setDateTimeEnd(temp);
            }
        }
    }
    default void setDateTimeEnd(DateTimeEnd dtEnd) { dateTimeEndProperty().set(dtEnd); }
    default void setDateTimeEnd(Temporal temporal)
    {
        if ((getDateTimeEnd() == null) || ! getDateTimeEnd().getValue().getClass().equals(temporal.getClass()))
        {
            if (temporal instanceof LocalDate)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));            
            } else if (temporal instanceof LocalDateTime)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));            
            } else if (temporal instanceof ZonedDateTime)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));            
            } else
            {
                throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                        + temporal.getClass().getSimpleName() + " is not supported");
            }
        } else
        {
            getDateTimeEnd().setValue(temporal);
        }
    }
    /**
     * Sets the value of {@link #DateTimeEnd()}.
     * 
     * @return - this class for chaining
     */    default T withDateTimeEnd(Temporal dtEnd)
    {
        setDateTimeEnd(dtEnd);
        return (T) this;
    }
    /**
     * Sets the value of {@link #DateTimeEnd()} by parsing iCalendar text.
     * 
     * @return - this class for chaining
     */
    default T withDateTimeEnd(String dtEnd)
    {
        setDateTimeEnd(dtEnd);
        return (T) this;
    }
    /**
     * Sets the value of {@link #DateTimeEnd()}.
     * 
     * @return - this class for chaining
     */
    default T withDateTimeEnd(DateTimeEnd dtEnd)
    {
        setDateTimeEnd(dtEnd);
        return (T) this;
    }
    
    // From VComponentPrimary
    DateTimeStart getDateTimeStart();
    
    /** Ensures DateTimeEnd has same date-time type as DateTimeStart.  Should be called by listener
     *  after dateTimeEndProperty() is initialized.  Intended for internal use only. */
    default void checkDateTimeEndConsistency()
    {
        if ((getDateTimeEnd() != null) && (getDateTimeStart() != null))
        {
            DateTimeType dateTimeEndType = DateTimeUtilities.DateTimeType.of(getDateTimeEnd().getValue());
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            if (dateTimeEndType != dateTimeStartType)
            {
                throw new DateTimeException("DateTimeEnd DateTimeType (" + dateTimeEndType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }
        }
    }
    
    /**
     * Creates error string if {@link DateTimeEnd} value has an error, null otherwise.
     * 
     * @param testObj  {@link VDateTimeEnd} to be tested.
     * @return  Error string or null if no error.
     */
    static String errorsDateTimeEnd(VDateTimeEnd<?> testObj)
    {
        if (testObj.getDateTimeEnd() != null)
        {
            if (testObj.getDateTimeStart() != null)
            {
                DateTimeType startType = DateTimeUtilities.DateTimeType.of(testObj.getDateTimeStart().getValue());
                DateTimeType endType = DateTimeUtilities.DateTimeType.of(testObj.getDateTimeEnd().getValue());
                boolean isDateTimeEndMatch = startType == endType;
                if (! isDateTimeEndMatch)
                {
                     return "The value type of DTEND MUST be the same as the DTSTART property (" + endType + ", " + startType + ")";
                }
            }
        }
        return null;
    }
}
