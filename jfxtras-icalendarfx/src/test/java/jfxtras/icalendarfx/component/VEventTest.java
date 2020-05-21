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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Test following components:
 * @see VEvent
 * 
 * for the following properties:
 * @see TimeTransparency
 * 
 * @author David Bal
 *
 */
public class VEventTest extends ICalendarTestAbstract
{
    @Test
    public void canBuildVEvent()
    {
        VEvent builtComponent = new VEvent()
                .withDateTimeEnd(LocalDate.of(1997, 3, 1))
                .withTimeTransparency(TimeTransparencyType.OPAQUE);
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DTEND;VALUE=DATE:19970301" + System.lineSeparator() +
                "TRANSP:OPAQUE" + System.lineSeparator() +
                "END:" + componentName;
                
        VEvent madeComponent = VEvent.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toString());
    }
        
    @Test
    public void canCatchBothDurationAndDTEnd()
    {
        VEvent v = new VEvent()
                .withDateTimeEnd(LocalDate.of(1997, 3, 1))
                .withDuration(Duration.ofMinutes(30));
        String expectedError = "Both DTEND and DURATION are present.  DTEND or DURATION MAY exist, but both MUST NOT occur in the same VEVENT";
        boolean isErrorPresent = v.errors()
        	.stream()
        	.anyMatch(e -> e.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canCatchBothDurationAndDTEnd2()
    {
        VEvent v = new VEvent()
                .withDuration(Duration.ofMinutes(30))
                .withDateTimeEnd(LocalDate.of(1997, 3, 1))
                ;
        String expectedError = "Both DTEND and DURATION are present.  DTEND or DURATION MAY exist, but both MUST NOT occur in the same VEVENT";
        boolean isErrorPresent = v.errors()
        	.stream()
        	.anyMatch(e -> e.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canChangeDTEndToDuration()
    {
        VEvent builtComponent = new VEvent()
             .withDateTimeEnd(LocalDate.of(1997, 3, 1));
        assertEquals(LocalDate.of(1997, 3, 1), builtComponent.getDateTimeEnd().getValue());
        builtComponent.setDateTimeEnd((DateTimeEnd) null);
        builtComponent.withDateTimeEnd((DateTimeEnd) null).withDuration("PT15M");
        assertEquals(Duration.ofMinutes(15), builtComponent.getDuration().getValue());
        assertNull(builtComponent.getDateTimeEnd());
    }
    
    
    @Test
    public void canStreamWithEnd()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 2, 0))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withCount(6)
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
              , LocalDateTime.of(2015, 11, 24, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 15, 22, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canStreamWithRange()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withDuration(Duration.ofHours(6))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 14, 20, 0), 
                                                           LocalDateTime.of(2015, 11, 22, 0, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }

//    /** use {@link VComponentBase#copyComponentFrom} */
    @Test
    public void canCopyComponent()
    {
        VEvent e = new VEvent().withSummary("Test").withRecurrenceRule("RRULE:FREQ=DAILY");
        VEvent e2 = new VEvent(e);
        assertEquals(e, e2);
    }

    // Use copy constructor
    @Test
    public void canCopyComponent2()
    {
        VEvent e = getYearly1();
        VEvent e2 = new VEvent(e);
        assertEquals(e, e2);
    }

    @Test
    public void canChangeLocalDateToLocalDateTime()
    {
        VEvent vEvent = new VEvent()
                .withDateTimeEnd(LocalDate.of(2016, 3, 7))
                .withDateTimeStart(LocalDate.of(2016, 3, 6));
        vEvent.setDateTimeStart(LocalDateTime.of(2016, 3, 6, 4, 30));// NOTE: this allows a temporary invalid state
        String expectedError = "DTEND value type (DATE) must be the same value type as DTSTART (DATE_WITH_LOCAL_TIME)";
        boolean isErrorPresent = vEvent.errors().stream().anyMatch(s -> s.equals(expectedError));
        assertTrue(isErrorPresent);
    }

    @Test
    public void canCatchTooEarlyDTEND()
    {
        VEvent vEvent = new VEvent()
                .withDateTimeEnd(LocalDate.of(2016, 3, 5))
                .withDateTimeStart(LocalDate.of(2016, 3, 6)); // makes DTEND too early
        String expectedError = "DTEND does not occur after DTSTART.  DTEND MUST occur after DTSTART (2016-03-05, 2016-03-06)";
        boolean isErrorPresent = vEvent.errors()
        		.stream()
        		.anyMatch(s -> s.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canCatchTooEarlyDTEND2()
    {
        VEvent vEvent = new VEvent()
                .withDateTimeStart(LocalDate.of(2016, 3, 6));
        vEvent.setDateTimeEnd(LocalDate.of(2016, 3, 5));
        String expectedError = "DTEND does not occur after DTSTART.  DTEND MUST occur after DTSTART (2016-03-05, 2016-03-06)";
        boolean isErrorPresent = vEvent.errors()
        	.stream()
        	.anyMatch(e -> e.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canCatchWrongDTENDType()
    {
        VEvent vEvent = new VEvent()
                .withDateTimeStart(LocalDate.of(2016, 3, 6));
        vEvent.setDateTimeEnd(LocalDateTime.of(2016, 3, 6, 4, 30));
        String expectedError = "DTEND value type (" + DateTimeType.DATE_WITH_LOCAL_TIME + ") must be the same value type as DTSTART (" + DateTimeType.DATE + ")";
        boolean isErrorPresent = vEvent.errors()
        	.stream()
        	.anyMatch(e -> e.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
}