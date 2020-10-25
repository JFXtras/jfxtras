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
package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;

import org.junit.Test;

import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneIdentifier;

public class TimeZoneIdentifierTest
{
    @Test
    public void canParseTimeZoneIdentifier1()
    {
        String content = "TZID:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = TimeZoneIdentifier.parse(content);
        assertEquals(content, madeProperty.toString());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"));
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseTimeZoneIdentifier2()
    {
        String content = "TZID;VALUE=TEXT:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = TimeZoneIdentifier.parse(content);
        assertEquals(content, madeProperty.toString());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"))
                .withValueType(ValueType.TEXT);
        assertEquals(expectedProperty, madeProperty);
    }

    @Test
    public void canParseTimeZoneIdentifier3()
    {
        String content = "TZID:/US-New_York-New_York";
        TimeZoneIdentifier madeProperty = TimeZoneIdentifier.parse(content);
        assertEquals(content, madeProperty.toString());
        TimeZoneIdentifier expectedProperty = TimeZoneIdentifier.parse("/US-New_York-New_York");
        assertEquals(expectedProperty, madeProperty);
        assertNull(expectedProperty.getValue());
    }
}
