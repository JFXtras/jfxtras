package jfxtras.icalendarfx.components;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardOrDaylight;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VRepeatableBase;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;

/**
 * <p>Superclass of {@link DaylightSavingTime} and {@link StandardTime} that
 * contains the following properties:
 *<ul>
 *<li>{@link TimeZoneName TZNAME}
 *<li>{@link TimeZoneOffsetFrom TZOFFSETFROM}
 *<li>{@link TimeZoneOffsetTo TZOFFSETTO}
 *</ul>
 *</p>
 * 
 * @author David Bal
 *
 */
public abstract class StandardOrDaylight<T extends StandardOrDaylight<T>> extends VRepeatableBase<T>
{
    /**
     * <p>This property specifies the customary designation for a time zone description.<br>
     * RFC 5545, 3.8.3.2, page 103
     * </p>
     * 
     * <p>EXAMPLES:
     * <ul>
     * <li>TZNAME:EST
     * <li>TZNAME;LANGUAGE=fr-CA:HN
     * </ul>
     * </p>
     */
    private List<TimeZoneName> timeZoneNames;
    public List<TimeZoneName> getTimeZoneNames() { return timeZoneNames; }
    public void setTimeZoneNames(List<TimeZoneName> timeZoneNames)
    {
    	if (this.timeZoneNames != null)
    	{
    		this.timeZoneNames.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.timeZoneNames = timeZoneNames;
    	if (timeZoneNames != null)
    	{
    		timeZoneNames.forEach(c -> orderChild(c));
    	}
	}
    /**
     * Sets the value of the {@link #timeZoneNamesProperty()}
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(List<TimeZoneName> timeZoneNames)
    {
    	if (getTimeZoneNames() == null)
    	{
    		setTimeZoneNames(new ArrayList<>());
    	}
    	getTimeZoneNames().addAll(timeZoneNames);
    	if (timeZoneNames != null)
    	{
    		timeZoneNames.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    /**
     * Sets the value of the {@link #timeZoneNamesProperty()} by parsing a vararg of time zone name strings
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(String...timeZoneNames)
    {
        List<TimeZoneName> newElements = Arrays.stream(timeZoneNames)
                .map(c -> TimeZoneName.parse(c))
                .collect(Collectors.toList());
        return withTimeZoneNames(newElements);
    }
    /**
     * Sets the value of the {@link #timeZoneNamesProperty()} from a vararg of {@link TimeZoneName} objects.
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(TimeZoneName...timeZoneNames)
    {
    	return withTimeZoneNames(Arrays.asList(timeZoneNames));
    }
    
    /**
     * <p>This property specifies the offset that is in use prior to this time zone observance.<br>
     * RFC 5545, 3.8.3.3, page 104
     * </p>
     * 
     * <p>EXAMPLES:
     * <ul>
     * <li>TZOFFSETFROM:-0500
     * <li>TZOFFSETFROM:+1345
     * </ul>
     */
    private TimeZoneOffsetFrom timeZoneOffsetFrom;
    public TimeZoneOffsetFrom getTimeZoneOffsetFrom() { return timeZoneOffsetFrom; }
    public void setTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom)
    {
    	orderChild(this.timeZoneOffsetFrom, timeZoneOffsetFrom);
    	this.timeZoneOffsetFrom = timeZoneOffsetFrom;
	}
    public void setTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(new TimeZoneOffsetFrom(zoneOffset)); }
    public void setTimeZoneOffsetFrom(String timeZoneOffsetFrom) { setTimeZoneOffsetFrom(TimeZoneOffsetFrom.parse(timeZoneOffsetFrom)); }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetFromProperty()} by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * 
     * @param zoneOffset  value for new {@link TimeZoneOffsetFrom}
     */
    public T withTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom)
    {
    	setTimeZoneOffsetFrom(timeZoneOffsetFrom);
    	return (T) this;
	}
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetFromProperty()} by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetFrom(ZoneOffset zoneOffset)
    {
    	setTimeZoneOffsetFrom(zoneOffset);
    	return (T) this;
	}
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetFromProperty()} by parsing a iCalendar content string</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetFrom(String timeZoneOffsetFrom)
    {
    	setTimeZoneOffsetFrom(timeZoneOffsetFrom);
    	return (T) this;
	}


    /**
     * <p>This property specifies the offset that is in use in this time zone observance<br>
     * RFC 5545, 3.8.3.4, page 105</p>
     * 
     * <p>EXAMPLES:
     * <ul>
     * <li>TZOFFSETTO:-0400
     * <li>TZOFFSETTO:+1245
     * </ul>
     */
    private TimeZoneOffsetTo timeZoneOffsetTo;
    public TimeZoneOffsetTo getTimeZoneOffsetTo() { return timeZoneOffsetTo; }
    public void setTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo)
    {
    	orderChild(this.timeZoneOffsetTo, timeZoneOffsetTo);
    	this.timeZoneOffsetTo = timeZoneOffsetTo;
	}
    public void setTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(new TimeZoneOffsetTo(zoneOffset)); }
    public void setTimeZoneOffsetTo(String timeZoneOffsetTo) { setTimeZoneOffsetTo(TimeZoneOffsetTo.parse(timeZoneOffsetTo)); }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetToProperty()}</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo)
    {
    	setTimeZoneOffsetTo(timeZoneOffsetTo);
    	return (T) this;
	}
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetToProperty()} by creating a new {@link TimeZoneOffsetTo} from
     * the {@link ZoneOffset} parameter</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(ZoneOffset zoneOffset)
    {
    	setTimeZoneOffsetTo(zoneOffset);
    	return (T) this; 
	}
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetToProperty()} by parsing a iCalendar content string</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(String timeZoneOffsetTo) 
    {
    	setTimeZoneOffsetTo(timeZoneOffsetTo);
    	return (T) this;
	}
    
	@Override
	public List<? extends VComponent> calendarList()
	{
		throw new RuntimeException("Subcomponent " + name() + " is embedded in VTimeZone not VCalendar");
	}
    
    /*
     * CONSTRUCTORS
     */
    public StandardOrDaylight() { super(); }

    public StandardOrDaylight(StandardOrDaylight<T> source)
    {
        super(source);
    }

    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getDateTimeStart() == null)
        {
            errors.add("DTSTART is not present.  DTSTART is REQUIRED and MUST NOT occur more than once");
        }
        
        if (getTimeZoneOffsetFrom() == null)
        {
            errors.add("TZOFFSETFROM is not present.  TZOFFSETFROM is REQUIRED and MUST NOT occur more than once");
        }
        
        if (getTimeZoneOffsetTo() == null)
        {
            errors.add("TZOFFSETTO is not present.  TZOFFSETTO is REQUIRED and MUST NOT occur more than once");
        }
        return Collections.unmodifiableList(errors);
    }
    
    // Recurrence streamer - produces recurrence set
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceCache() { return streamer; }
}
