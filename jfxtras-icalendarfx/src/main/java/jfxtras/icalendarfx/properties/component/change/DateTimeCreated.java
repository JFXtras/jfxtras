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
package jfxtras.icalendarfx.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseUTC;
import jfxtras.icalendarfx.properties.component.change.DateTimeCreated;

/**
 * CREATED
 * Date-Time Created
 * RFC 5545, 3.8.7.1, page 136
 * 
 * This property specifies the date and time that the calendar information
 * was created by the calendar user agent in the calendar store.
 * 
 * The value MUST be specified as a date with UTC time.
 * 
 * Example:
 * CREATED:19960329T133000Z
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class DateTimeCreated extends PropBaseUTC<DateTimeCreated>
{
    public DateTimeCreated(ZonedDateTime temporal)
    {
        super(temporal);
    }
    
    public DateTimeCreated(DateTimeCreated source)
    {
        super(source);
    }
    
    public DateTimeCreated()
    {
        super();
    }

    public static DateTimeCreated parse(String content)
    {
    	return DateTimeCreated.parse(new DateTimeCreated(), content);
    }
}
