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
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;

public class BySecondTest
{
    @Test
    public void canParseBySecond()
    {
        BySecond element = new BySecond(10,20);
        assertEquals(Arrays.asList(10,20), element.getValue());
        assertEquals("BYSECOND=10,20", element.toString());
    }
    
    /*
    DTSTART:20160101T100000
    RRULE:FREQ=SECONDLY;BYSECOND=10,15,20
     */
    @Test
    public void canStreamBySecond()
    {
        BySecond element = new BySecond(10,15,20);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 1, 10, 10);
        ChronoUnit frequency = ChronoUnit.SECONDS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 1, 10, 10, 10)
              , LocalDateTime.of(2016, 1, 1, 10, 10, 15)
              , LocalDateTime.of(2016, 1, 1, 10, 10, 20)
              , LocalDateTime.of(2016, 1, 1, 10, 11, 10)
              , LocalDateTime.of(2016, 1, 1, 10, 11, 15)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=DAILY;BYSECOND=1,2,3
     */
    @Test
    public void canStreamBySecond2()
    {
        BySecond element = new BySecond(10,20);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 10);
        ChronoUnit frequency = ChronoUnit.DAYS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 10, 10)
              , LocalDateTime.of(2016, 5, 5, 10, 10, 20)
              , LocalDateTime.of(2016, 5, 6, 10, 10, 10)
              , LocalDateTime.of(2016, 5, 6, 10, 10, 20)
              , LocalDateTime.of(2016, 5, 7, 10, 10, 10)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test  (expected = IllegalArgumentException.class)
    public void canCatchOutOfRangeBySecond()
    {
        new BySecond(1100,200,300);
    }
}
