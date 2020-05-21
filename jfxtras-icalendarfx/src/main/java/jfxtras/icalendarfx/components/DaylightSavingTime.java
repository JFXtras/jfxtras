/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.components;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardOrDaylight;
import jfxtras.icalendarfx.components.VTimeZone;
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
	public final static String NAME = "DAYLIGHT";
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
     * @param content  the text to parse, not null
     * @return  the parsed DaylightSavingTime
     */
    public static DaylightSavingTime parse(String content)
    {
    	return DaylightSavingTime.parse(new DaylightSavingTime(), content);
    }
}
