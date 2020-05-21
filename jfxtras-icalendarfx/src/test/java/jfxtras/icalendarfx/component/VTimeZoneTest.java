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
package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VTimeZone;

public class VTimeZoneTest
{
    @Test
    public void canBuildVTimeZone() throws InstantiationException, IllegalAccessException
    {
        VTimeZone builtComponent = new VTimeZone()
                .withTimeZoneIdentifier("America/Los_Angeles");

        String componentName = builtComponent.name();            
        String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                "TZID:America/Los_Angeles" + System.lineSeparator() +
                "END:" + componentName;
                
        VComponent parsedComponent = builtComponent.getClass().newInstance();
        parsedComponent.addChild(expectedContent);

        assertEquals(parsedComponent, builtComponent);
        assertEquals(expectedContent, builtComponent.toString());            
    }
    
    @Test
    public void canParseVTimeZone()
    {
        String expectedContent = 
            "BEGIN:VTIMEZONE" + System.lineSeparator() +
            "TZID:America/New_York" + System.lineSeparator() +
            "LAST-MODIFIED:20050809T050000Z" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 1
            "DTSTART:19670430T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z" + System.lineSeparator() +            
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:STANDARD" + System.lineSeparator() + // 2
            "DTSTART:19671029T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z" + System.lineSeparator() +
            "TZOFFSETFROM:-0400" + System.lineSeparator() +
            "TZOFFSETTO:-0500" + System.lineSeparator() +
            "TZNAME:EST" + System.lineSeparator() +
            "END:STANDARD" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 3
            "DTSTART:19740106T020000" + System.lineSeparator() +
            "RDATE:19750223T020000" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 4
            "DTSTART:19760425T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 5
            "DTSTART:19870405T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 6
            "DTSTART:20070311T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:STANDARD" + System.lineSeparator() + // 7
            "DTSTART:20071104T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU" + System.lineSeparator() +
            "TZOFFSETFROM:-0400" + System.lineSeparator() +
            "TZOFFSETTO:-0500" + System.lineSeparator() +
            "TZNAME:EST" + System.lineSeparator() +
            "END:STANDARD" + System.lineSeparator() +
            "END:VTIMEZONE";
        VTimeZone component = VTimeZone.parse(expectedContent);
        VTimeZone builtComponent = ICalendarTestAbstract.getTimeZone1();
        assertEquals(component, builtComponent);
        assertEquals(expectedContent, component.toString());
        assertEquals(builtComponent.toString(), component.toString());
    }
}
