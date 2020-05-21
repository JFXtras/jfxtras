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
package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;

public class RecurrenceIDParentValidTest
{
    @Test
    public void canCatchWrongRecurrenceIdType()
    {
        VCalendar c = new VCalendar();
        VEvent parentComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        VEvent childComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016");
        c.addChild(parentComponent);
        c.addChild(childComponent);
        childComponent.withRecurrenceId(new RecurrenceId(LocalDateTime.of(2016, 3, 6, 8, 0)));
        String expectedError = "RECURRENCE-ID:RecurrenceId DateTimeType (DATE_WITH_LOCAL_TIME) must be same as the type of its parent's DateTimeStart (DATE)";
        boolean isErrorPresent = c.errors().stream().anyMatch(e -> e.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canCatchWrongRecurrenceIdType2()
    {
        VEvent parentComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        VEvent childComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withRecurrenceId(LocalDate.of(1997, 3, 1));
        VCalendar c = new VCalendar()
        		.withVEvents(parentComponent, childComponent);
        parentComponent.setDateTimeStart(LocalDateTime.of(1997, 4, 1, 8, 0));
        String errorPrefix = VPropertyElement.RECURRENCE_IDENTIFIER.toString();
        boolean hasError = childComponent.errors().stream()
            .anyMatch(s -> s.substring(0, errorPrefix.length()).equals(errorPrefix));
        assertTrue(hasError);
    }
}
