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

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VDescribable2;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.components.VTodo;
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
    private DateTimeCompleted dateTimeCompleted;
    public DateTimeCompleted getDateTimeCompleted() { return dateTimeCompleted; }
    public void setDateTimeCompleted(String dateTimeCompleted) { setDateTimeCompleted(DateTimeCompleted.parse(dateTimeCompleted)); }
    public void setDateTimeCompleted(DateTimeCompleted dateTimeCompleted)
    {
    	orderChild(this.dateTimeCompleted, dateTimeCompleted);
    	this.dateTimeCompleted = dateTimeCompleted;
	}
    public void setDateTimeCompleted(ZonedDateTime dateTimeCompleted) { setDateTimeCompleted(new DateTimeCompleted(dateTimeCompleted)); }
    public VTodo withDateTimeCompleted(ZonedDateTime dateTimeCompleted)
    {
    	setDateTimeCompleted(dateTimeCompleted);
    	return this;
	}
    public VTodo withDateTimeCompleted(String dateTimeCompleted)
    {
    	setDateTimeCompleted(dateTimeCompleted);
    	return this;
	}
    public VTodo withDateTimeCompleted(DateTimeCompleted dateTimeCompleted)
    {
    	setDateTimeCompleted(dateTimeCompleted);
    	return this;
	}
    
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
    private DateTimeDue dateTimeDue;
    public DateTimeDue getDateTimeDue() { return dateTimeDue; }
    public void setDateTimeDue(String dateTimeDue) { setDateTimeDue(DateTimeDue.parse(dateTimeDue)); }
    public void setDateTimeDue(DateTimeDue dateTimeDue)
    {
    	orderChild(this.dateTimeDue, dateTimeDue);
    	this.dateTimeDue = dateTimeDue;
	}
    public void setDateTimeDue(Temporal dateTimeDue)
    {
        if ((dateTimeDue instanceof LocalDate) || (dateTimeDue instanceof LocalDateTime) || (dateTimeDue instanceof ZonedDateTime))
        {
            setDateTimeDue(new DateTimeDue(dateTimeDue));            
        } else
        {
            throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                    + dateTimeDue.getClass().getSimpleName() + " is not supported");
        }
    }
    public VTodo withDateTimeDue(Temporal dateTimeDue)
    {
    	setDateTimeDue(dateTimeDue);
    	return this;
	}
    public VTodo withDateTimeDue(String dateTimeDue)
    {
    	setDateTimeDue(dateTimeDue);
    	return this;
	}
    public VTodo withDateTimeDue(DateTimeDue dateTimeDue) 
    { 
    	setDateTimeDue(dateTimeDue); 
    	return this; 
	}

    @Override
    public void setDuration(DurationProp duration)
    {
        if ((getDateTimeDue() != null) && (getDuration() != null))
        {
            throw new DateTimeException("DURATION and DUE can't both be set");
        }      
        super.setDuration(duration);
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
    private PercentComplete percentComplete;
    public PercentComplete getPercentComplete() { return percentComplete; }
    public void setPercentComplete(String percentComplete) { setPercentComplete(PercentComplete.parse(percentComplete)); }
    public void setPercentComplete(Integer percentComplete) { setPercentComplete(new PercentComplete(percentComplete)); }
    public void setPercentComplete(PercentComplete percentComplete)
    {
    	orderChild(this.percentComplete, percentComplete);
    	this.percentComplete = percentComplete;
	}
    public VTodo withPercentComplete(PercentComplete percentComplete)
    { 
    	setPercentComplete(percentComplete); 
    	return this; 
	}
    public VTodo withPercentComplete(Integer percentComplete) 
    { 
    	setPercentComplete(percentComplete); 
    	return this; 
	}
    public VTodo withPercentComplete(String percentComplete) 
    { 
    	setPercentComplete(PercentComplete.parse(percentComplete));
    	return this; 
	}
    
    
	@Override
	public List<VTodo> calendarList()
	{
		if (getParent() != null)
		{
			VCalendar cal = (VCalendar) getParent();
			return cal.getVTodos();
		}
		return null;
	}
	
    /*
     * CONSTRUCTORS
     */
    public VTodo() { super(); }
   
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
        }
            
        return Collections.unmodifiableList(errors);
    }
    
    @Override
    public void eraseDateTimeProperties()
    {
        super.eraseDateTimeProperties();
        setDateTimeDue((DateTimeDue) null);
    }
    
    /**
     * Creates a new VTodo calendar component by parsing a String of iCalendar content lines
     *
     * @param content  the text to parse, not null
     * @return  the parsed VTodo
     */
    public static VTodo parse(String content)
    {
    	return VTodo.parse(new VTodo(), content);
    }
}
