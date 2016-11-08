package jfxtras.icalendarfx.components;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.properties.PropertyType;
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
public abstract class StandardOrDaylight<T> extends VRepeatableBase<T>
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
    public ObjectProperty<ObservableList<TimeZoneName>> timeZoneNamesProperty()
    {
        if (timeZoneNames == null)
        {
            timeZoneNames = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_NAME.toString());
        }
        return timeZoneNames;
    }
    private ObjectProperty<ObservableList<TimeZoneName>> timeZoneNames;
    public ObservableList<TimeZoneName> getTimeZoneNames()
    {
        return (timeZoneNames == null) ? null : timeZoneNames.get();
    }
    public void setTimeZoneNames(ObservableList<TimeZoneName> timeZoneNames)
    {
        if (timeZoneNames != null)
        {
            if ((this.timeZoneNames != null) && (this.timeZoneNames.get() != null))
            {
                // replace sort order in new list
                orderer().replaceList(timeZoneNamesProperty().get(), timeZoneNames);
            }
            orderer().registerSortOrderProperty(timeZoneNames);
        } else
        {
            orderer().unregisterSortOrderProperty(timeZoneNamesProperty().get());
        }
        timeZoneNamesProperty().set(timeZoneNames);
    }
    /**
     * Sets the value of the {@link #timeZoneNamesProperty()}
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(ObservableList<TimeZoneName> timeZoneNames)
    {
        setTimeZoneNames(timeZoneNames);
        return (T) this;
    }
    /**
     * Sets the value of the {@link #timeZoneNamesProperty()} by parsing a vararg of time zone name strings
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(String...timeZoneNames)
    {
        List<TimeZoneName> a = Arrays.stream(timeZoneNames)
                .map(c -> TimeZoneName.parse(c))
                .collect(Collectors.toList());
        setTimeZoneNames(FXCollections.observableArrayList(a));
        return (T) this;
    }
    /**
     * Sets the value of the {@link #timeZoneNamesProperty()} from a vararg of {@link TimeZoneName} objects.
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(TimeZoneName...timeZoneNames)
    {
        if (getTimeZoneNames() == null)
        {
            setTimeZoneNames(FXCollections.observableArrayList(timeZoneNames));
        } else
        {
            getTimeZoneNames().addAll(timeZoneNames);
        }
        return (T) this;
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
    public ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFromProperty()
    {
        if (timeZoneOffsetFrom == null)
        {
            timeZoneOffsetFrom = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_OFFSET_FROM.toString());
            orderer().registerSortOrderProperty(timeZoneOffsetFrom);
        }
        return timeZoneOffsetFrom;
    }
    private ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFrom;
    public TimeZoneOffsetFrom getTimeZoneOffsetFrom() { return timeZoneOffsetFromProperty().get(); }
    public void setTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom) { timeZoneOffsetFromProperty().set(timeZoneOffsetFrom); }
    public void setTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(new TimeZoneOffsetFrom(zoneOffset)); }
    public void setTimeZoneOffsetFrom(String timeZoneOffsetFrom) { PropertyType.TIME_ZONE_OFFSET_FROM.parse(this, timeZoneOffsetFrom); }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetFromProperty()} by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * 
     * @param zoneOffset  value for new {@link TimeZoneOffsetFrom}
     */
    public T withTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom) { setTimeZoneOffsetFrom(timeZoneOffsetFrom); return (T) this; }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetFromProperty()} by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(zoneOffset); return (T) this; }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetFromProperty()} by parsing a iCalendar content string</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetFrom(String timeZoneOffsetFrom) { setTimeZoneOffsetFrom(timeZoneOffsetFrom); return (T) this; }


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
    public ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetToProperty()
    {
        if (timeZoneOffsetTo == null)
        {
            timeZoneOffsetTo = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_OFFSET_TO.toString());
            orderer().registerSortOrderProperty(timeZoneOffsetTo);
        }
        return timeZoneOffsetTo;
    }
    private ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetTo;
    public TimeZoneOffsetTo getTimeZoneOffsetTo() { return timeZoneOffsetToProperty().get(); }
    public void setTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo) { timeZoneOffsetToProperty().set(timeZoneOffsetTo); }
    public void setTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(new TimeZoneOffsetTo(zoneOffset)); }
    public void setTimeZoneOffsetTo(String timeZoneOffsetTo) { PropertyType.TIME_ZONE_OFFSET_TO.parse(this, timeZoneOffsetTo); }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetToProperty()}</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo) { setTimeZoneOffsetTo(timeZoneOffsetTo); return (T) this; }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetToProperty()} by creating a new {@link TimeZoneOffsetTo} from
     * the {@link ZoneOffset} parameter</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(zoneOffset); return (T) this; }
    /**
     * <p>Sets the value of the {@link #timeZoneOffsetToProperty()} by parsing a iCalendar content string</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(String timeZoneOffsetTo) { setTimeZoneOffsetTo(timeZoneOffsetTo); return (T) this; }
    
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
