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

import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ByWeekNumberTest
{

    @Test
    public void canParseByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber(4,5);
        assertEquals(Arrays.asList(4,5), element.getValue());
        assertEquals("BYWEEKNO=4,5", element.toString());
    }

    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=20
     */
    @Test
    public void canStreamByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber(20);
        LocalDateTime dateTimeStart = LocalDateTime.of(1997, 5, 12, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1997, 5, 12, 10, 0)
              , LocalDateTime.of(1997, 5, 13, 10, 0)
              , LocalDateTime.of(1997, 5, 14, 10, 0)
              , LocalDateTime.of(1997, 5, 15, 10, 0)
              , LocalDateTime.of(1997, 5, 16, 10, 0)
              , LocalDateTime.of(1997, 5, 17, 10, 0)
              , LocalDateTime.of(1997, 5, 18, 10, 0)
              , LocalDateTime.of(1998, 5, 11, 10, 0)
              , LocalDateTime.of(1998, 5, 12, 10, 0)
              , LocalDateTime.of(1998, 5, 13, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(10).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=53
     */
    @Test
    public void canStreamByWeekNumber2()
    {
        ByWeekNumber element = new ByWeekNumber(53);
        LocalDateTime dateTimeStart = LocalDateTime.of(1997, 12, 29, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1997, 12, 29, 10, 0)
              , LocalDateTime.of(1997, 12, 30, 10, 0)
              , LocalDateTime.of(1997, 12, 31, 10, 0)
              , LocalDateTime.of(1998, 12, 28, 10, 0)
              , LocalDateTime.of(1998, 12, 29, 10, 0)
              , LocalDateTime.of(1998, 12, 30, 10, 0)
              , LocalDateTime.of(1998, 12, 31, 10, 0)
              , LocalDateTime.of(2001, 12, 31, 10, 0)
              , LocalDateTime.of(2002, 12, 30, 10, 0)
              , LocalDateTime.of(2002, 12, 31, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=-53
     */
    @Test
    public void canStreamByWeekNumber3()
    {
        ByWeekNumber element = new ByWeekNumber(-53);
        LocalDateTime dateTimeStart = LocalDateTime.of(1997, 1, 29, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1998, 1, 1, 10, 0)
              , LocalDateTime.of(1998, 1, 2, 10, 0)
              , LocalDateTime.of(1998, 1, 3, 10, 0)
              , LocalDateTime.of(1998, 1, 4, 10, 0)
              , LocalDateTime.of(1999, 1, 1, 10, 0)
              , LocalDateTime.of(1999, 1, 2, 10, 0)
              , LocalDateTime.of(1999, 1, 3, 10, 0)
              , LocalDateTime.of(2000, 1, 1, 10, 0)
              , LocalDateTime.of(2000, 1, 2, 10, 0)
              , LocalDateTime.of(2004, 1, 1, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:19970512T100000
    RRULE:FREQ=YEARLY;BYWEEKNO=-1
     */
    @Test
    public void canStreamByWeekNumber4()
    {
        ByWeekNumber element = new ByWeekNumber(-1);
        LocalDateTime dateTimeStart = LocalDateTime.of(1998, 12, 28, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1998, 12, 28, 10, 0)
              , LocalDateTime.of(1998, 12, 29, 10, 0)
              , LocalDateTime.of(1998, 12, 30, 10, 0)
              , LocalDateTime.of(1998, 12, 31, 10, 0)
              , LocalDateTime.of(1999, 12, 27, 10, 0)
              , LocalDateTime.of(1999, 12, 28, 10, 0)
              , LocalDateTime.of(1999, 12, 29, 10, 0)
              , LocalDateTime.of(1999, 12, 30, 10, 0)
              , LocalDateTime.of(1999, 12, 31, 10, 0)
              , LocalDateTime.of(2000, 12, 25, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream
                .filter(r -> ! DateTimeUtilities.isBefore(r, dateTimeStart)) // filter is normally done in streamRecurrences in RecurrenceRule2
                .limit(10)
                .collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test
    public void canCatchInvalidByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber();
        element.getValue().add(999); // invalid element
        assertEquals(1, element.errors().size());
        String expected = "Out of range BYWEEKNO value: 999";
		assertEquals(expected, element.errors().get(0));
    }

}
