package jfxtras.icalendarfx.components;

import java.time.temporal.TemporalAmount;

import javafx.beans.property.ObjectProperty;
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
    /** A property wrapping the {@link DurationProp} value. */
    ObjectProperty<DurationProp> durationProperty();
    /** Gets the value of the {@link DurationProp} */
    default DurationProp getDuration() { return durationProperty().get(); }
    /** Sets the value of the {@link DurationProp} */
    default void setDuration(DurationProp duration) { durationProperty().set(duration); }
    /** Sets the value of the {@link DurationProp} by parsing iCalendar content text */
    default void setDuration(String duration)
    {
        if (getDuration() == null)
        {
            setDuration(DurationProp.parse(duration));
        } else
        {
            DurationProp temp = DurationProp.parse(duration);
            if (temp.getValue().getClass().equals(getDuration().getValue().getClass()))
            {
                getDuration().setValue(temp.getValue());
            } else
            {
                setDuration(temp);
            }
        }
    }
    /** Sets the value of the {@link DurationProp} by creating new {@link DurationProp} from the TemporalAmount parameter */
    default void setDuration(TemporalAmount duration)
    {
        if (getDuration() == null)
        {
            setDuration(new DurationProp(duration));
        } else
        {
            getDuration().setValue(duration);
        }
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property } by creating a new {@link DurationProp} from
     * the TemporalAmount parameter</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(TemporalAmount duration)
    {
        if (getDuration() == null)
        {
            setDuration(duration);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property } by parsing iCalendar content text</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(String duration)
    {
        if (getDuration() == null)
        {
            setDuration(duration);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property}</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(DurationProp duration)
    {
        if (getDuration() == null)
        {
            setDuration(duration);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
}
