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

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

public class RRuleErrorTest
{
    @Test
    public void canDetectIntervalError()
    {
        RecurrenceRuleValue rrule = new RecurrenceRuleValue()
                .withFrequency(FrequencyType.YEARLY)
                .withInterval(0); // invalid
        assertEquals(1, rrule.errors().size());
    }

    @Test
    public void canDetectMissingFrequency()
    {
        RecurrenceRuleValue rrule = new RecurrenceRuleValue();
        assertEquals(1, rrule.errors().size());
    }
    
    @Test
    public void canDetectCountAndUntilFrequency()
    {
        RecurrenceRuleValue rrule = new RecurrenceRuleValue()
                .withFrequency(FrequencyType.YEARLY)
                .withCount(10)
                .withUntil("19970610T172345Z"); // invalid
        assertEquals(1, rrule.errors().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void canDetectDuplicateByRule()
    {
        RecurrenceRuleValue.parse("FREQ=WEEKLY;BYDAY=TU;BYDAY=TU");
    }

    @Test (expected = IllegalArgumentException.class)
    public void canDetectDuplicateByRule2()
    {
    	RecurrenceRuleValue.parse("FREQ=WEEKLY;BYDAY=TU;BYDAY=FR");
    }

    @Test
    public void canDetectDuplicateByRule3()
    {
        RecurrenceRuleValue rrule = RecurrenceRuleValue.parse("FREQ=WEEKLY;BYMONTH=1;BYDAY=TU");
        rrule.getByRules().add(new ByDay(DayOfWeek.FRIDAY));
        List<String> expectedErrors = Arrays.asList("RRULE:ByDay can only occur once in a RRULE.");
        assertEquals(expectedErrors, rrule.errors());
    }
}
