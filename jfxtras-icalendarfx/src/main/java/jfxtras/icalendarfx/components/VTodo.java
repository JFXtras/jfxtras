package jfxtras.icalendarfx.components;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.descriptive.PercentComplete;
import jfxtras.icalendarfx.properties.component.time.DateTimeCompleted;
import jfxtras.icalendarfx.properties.component.time.DateTimeDue;
import jfxtras.icalendarfx.properties.component.time.DurationProp;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * VTODO
 * To-Do Component
 * RFC 5545, 3.6.2, page 55
 * 
 * A "VTODO" calendar component is a grouping of component
      properties and possibly "VALARM" calendar components that
      represent an action-item or assignment.  For example, it can be

      used to represent an item of work assigned to an individual; such
      as "turn in travel expense today".

      The "VTODO" calendar component cannot be nested within another
      calendar component.  However, "VTODO" calendar components can be
      related to each other or to a "VEVENT" or to a "VJOURNAL" calendar
      component with the "RELATED-TO" property.

      A "VTODO" calendar component without the "DTSTART" and "DUE" (or
      "DURATION") properties specifies a to-do that will be associated
      with each successive calendar date, until it is completed.
      
      Examples:  The following is an example of a "VTODO" calendar
      component that needs to be completed before May 1st, 2007.  On
      midnight May 1st, 2007 this to-do would be considered overdue.

       BEGIN:VTODO
       UID:20070313T123432Z-456553@example.com
       DTSTAMP:20070313T123432Z
       DUE;VALUE=DATE:20070501
       SUMMARY:Submit Quebec Income Tax Return for 2006
       CLASS:CONFIDENTIAL
       CATEGORIES:FAMILY,FINANCE
       STATUS:NEEDS-ACTION
       END:VTODO
 * 
 * @author David Bal
 *
 */
public class VTodo extends VLocatable<VTodo> implements VDescribable2<VTodo>
{    
    /**
     * COMPLETED: Date-Time Completed
     * RFC 5545 iCalendar 3.8.2.1 page 94
     * This property defines the date and time that a to-do was
     * actually completed.
     * The value MUST be specified in the UTC time format.
     * 
     * Example:
     * COMPLETED:19960401T150000Z
     */
    public ObjectProperty<DateTimeCompleted> dateTimeCompletedProperty()
    {
        if (dateTimeCompleted == null)
        {
            dateTimeCompleted = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_COMPLETED.toString());
            orderer().registerSortOrderProperty(dateTimeCompleted);
        }
        return dateTimeCompleted;
    }
    private ObjectProperty<DateTimeCompleted> dateTimeCompleted;
    public DateTimeCompleted getDateTimeCompleted() { return dateTimeCompletedProperty().get(); }
    public void setDateTimeCompleted(String completed) { setDateTimeCompleted(DateTimeCompleted.parse(completed)); }
    public void setDateTimeCompleted(DateTimeCompleted completed) { dateTimeCompletedProperty().set(completed); }
    public void setDateTimeCompleted(ZonedDateTime completed) { setDateTimeCompleted(new DateTimeCompleted(completed)); }
    public VTodo withDateTimeCompleted(ZonedDateTime completed) { setDateTimeCompleted(completed); return  this; }
    public VTodo withDateTimeCompleted(String completed) { setDateTimeCompleted(completed); return this; }
    public VTodo withDateTimeCompleted(DateTimeCompleted completed) { setDateTimeCompleted(completed); return this; }
    
    /**
     * DUE: Date-Time Due
     * RFC 5545 iCalendar 3.8.2.3 page 96
     * This property defines the date and time that a to-do is
     * expected to be completed.
     * the value type of this property MUST be the same as the "DTSTART" property
     * 
     * Example:
     * DUE:TZID=America/Los_Angeles:19970512T090000
     */
    public ObjectProperty<DateTimeDue> dateTimeDueProperty()
    {
        if (dateTimeDue == null)
        {
            dateTimeDue = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_DUE.toString());
            orderer().registerSortOrderProperty(dateTimeDue);
            dateTimeDue.addListener((observable, oldValue, newValue) -> 
            {
                if ((getDateTimeDue() != null) && (getDuration() != null))
                {
                    throw new DateTimeException("DURATION and DUE can't both be set");
                }                
            });

        }
        return dateTimeDue;
    }
    private ObjectProperty<DateTimeDue> dateTimeDue;
    public DateTimeDue getDateTimeDue() { return dateTimeDueProperty().get(); }
    public void setDateTimeDue(String due) { setDateTimeDue(DateTimeUtilities.temporalFromString(due)); }
    public void setDateTimeDue(DateTimeDue due) { dateTimeDueProperty().set(due); }
    public void setDateTimeDue(Temporal due)
    {
        if (due instanceof LocalDate)
        {
            setDateTimeDue(new DateTimeDue(due));            
        } else if (due instanceof LocalDateTime)
        {
            setDateTimeDue(new DateTimeDue(due));            
        } else if (due instanceof ZonedDateTime)
        {
            setDateTimeDue(new DateTimeDue(due));            
        } else
        {
            throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                    + due.getClass().getSimpleName() + " is not supported");
        }
    }
    public VTodo withDateTimeDue(Temporal due) { setDateTimeDue(due); return this; }
    public VTodo withDateTimeDue(String due) { setDateTimeDue(due); return this; }
    public VTodo withDateTimeDue(DateTimeDue due) { setDateTimeDue(due); return this; }

    /** Ensures DateTimeDue and Duration are not both used. */
    @Override public ObjectProperty<DurationProp> durationProperty()
    {
        ObjectProperty<DurationProp> duration = super.durationProperty();
        duration.addListener((obs) ->
        {
            if ((getDateTimeDue() != null) && (getDuration() != null))
            {
                throw new DateTimeException("DURATION and DUE can't both be set");
            }            
        });
        return duration;
    }
    
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
     */
    public ObjectProperty<PercentComplete> percentCompleteProperty()
    {
        if (percentComplete == null)
        {
            percentComplete = new SimpleObjectProperty<>(this, PropertyType.PERCENT_COMPLETE.toString());
            orderer().registerSortOrderProperty(percentComplete);
        }
        return percentComplete;
    }
    private ObjectProperty<PercentComplete> percentComplete;
    public PercentComplete getPercentComplete() { return percentCompleteProperty().get(); }
    public void setPercentComplete(String percentComplete) { setPercentComplete(PercentComplete.parse(percentComplete)); }
    public void setPercentComplete(Integer percentComplete) { setPercentComplete(new PercentComplete(percentComplete)); }
    public void setPercentComplete(PercentComplete percentComplete) { percentCompleteProperty().set(percentComplete); }
    public VTodo withPercentComplete(PercentComplete percentComplete) { setPercentComplete(percentComplete); return this; }
    public VTodo withPercentComplete(Integer percentComplete) { setPercentComplete(percentComplete); return this; }
    public VTodo withPercentComplete(String percentComplete) { PropertyType.PERCENT_COMPLETE.parse(this, percentComplete); return this; }
    
    /*
     * CONSTRUCTORS
     */
    public VTodo() { super(); }
    
//    public VTodo(String contentLines)
//    {
//        super(contentLines);
//    }
    
    public VTodo(VTodo source)
    {
        super(source);
    }
    
    @Override
    public TemporalAmount getActualDuration()
    {
        final TemporalAmount duration;
        if (getDuration() != null)
        {
            duration = getDuration().getValue();
        } else if (getDateTimeDue() != null)
        {
            Temporal dtstart = getDateTimeStart().getValue();
            Temporal dtdue = getDateTimeDue().getValue();
            duration = DateTimeUtilities.temporalAmountBetween(dtstart, dtdue);
        } else
        {
            return Duration.ZERO;
        }
        return duration;
    }
    
    @Override
    public void setEndOrDuration(Temporal startRecurrence, Temporal endRecurrence)
    {
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(startRecurrence, endRecurrence);
        if (getDuration() != null)
        {
            setDuration(duration);
        } else if (getDateTimeDue() != null)
        {
            Temporal due = getDateTimeStart().getValue().plus(duration);
            setDateTimeDue(due);
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getDateTimeStart() == null)
        {
            errors.add("DTSTART is REQUIRED and MUST NOT occur more than once");
        }
        boolean isDateTimeDuePresent = getDateTimeDue() != null;
        boolean isDurationPresent = getDuration() != null;
        
        if (getDateTimeDue() != null)
        {
            if (getDateTimeStart() != null)
            {
                DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
                DateTimeType dueType = DateTimeUtilities.DateTimeType.of(getDateTimeDue().getValue());
                boolean isDateTimeDueMatch = startType == dueType;
                if (! isDateTimeDueMatch)
                {
                    errors.add("The value type of DUE MUST be the same as the DTSTART property (" + dueType + ", " + startType);
                }
            }
        }
        
        if ((! isDateTimeDuePresent) && (! isDurationPresent))
        {
//            errors.add("Neither DUE or DURATION is present.  DUE or DURATION is REQUIRED and MUST NOT occur more than once");
        } else if (isDateTimeDuePresent && isDurationPresent)
        {
            errors.add("Both DUE and DURATION are present.  DUE or DURATION MAY appear, but both MUST NOT occur in the same " + name());
        }  else if (isDateTimeDuePresent)
        {
            if (! DateTimeUtilities.isAfter(getDateTimeDue().getValue(), getDateTimeStart().getValue()))
            {
                errors.add("DUE is not after DTSTART.  DUE MUST be after DTSTART");                
            }
        } else // duration is present
        {
            Temporal actualEnd = getDateTimeStart().getValue().plus(getDuration().getValue());
            if (! DateTimeUtilities.isAfter(actualEnd, getDateTimeStart().getValue()))
            {
                errors.add("DURATION is negative.  DURATION MUST be positive");                
            }            
        }
            
        return Collections.unmodifiableList(errors);
    }
    
    @Override
    public void eraseDateTimeProperties()
    {
        super.eraseDateTimeProperties();
        setDateTimeDue((DateTimeDue) null);
    }
    
    /** Parse content lines into calendar component object */
    public static VTodo parse(String contentLines)
    {
        VTodo component = new VTodo();
        component.parseContent(contentLines);
        return component;
    }
}
