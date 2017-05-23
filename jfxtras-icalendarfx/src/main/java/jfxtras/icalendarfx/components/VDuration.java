package jfxtras.icalendarfx.components;

import java.time.temporal.TemporalAmount;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/**
 * Interface for {@link DurationProp} property
 * 
 * @author David Bal
 *
 * @param <T>  concrete subclass
 */
public interface VDuration<T> extends VComponent
{
    /*
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    /** Gets the value of the {@link DurationProp} */
    DurationProp getDuration();
    /** Sets the value of the {@link DurationProp} */
    void setDuration(DurationProp duration);
    /** Sets the value of the {@link DurationProp} by parsing iCalendar content text */
    default void setDuration(String duration)
    {
    	setDuration(DurationProp.parse(duration));
    }
    /** Sets the value of the {@link DurationProp} by creating new {@link DurationProp} from the TemporalAmount parameter */
    default void setDuration(TemporalAmount duration)
    {
        setDuration(new DurationProp(duration));
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property } by creating a new {@link DurationProp} from
     * the TemporalAmount parameter</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(TemporalAmount duration)
    {
        setDuration(duration);
        return (T) this;
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property } by parsing iCalendar content text</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(String duration)
    {
        setDuration(duration);
        return (T) this;
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property}</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(DurationProp duration)
    {
        setDuration(duration);
        return (T) this;
    }
}
