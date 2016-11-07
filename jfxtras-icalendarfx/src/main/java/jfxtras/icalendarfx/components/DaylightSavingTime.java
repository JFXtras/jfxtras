package jfxtras.icalendarfx.components;

import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;

/**
 * <p>DAYLIGHT<br>
 * Describes Daylight Saving Time<br>
 * RFC 5545, 3.6.5, page 65</p>
 * 
 * <p>The DAYLIGHT sub-component is always a child of a VTIMEZONE calendar component.  It can't
 * exist alone.  The "STANDARD" or "DAYLIGHT" sub-component MUST
 * include the {@link DateTimeStart DTSTART}, {@link TimeZoneOffsetFrom TZOFFSETFROM},
 * and {@link TimeZoneOffsetTo TZOFFSETTO} properties.</p>
 *
 * <p>The "DAYLIGHT" sub-component consists of a collection of properties
 * that describe Daylight Saving Time.  In general, this collection
 * of properties consists of:
 *<ul>
 *<li>the first onset DATE-TIME for the observance;
 *<li>the last onset DATE-TIME for the observance, if a last onset is
 *        known;
 *<li>the offset to be applied for the observance;
 *<li>a rule that describes the day and time when the observance
 *        takes effect;
 *<li>an optional name for the observance.</p>
 *</ul>
 *</p>
 *<p>Properties available to this sub-component include:
 *<ul>
 *<li>{@link Comment COMMENT}
 *<li>{@link DateTimeStart DTSTART}
 *<li>{@link RecurrenceDates RDATE}
 *<li>{@link RecurrenceRule RRULE}
 *<li>{@link TimeZoneName TZNAME}
 *<li>{@link TimeZoneOffsetFrom TZOFFSETFROM}
 *<li>{@link TimeZoneOffsetTo TZOFFSETTO}
 *<li>{@link NonStandardProperty X-PROP}
 *</ul>
 *</p>
 * 
 * @author David Bal
 * 
 * @see VTimeZone
 *
 */
public class DaylightSavingTime extends StandardOrDaylight<DaylightSavingTime>
{   
    /*
     * CONSTRUCTORS
     */
    
    /**
     * Creates a default DaylightSavingTime calendar component with no properties
     */
    public DaylightSavingTime()
    {
        super();
    }

    /**
     * Creates a deep copy of a DaylightSavingTime calendar component
     */
    public DaylightSavingTime(DaylightSavingTime source)
    {
        super(source);
    }

    /**
     * Creates a new DaylightSavingTime calendar component by parsing a String of iCalendar content lines
     *
     * @param contentLines  the text to parse, not null
     * @return  the parsed DaylightSavingTime
     */
    @Deprecated // use simple factory
    public static DaylightSavingTime parse(String contentLines)
    {
        DaylightSavingTime component = new DaylightSavingTime();
        component.parseContent(contentLines);
        return component;
    }
}
