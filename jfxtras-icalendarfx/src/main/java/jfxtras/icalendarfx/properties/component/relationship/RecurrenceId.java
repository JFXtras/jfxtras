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
package jfxtras.icalendarfx.properties.component.relationship;

import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.Range;
import jfxtras.icalendarfx.parameters.Range.RangeType;
import jfxtras.icalendarfx.properties.PropBaseDateTime;
import jfxtras.icalendarfx.properties.PropRecurrenceID;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;

/**
 * RECURRENCE-ID
 * RFC 5545, 3.8.4.4, page 112
 * 
 * This property is used in conjunction with the "UID" and
 * "SEQUENCE" properties to identify a specific instance of a
 * recurring "VEVENT", "VTODO", or "VJOURNAL" calendar component.
 * The property value is the original value of the "DTSTART" property
 * of the recurrence instance.
 * 
 * The "RANGE" parameter is used to specify the effective range of
 * recurrence instances from the instance specified by the
 * "RECURRENCE-ID" property value.  The value for the range parameter
 * can only be "THISANDFUTURE"  Note: THISANDFUTURE is not supported by
 * most iCalendar implementations.  It may be better to truncate the
 * unbounded recurring calendar component (i.e., with the "COUNT"
 * or "UNTIL" rule parts), and create two new unbounded recurring
 * calendar components for the future instances.
 * 
 * Example:
 * RECURRENCE-ID;VALUE=DATE:19960401
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class RecurrenceId extends PropBaseDateTime<Temporal, RecurrenceId> implements PropRecurrenceID<Temporal>
{
    /**
     * RANGE
     * Recurrence Identifier Range
     * RFC 5545, 3.2.13, page 23
     * 
     * To specify the effective range of recurrence instances from
     *  the instance specified by the recurrence identifier specified by
     *  the property.
     * 
     * Example:
     * RECURRENCE-ID;RANGE=THISANDFUTURE:19980401T133000Z
     * 
     * @author David Bal
     *
     */
    @Override
    public Range getRange() { return range; }
    private Range range;
    @Override
    public void setRange(Range range)
    {
    	orderChild(range);
    	this.range = range;
	}
    public void setRange(String value) { setRange(new Range(value)); }
    public RecurrenceId withRange(Range altrep) { setRange(altrep); return this; }
    public RecurrenceId withRange(RangeType value) { setRange(new Range(value)); return this; }
    public RecurrenceId withRange(String content) { setRange(content); return this; }

    /*
     * CONSTRUCTORS
     */
   public RecurrenceId(Temporal temporal)
    {
        super(temporal);
    }
    
    public RecurrenceId(RecurrenceId source)
    {
        super(source);
    }
    
    public RecurrenceId()
    {
        super();
    }
    
    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static RecurrenceId parse(String content)
    {
    	return RecurrenceId.parse(new RecurrenceId(), content);
    }
    
    /** Parse string with Temporal class explicitly provided as parameter */
    public static RecurrenceId parse(Class<? extends Temporal> clazz, String content)
    {
    	RecurrenceId property = RecurrenceId.parse(new RecurrenceId(), content);
        clazz.cast(property.getValue()); // class check
        return property;
    }
}
