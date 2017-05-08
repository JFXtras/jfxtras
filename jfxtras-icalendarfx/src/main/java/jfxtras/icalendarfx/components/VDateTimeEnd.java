package jfxtras.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDateTimeEnd;
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
    DateTimeEnd getDateTimeEnd();
    void setDateTimeEnd(DateTimeEnd dtEnd);
    default void setDateTimeEnd(String dtEnd) { setDateTimeEnd(DateTimeEnd.parse(dtEnd)); }
    default void setDateTimeEnd(Temporal temporal)
    {
        if ((getDateTimeEnd() == null) || ! getDateTimeEnd().getValue().getClass().equals(temporal.getClass()))
        {
            boolean t1 = temporal instanceof LocalDate;
            boolean t2 = temporal instanceof LocalDateTime;
            boolean t3 = temporal instanceof ZonedDateTime;
            if (t1 || t2 || t3)
            {
                setDateTimeEnd(new DateTimeEnd(temporal));
            } else
            {
                throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                        + temporal.getClass().getSimpleName() + " is not supported");
            }
        } else
        {
            setDateTimeEnd(new DateTimeEnd(temporal));
        }
    }
    /**
     * Sets the value of {@link #DateTimeEnd()}.
     * 
     * @return - this class for chaining
     */    
    default T withDateTimeEnd(Temporal dtEnd)
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
     *  after dateTimeEndProperty() is initialized.  Intended for internal use only. 
     * @return */
    default void checkDateTimeEndConsistency()
    {
        List<String> errors = errorsDateTimeEnd(this);
        if (! errors.isEmpty())
        {
            String message = errors.stream().collect(Collectors.joining(System.lineSeparator()));
            throw new DateTimeException(message);
        }
//        if ((getDateTimeEnd() != null) && (getDateTimeStart() != null))
//        {
//            DateTimeType dateTimeEndType = DateTimeUtilities.DateTimeType.of(getDateTimeEnd().getValue());
//            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
//            if (dateTimeEndType != dateTimeStartType)
//            {
//                throw new DateTimeException("DTEND value type (" + dateTimeEndType +
//                        ") must be same as the value type of DTSTART (" + dateTimeStartType + ")");
//            }
//            int after = DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(getDateTimeEnd().getValue(), getDateTimeStart().getValue());
//            if (after == -1)
//            {
//                throw new DateTimeException("DTEND is not after DTSTART.  DTEND MUST be after DTSTART ("
//                        + getDateTimeEnd().getValue() + "," + getDateTimeStart().getValue() + ")");
//            }
//        }
    }
    
    /**
     * Creates error string if {@link DateTimeEnd} value has an error, null otherwise.
     * 
     * @param testObj  {@link VDateTimeEnd} to be tested.
     * @return  Error string or null if no error.
     */
    static List<String> errorsDateTimeEnd(VDateTimeEnd<?> testObj)
    {
        List<String> errors = new ArrayList<>();
        if (testObj.getDateTimeEnd() != null)
        {
            if (testObj.getDateTimeStart() != null)
            {
                DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(testObj.getDateTimeStart().getValue());
                DateTimeType dateTimeEndType = DateTimeUtilities.DateTimeType.of(testObj.getDateTimeEnd().getValue());
                boolean isDateTimeEndMatch = dateTimeStartType == dateTimeEndType;
                if (! isDateTimeEndMatch)
                {
                    errors.add("DTEND value type (" + dateTimeEndType +
                    ") must be the same value type as DTSTART (" + dateTimeStartType + ")");
                }
                int after = DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(testObj.getDateTimeEnd().getValue(), testObj.getDateTimeStart().getValue());
                if (after == -1)
                {
                    errors.add("DTEND does not occur after DTSTART.  DTEND MUST occur after DTSTART ("
                            + testObj.getDateTimeEnd().getValue() + ", " + testObj.getDateTimeStart().getValue() + ")");
                }
            }
        }
        return errors;
    }
}
