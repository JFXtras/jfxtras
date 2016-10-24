package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseAltText;

/**
 * LOCATION
 * RFC 5545 iCalendar 3.8.1.7. page 87
 * 
 * This property defines the intended venue for the activity defined by a calendar component.
 * 
 * Example:
 * LOCATION;ALTREP="http://xyzcorp.com/conf-rooms/f123.vcf":
 *  Conference Room - F123\, Bldg. 002
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 */
public class Location extends PropBaseAltText<String, Location>
{    
    public Location(Location source)
    {
        super(source);
    }

    public Location()
    {
        super();
    }
    
    public static Location parse(String propertyContent)
    {
        if (propertyContent != null)
        {
            Location property = new Location();
            property.parseContent(propertyContent);
            return property;
        } else
        {
            return null;
        }
    }
}
