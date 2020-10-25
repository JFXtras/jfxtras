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
package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.icalendarfx.utilities.DateTimeUtilities;

//import net.balsoftware.utilities.DateTimeUtilities;

public class ParseDateTest
{
    @Test
    public void canParseDate1()
    {
        String value = "DTSTART;TZID=America/Los_Angeles:20160228T070000";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 2, 28, 7, 0), ZoneId.of("America/Los_Angeles")), t);
    }
    
    @Test
    public void canParseDate2()
    {
        String value = "TZID=Etc/GMT:20160306T080000Z";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 8, 0), ZoneId.of("Z")), t);
    }
    
    @Test
    public void canParseDate3()
    {
        String value = "20160306T080000Z";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 8, 0), ZoneId.of("Z")), t);
    }
    
    @Test
    public void canParseDate4()
    {
        String value = "DTSTART;VALUE=DATE:20160307";
        Temporal t = DateTimeUtilities.temporalFromString(value);
        assertEquals(LocalDate.of(2016, 3, 7), t);
    }    
   

}


