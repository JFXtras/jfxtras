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

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;

public class ByYearDayTest
{
    @Test
    public void canParseByYearDay()
    {
        ByYearDay element = new ByYearDay(100,200,300);
        assertEquals(Arrays.asList(100,200,300), element.getValue());
        assertEquals("BYYEARDAY=100,200,300", element.toString());
    }
    
    /*
    DTSTART:20160104T000000
    RRULE:FREQ=HOURLY;BYYEARDAY=-2,4
     */
    @Test
    public void canStreamByYearDay()
    {
        ByYearDay element = new ByYearDay(4,-2);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 0, 0);
        ChronoUnit frequency = ChronoUnit.HOURS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(8, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 0, 0)
              , LocalDateTime.of(2016, 1, 4, 8, 0)
              , LocalDateTime.of(2016, 1, 4, 16, 0)
              , LocalDateTime.of(2016, 12, 30, 0, 0)
              , LocalDateTime.of(2016, 12, 30, 8, 0)
              , LocalDateTime.of(2016, 12, 30, 16, 0)
              , LocalDateTime.of(2017, 1, 4, 0, 0)
              , LocalDateTime.of(2017, 1, 4, 8, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(8).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYYEARDAY=-366
     */
    @Test
    public void canStreamByYearDay2()
    {
        ByYearDay element = new ByYearDay(-366);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 1, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 1, 10, 0)
              , LocalDateTime.of(2020, 1, 1, 10, 0)
              , LocalDateTime.of(2024, 1, 1, 10, 0)
              , LocalDateTime.of(2028, 1, 1, 10, 0)
              , LocalDateTime.of(2032, 1, 1, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYYEARDAY=366
     */
    @Test
    public void canStreamByYearDay3()
    {
        ByYearDay element = new ByYearDay(366);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 12, 31, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 12, 31, 10, 0)
              , LocalDateTime.of(2020, 12, 31, 10, 0)
              , LocalDateTime.of(2024, 12, 31, 10, 0)
              , LocalDateTime.of(2028, 12, 31, 10, 0)
              , LocalDateTime.of(2032, 12, 31, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test  (expected = IllegalArgumentException.class)
    public void canCatchOutOfRangeByYearDay()
    {
        new ByYearDay(1100,200,300);
    }
}
