package jfxtras.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDescribable2;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VDuration;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

public abstract class VLocatable<T> extends VDisplayable<T> implements VDescribable2<T>, VDuration<T>
{
    /**
     * DESCRIPTION
     * RFC 5545 iCalendar 3.8.1.5. page 84
     * 
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * 
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     *
     * Note: Only VJournal allows multiple instances of DESCRIPTION
     */
    @Override
    public Description getDescription() { return description; }
    private Description description;
    @Override
	public void setDescription(Description description)
    {
    	orderChild(this.description, description);
    	this.description = description;
	}

    /** 
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    private DurationProp duration;
	@Override
	public DurationProp getDuration() { return duration; }
    @Override
	public void setDuration(DurationProp duration)
    {
        if (duration != null)
        {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime adjustedNow = now.plus(duration.getValue());
            
            if (adjustedNow.isBefore(now))
            {
                throw new DateTimeException("DURATION is negative (" + duration + "). DURATION MUST be positive.");
            }
        }
        orderChild(this.duration, duration);
        this.duration = duration;
    }
    
    /**
     * GEO: Geographic Position
     * RFC 5545 iCalendar 3.8.1.6 page 85, 3.3.6 page 85
     * This property specifies information related to the global
     * position for the activity specified by a calendar component.
     * 
     * This property value specifies latitude and longitude,
     * in that order (i.e., "LAT LON" ordering).
     * 
     * Example:
     * GEO:37.386013;-122.082932
     */
    private GeographicPosition geographicPosition;
    public GeographicPosition getGeographicPosition() { return geographicPosition; }
    public void setGeographicPosition(GeographicPosition geographicPosition)
    {
    	orderChild(this.geographicPosition, geographicPosition);
    	this.geographicPosition = geographicPosition;
	}
    public void setGeographicPosition(String geographicPosition) { setGeographicPosition(GeographicPosition.parse(geographicPosition)); }
    public void setGeographicPosition(double latitude, double longitude)
    {
        getGeographicPosition().setLatitude(latitude);
        getGeographicPosition().setLongitude(longitude);
    }
    public T withGeographicPosition(GeographicPosition geographicPosition)
    {
        setGeographicPosition(geographicPosition);
        return (T) this;
    }
    public T withGeographicPosition(String geographicPosition)
    {
        setGeographicPosition(geographicPosition);
        return (T) this;
    }
    public T withGeographicPosition(double latitude, double longitude)
    {
        setGeographicPosition(latitude, longitude);
        return (T) this;
    }

    /**
     * LOCATION:
     * RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    private Location location;
    public Location getLocation() { return location; }
    public void setLocation(Location location)
    {
    	orderChild(this.location, location);
    	this.location = location;
	}
    public void setLocation(String location) { setLocation(Location.parse(location)); }
    public T withLocation(Location location)
    {
        setLocation(location);
        return (T) this;
    }
    public T withLocation(String location)
    {
        setLocation(location);
        return (T) this;
    }

    /**
     * PRIORITY
     * RFC 5545 iCalendar 3.8.1.6 page 85, 3.3.6 page 85
     * This property defines the relative priority for a calendar component.
     * This priority is specified as an integer in the range 0 to 9.
     * 
     * Example: The following is an example of a property with the highest priority:
     * PRIORITY:1
     */
    private Priority priority;
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority)
    {
    	orderChild(this.priority, priority);
    	this.priority = priority;
	}
    public void setPriority(String priority) { setPriority(Priority.parse(priority)); }
    public void setPriority(int priority) { setPriority(new Priority(priority)); }
    public T withPriority(Priority priority)
    {
        setPriority(priority);
        return (T) this;
    }
    public T withPriority(String priority)
    {
        setPriority(priority);
        return (T) this;
    }
    public T withPriority(int priority)
    {
        setPriority(priority);
        return (T) this;
    }
    
    /**
     * RESOURCES:
     * RFC 5545 iCalendar 3.8.1.10. page 91
     * This property defines the equipment or resources
     * anticipated for an activity specified by a calendar component.
     * More than one resource can be specified as a COMMA-separated list
     * Example:
     * RESOURCES:EASEL,PROJECTOR,VCR
     * RESOURCES;LANGUAGE=fr:Nettoyeur haute pression
     */
    public List<Resources> getResources() { return resources; }
    private List<Resources> resources;
    public void setResources(List<Resources> resources)
    {
    	if (this.resources != null)
    	{
    		this.resources.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.resources = resources;
    	if (resources != null)
    	{
    		resources.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    public T withResources(List<Resources> resources)
    {
    	if (getResources() == null)
    	{
    		setResources(new ArrayList<>());
    	}
    	getResources().addAll(resources);
    	if (resources != null)
    	{
    		resources.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    public T withResources(String...resources)
    {
        List<Resources> list = Arrays.stream(resources)
                .map(c -> Resources.parse(c))
                .collect(Collectors.toList());
        return withResources(list);
    }
    public T withResources(Resources...resources)
    {
    	return withResources(Arrays.asList(resources));
    }

    /** 
     * VALARM
     * Alarm Component
     * RFC 5545 iCalendar 3.6.6. page 71
     * 
     * Provide a grouping of component properties that define an alarm.
     * 
     * The "VALARM" calendar component MUST only appear within either a
     * "VEVENT" or "VTODO" calendar component.
     */
    public List<VAlarm> getVAlarms() { return vAlarms; }
    private List<VAlarm> vAlarms;
    public void setVAlarms(List<VAlarm> vAlarms)
    {
    	if (this.vAlarms != null)
    	{
    		this.vAlarms.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.vAlarms = vAlarms;
    	if (vAlarms != null)
    	{
    		vAlarms.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    public T withVAlarms(List<VAlarm> vAlarms)
    {
    	if (getVAlarms() == null)
    	{
    		setVAlarms(new ArrayList<>());
    	}
    	if (vAlarms != null)
    	{
    		getVAlarms().addAll(vAlarms);
    		vAlarms.forEach(c -> orderChild(c));
    	}
    	return (T) this;
	}
    public T withVAlarms(VAlarm...vAlarms)
    {
    	withVAlarms(Arrays.asList(vAlarms));
        return (T) this;
        
    }
    public T withVAlarms(String...vAlarms)
    {
        List<VAlarm> newElements = Arrays.stream(vAlarms)
                .map(c -> VAlarm.parse(c))
                .collect(Collectors.toList());
        return withVAlarms(newElements);
    }
    
    /*
     * CONSTRUCTORS
     */
    public VLocatable() { super(); }
    
    public VLocatable(VLocatable<T> source)
    {
        super(source);
    }

    @Override
    void addSubcomponent(VComponent subcomponent)
    {
        if (subcomponent instanceof VAlarm)
        {
            final List<VAlarm> list;
            if (getVAlarms() == null)
            {
                list = new ArrayList<>();
                setVAlarms(list);
            } else
            {
                list = getVAlarms();
            }
            list.add((VAlarm) subcomponent);
        } else
        {
            throw new IllegalArgumentException("Unspoorted subcomponent type:" + subcomponent.getClass().getSimpleName() +
                    " found inside " + name() + " component");
        }        
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getVAlarms() != null)
        {
            getVAlarms().forEach(v -> errors.addAll(v.errors()));
        }
        boolean isDurationPresent = getDuration() != null;
        if (isDurationPresent) // duration is present
        {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime adjustedNow = now.plus(getDuration().getValue());
            if (adjustedNow.isBefore(now))
            {
                errors.add("DURATION is negative (" + getDuration().getValue() + "). DURATION MUST be positive.");                
            }
        }
        return errors;
    }
    
    @Override // include VAlarms
    public int hashCode()
    {
        int hash = super.hashCode();
        if (getVAlarms() != null)
        {
            Iterator<VAlarm> i = getVAlarms().iterator();
            while (i.hasNext())
            {
                Object property = i.next();
                hash = (31 * hash) + property.hashCode();
            }
        }
        return hash;
    }
    
    /** Stream recurrence dates with adjustment to include recurrences that don't end before start parameter */
    @Override
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        final TemporalAmount adjustment = getActualDuration();
        return super.streamRecurrences(start.minus(adjustment));
    }
    
    @Override
    public void eraseDateTimeProperties()
    {
        super.eraseDateTimeProperties();
        setDuration((DurationProp) null);
    }
    
    /** A convenience method that returns either Duration property value, or a calculated duration based on start and end values */
    public abstract TemporalAmount getActualDuration();
    
    /**
     * A convenience method that sets DTEND, DURATION (VEvent) or DUE (VTodo) depending on which ever is already set
     * to new value calculated by the duration or period between input parameters (depending on if the parameters
     * are LocalDate or a date/time type (i.e. ZonedDateTime))
     *  
     * Note: In order to set DTEND, DTSTART must be assigned a value.  DURATION and DUE doesn't require a DTSTART value.
     *  
     * @param startRecurrence
     * @param endRecurrence
     */
    public abstract void setEndOrDuration(Temporal startRecurrence, Temporal endRecurrence);
}
