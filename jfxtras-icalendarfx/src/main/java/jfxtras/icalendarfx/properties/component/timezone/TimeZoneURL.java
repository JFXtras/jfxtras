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
package jfxtras.icalendarfx.properties.component.timezone;

import java.net.URI;

import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.properties.VPropertyBase;

/**
 * TZURL
 * Time Zone URL
 * RFC 5545, 3.8.3.5, page 106
 * 
 * This property provides a means for a "VTIMEZONE" component
 * to point to a network location that can be used to retrieve an up-
 * to-date version of itself
 * 
 * EXAMPLES:
 * TZURL:http://timezones.example.org/tz/America-Los_Angeles.ics
 * 
 * @author David Bal
 * @see VTimeZone
 */
public class TimeZoneURL extends VPropertyBase<URI,TimeZoneURL>
{
    public TimeZoneURL(TimeZoneURL source)
    {
        super(source);
    }
    
    public TimeZoneURL(URI value)
    {
        super(value);
    }
    
    public TimeZoneURL()
    {
        super();
    }

    public static TimeZoneURL parse(String content)
    {
    	return TimeZoneURL.parse(new TimeZoneURL(), content);
    }
}
