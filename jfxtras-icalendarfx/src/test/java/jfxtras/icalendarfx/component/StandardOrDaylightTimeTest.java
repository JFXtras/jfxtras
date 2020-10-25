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
package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardOrDaylight;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;

public class StandardOrDaylightTimeTest
{
    @Test
    public void canBuildStandardOrDaylight() throws InstantiationException, IllegalAccessException
    {
        List<StandardOrDaylight<?>> components = Arrays.asList(
                new DaylightSavingTime()
                    .withTimeZoneOffsetFrom(ZoneOffset.ofHours(-4))
                    .withTimeZoneOffsetTo(ZoneOffset.ofHours(-5))
                    .withTimeZoneNames("TZNAME;LANGUAGE=fr-CA:HNE"),
                new StandardTime()
                    .withTimeZoneOffsetFrom(ZoneOffset.ofHours(-4))
                    .withTimeZoneOffsetTo(ZoneOffset.ofHours(-5))
                    .withTimeZoneNames("TZNAME;LANGUAGE=fr-CA:HNE")
                );
        
        for (StandardOrDaylight<?> builtComponent : components)
        {
            String componentName = builtComponent.name();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "TZOFFSETFROM:-0400" + System.lineSeparator() +
                    "TZOFFSETTO:-0500" + System.lineSeparator() +
                    "TZNAME;LANGUAGE=fr-CA:HNE" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.addChild(expectedContent);

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toString());            
        }
    }    
    
    @Test
    public void canStreamWithRange()
    {
        StandardTime e = new StandardTime()
                .withDateTimeStart("19961027T020000")
                .withRecurrenceRule(RecurrenceRuleValue.parse("FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU"));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1996, 10, 27, 2, 0)
              , LocalDateTime.of(1997, 10, 26, 2, 0)
              , LocalDateTime.of(1998, 10, 25, 2, 0)
              , LocalDateTime.of(1999, 10, 31, 2, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(1996, 1, 1, 0, 0), 
                                                           LocalDateTime.of(2000, 1, 1, 0, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
}
