package jfxtras.icalendarfx.properties;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.time.DateTimeCompleted;

/**
 * Abstract class for all UTC zoned-date-time classes
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DateTimeCompleted
 * @see DateTimeCreated
 * @see DateTimeStamp
 */
public abstract class PropBaseUTC<U> extends PropertyBase<ZonedDateTime,U>
{
    /*
     * CONSTRUCTORS
     */
    protected PropBaseUTC()
    {
        super();
    }
    
    public PropBaseUTC(ZonedDateTime temporal)
    {
        super(temporal);
    }
    
    public PropBaseUTC(PropBaseUTC<U> source)
    {
        super(source);
    }

    @Override
    public void setValue(ZonedDateTime value)
    {
        ZoneId zone = value.getZone();
        if (! zone.equals(ZoneId.of("Z")))
        {
            throw new DateTimeException("Unsupported ZoneId:" + zone + " only Z supported");
        }
        super.setValue(value);
    }
    
    @Override
    public boolean isValid()
    {
        ZoneId zone = getValue().getZone();
        if (! zone.equals(ZoneId.of("Z")))
        {
            return false;
        }
        return super.isValid();
    }
}
