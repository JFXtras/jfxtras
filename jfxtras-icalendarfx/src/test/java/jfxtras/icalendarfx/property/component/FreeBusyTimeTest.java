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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.icalendarfx.properties.component.time.FreeBusyTime;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.Pair;

public class FreeBusyTimeTest
{
    @Test
    public void canParseFreeBusyTime1()
    {
        String content = "FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M";
        FreeBusyTime madeProperty = FreeBusyTime.parse(content);
        assertEquals(content, madeProperty.toString());
        FreeBusyTime expectedProperty = FreeBusyTime.parse("19970308T160000Z/PT8H30M")
                .withFreeBusyType(FreeBusyTypeEnum.BUSY_UNAVAILABLE);
        assertEquals(expectedProperty, madeProperty);
        Pair<ZonedDateTime, TemporalAmount> expectedValue = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 16, 0), ZoneId.of("Z")), Duration.ofHours(8).plusMinutes(30));
        assertEquals(expectedValue.getKey(), madeProperty.getValue().get(0).getKey());
        assertEquals(expectedValue.getValue(), madeProperty.getValue().get(0).getValue());
    }
    
    @Test
    public void canParseFreeBusyTime2()
    {
        String content = "FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/19970309T000000Z";
        FreeBusyTime madeProperty = FreeBusyTime.parse(content);
        String foldedContent = ICalendarUtilities.foldLine("FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/PT1H").toString();
        assertEquals(foldedContent, madeProperty.toString());
        FreeBusyTime expectedProperty = FreeBusyTime.parse("19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/19970309T000000Z")
                .withFreeBusyType(FreeBusyTypeEnum.FREE);
        assertEquals(expectedProperty, madeProperty);
        Pair<ZonedDateTime, TemporalAmount> expectedValue1 = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 16, 0), ZoneId.of("Z")), Duration.ofHours(3));
        assertEquals(expectedValue1.getKey(), madeProperty.getValue().get(0).getKey());
        assertEquals(expectedValue1.getValue(), madeProperty.getValue().get(0).getValue());
        
        Pair<ZonedDateTime, TemporalAmount> expectedValue2 = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 20, 0), ZoneId.of("Z")), Duration.ofHours(1));
        assertEquals(expectedValue2.getKey(), madeProperty.getValue().get(1).getKey());
        assertEquals(expectedValue2.getValue(), madeProperty.getValue().get(1).getValue());
        
        Pair<ZonedDateTime, TemporalAmount> expectedValue3 = new Pair<ZonedDateTime, TemporalAmount>(
                ZonedDateTime.of(LocalDateTime.of(1997, 3, 8, 23, 0), ZoneId.of("Z")), Duration.ofHours(1));
        assertEquals(expectedValue3.getKey(), madeProperty.getValue().get(2).getKey());
        assertEquals(expectedValue3.getValue(), madeProperty.getValue().get(2).getValue());
    }
}
