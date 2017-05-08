package jfxtras.icalendarfx.properties.calendar;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.CalendarScale.CalendarScaleType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * CALSCALE
 * Calendar Scale
 * RFC 5545, 3.7.1, page 76
 * 
 * This property defines the calendar scale used for the
 * calendar information specified in the iCalendar object.
 * 
 * Only allowed value is GREGORIAN
 * It is expected that other calendar scales will be defined in other specifications or by
 * future versions of this memo. 
 * 
 * Example:
 * CALSCALE:GREGORIAN
 * 
 * @author David Bal
 * @see VCalendar
 */
public class CalendarScale extends VPropertyBase<CalendarScaleType, CalendarScale> implements VElement
{
    public static final CalendarScaleType DEFAULT_CALENDAR_SCALE = CalendarScaleType.GREGORIAN;
    
    private final static StringConverter<CalendarScaleType> CONVERTER = new StringConverter<CalendarScaleType>()
    {
        @Override
        public String toString(CalendarScaleType object)
        {
            return object.toString();
        }

        @Override
        public CalendarScaleType fromString(String string)
        {
            return CalendarScaleType.valueOf(string);
        }
    };
    
    public CalendarScale(CalendarScale source)
    {
        super(source);
    }

    /** sets default value of GREGORIAN */
    public CalendarScale()
    {
       super();
       setConverter(CONVERTER);
    }
    
    public CalendarScale(CalendarScaleType calendarScaleType)
    {
       super(calendarScaleType);
       setConverter(CONVERTER);
    }

    
    public enum CalendarScaleType
    {
        GREGORIAN;
    }

    public static CalendarScale parse(String content)
    {
    	return CalendarScale.parse(new CalendarScale(), content);
    }    
}
