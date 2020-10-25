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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.recurrence;

import java.time.temporal.Temporal;
import java.util.Set;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.PropertyBaseRecurrence;

/**
 * EXDATE
 * Exception Date-Times
 * RFC 5545 iCalendar 3.8.5.1, page 117.
 * 
 * This property defines the list of DATE-TIME exceptions for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class ExceptionDates extends PropertyBaseRecurrence<ExceptionDates>
{       
    @SuppressWarnings("unchecked")
    public ExceptionDates(Temporal...temporals)
    {
        super(temporals);
    }

    public ExceptionDates(ExceptionDates source)
    {
        super(source);
    }
    
    public ExceptionDates(Set<Temporal> value)
    {
        super(value);
    }
    
    public ExceptionDates()
    {
        super();
    }

    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static ExceptionDates parse(String content)
    {
    	return ExceptionDates.parse(new ExceptionDates(), content);
    }
    
    /** Parse string with Temporal class Exceptions provided as parameter */
    public static  ExceptionDates parse(Class<? extends Temporal> clazz, String content)
    {
        ExceptionDates property = ExceptionDates.parse(new ExceptionDates(), content);
        clazz.cast(property.getValue().iterator().next()); // class check
        return property;
    }
}
