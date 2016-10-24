package jfxtras.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;

/**
 * For single DESCRIPTION property
 * Note: Not for VJournal - allows multiple descriptions
 * 
 * <p>{@link VComponent} with the following properties
 * <ul>
 * <li>{@link Description DESCRIPTION}
 * </ul>
 * </p>
 * 
 * @author David Bal
 */
public interface VDescribable2<T> extends VDescribable<T>
{
    /**
     * <p>This property provides a more complete description of the
     * calendar component than that provided by the {@link Summary} property.<br>
     * RFC 5545 iCalendar 3.8.1.5. page 84</p>
     * 
     * <p>Example:
     * <ul>
     * <li>DESCRIPTION:Meeting to provide technical review for "Phoenix"<br>
     *  design.\nHappy Face Conference Room. Phoenix design team<br>
     *  MUST attend this meeting.\nRSVP to team leader.
     * </ul>
     *
     * <p>Note: Only {@link VJournal} allows multiple instances of DESCRIPTION</p>
     */
    ObjectProperty<Description> descriptionProperty();
    Description getDescription();
    default void setDescription(Description description) { descriptionProperty().set(description); }
    default void setDescription(String description)
    {
        if (description == null)
        {
            descriptionProperty().set(null);
        } else if (getDescription() == null)
        {
            setDescription(Description.parse(description));
        } else
        {
            getDescription().setValue(description);
        }
    }
    /**
     * Sets the value of the {@link Description}
     * 
     * @return - this class for chaining
     */
    default T withDescription(Description description)
    {
        setDescription(description);
        return (T) this;
    }
    /**
     * Sets the value of the {@link Description} by parsing iCalendar text
     * 
     * @return - this class for chaining
     */
    default T withDescription(String description)
    {
        setDescription(description);
        return (T) this;
    }
}
