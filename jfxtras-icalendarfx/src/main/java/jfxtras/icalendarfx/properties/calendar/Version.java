package jfxtras.icalendarfx.properties.calendar;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * VERSION
 * RFC 5545, 3.7.4, page 79
 * 
 * This property specifies the identifier corresponding to the
 * highest version number or the minimum and maximum range of the
 * iCalendar specification that is required in order to interpret the
 * iCalendar object. 
 * 
 * A value of "2.0" corresponds to this software.
 * 
 * Example:
 * VERSION:2.0
 * 
 * @author David Bal
 * @see VCalendar
 */
public class Version extends PropertyBase<String, Version> implements VElement
{
    public static final String DEFAULT_ICALENDAR_SPECIFICATION_VERSION = ("2.0");
    
    public Version(Version source)
    {
        super(source);
    }
    
    /** Set version to default value of 2.0 */
    public Version()
    {
        super();
        setValue(DEFAULT_ICALENDAR_SPECIFICATION_VERSION);
    }
    
    public static Version parse(String string)
    {
        Version property = new Version();
        property.parseContent(string);
        return property;
    }
}
