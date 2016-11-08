package jfxtras.icalendarfx.properties.component.timezone;

import java.time.ZoneOffset;

import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * Base class for ZoneOffset-based properties
 * 
 * @author David Bal
 *
 * @param <U> - concrete subclass
 * @see TimeZoneOffsetFrom
 * @see TimeZoneOffsetTo
 */
public abstract class PropertyBaseZoneOffset<U> extends PropertyBase<ZoneOffset,U>
{
//    public PropertyBaseZoneOffset(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    public PropertyBaseZoneOffset(PropertyBaseZoneOffset<U> source)
    {
        super(source);
    }
    
    public PropertyBaseZoneOffset(ZoneOffset value)
    {
        super(value);
    }

    public PropertyBaseZoneOffset()
    {
        super();
    }
}
