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
package jfxtras.icalendarfx.parameter.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySetPosition;

public class BySetPositionTest
{
    @Test
    public void canParseSetPosition()
    {
        BySetPosition element = new BySetPosition(-2,5);
        assertEquals(Arrays.asList(-2,5), element.getValue());
        assertEquals("BYSETPOS=-2,5", element.toString());
    }
    
    @Test
    public void canStreamSetPosition()
    {
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 0, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        List<Temporal> list = Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 0, 0), 
                LocalDateTime.of(2016, 1, 5, 0, 0),
                LocalDateTime.of(2016, 1, 6, 0, 0)
                );
        
        BySetPosition element = new BySetPosition(2);        
        Stream<Temporal> recurrenceStream = element.streamRecurrences(list.stream(), frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 5, 0, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(8).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test
    public void canStreamSetPosition2()
    {
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 0, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        List<Temporal> list = Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 0, 0), 
                LocalDateTime.of(2016, 1, 5, 0, 0),
                LocalDateTime.of(2016, 1, 6, 0, 0)
                );
        
        BySetPosition element = new BySetPosition(-1);        
        Stream<Temporal> recurrenceStream = element.streamRecurrences(list.stream(), frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 6, 0, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(8).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
