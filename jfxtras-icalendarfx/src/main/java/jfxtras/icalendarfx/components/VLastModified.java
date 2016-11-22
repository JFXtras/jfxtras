package jfxtras.icalendarfx.components;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.properties.component.change.LastModified;

public interface VLastModified<T> extends VComponent
{
    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     * 
     * The property value MUST be specified in the UTC time format.
     */
    ObjectProperty<LastModified> dateTimeLastModifiedProperty();
    LastModified getDateTimeLastModified();
    default void setDateTimeLastModified(String lastModified)
    {
        if (getDateTimeLastModified() == null)
        {
            setDateTimeLastModified(LastModified.parse(lastModified));
        } else
        {
            LastModified temp = LastModified.parse(lastModified);
            if (temp.getValue().getClass().equals(getDateTimeLastModified().getValue().getClass()))
            {
                getDateTimeLastModified().setValue(temp.getValue());
            } else
            {
                setDateTimeLastModified(temp);
            }
        }
    }
    default void setDateTimeLastModified(LastModified lastModified) { dateTimeLastModifiedProperty().set(lastModified); }
    default void setDateTimeLastModified(ZonedDateTime lastModified)
    {
        setDateTimeLastModified(new LastModified(lastModified));
    }
    default T withDateTimeLastModified(ZonedDateTime lastModified)
    {
        setDateTimeLastModified(lastModified);
        return (T) this;
    }
    default T withDateTimeLastModified(String lastModified)
    {
        setDateTimeLastModified(lastModified);
        return (T) this;
    }
    default T withDateTimeLastModified(LastModified lastModified)
    {
        setDateTimeLastModified(lastModified);
        return (T) this;
    }
}
