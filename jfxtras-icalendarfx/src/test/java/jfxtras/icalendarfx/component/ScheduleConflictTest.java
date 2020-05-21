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

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

public class ScheduleConflictTest extends ICalendarTestAbstract
{
    @Test // starts at same time and day
    public void canDetectScheduleConflict1()
    {
        List<VEvent> list = Arrays.asList(getYearly1());
        VEvent vEvent = getDaily1();
        String conflict = DateTimeUtilities.checkScheduleConflict(vEvent, list);
        assertEquals("20151109T082900-0@jfxtras.org, 20151109T100000", conflict);
    }
    
    @Test // overlaps about a year in future
    public void canDetectScheduleConflict2()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20161109T100000", conflict);
    }
    
    @Test // multiple events with a TRANSPARENT one that must be ignored to be correct
    public void canDetectScheduleConflict3()
    {
        VEvent existingVEvent1 = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        VEvent existingVEvent2 = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2016, 5, 9, 9, 0))
                .withDuration(Duration.ofHours(3))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(5));
        VEvent existingVEvent3 = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2016, 5, 9, 7, 0))
                .withDuration(Duration.ofHours(1))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        VEvent existingVEvent4 = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2016, 1, 1, 10, 0))
                .withDuration(Duration.ofHours(1))
                .withTimeTransparency(TimeTransparencyType.TRANSPARENT)
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        List<VEvent> list = Arrays.asList(existingVEvent1, existingVEvent2, existingVEvent3, existingVEvent4);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 27, 10, 30))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.FRIDAY)));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20160603T090000", conflict);
    }
        
    @Test // starts before, ends in middle
    public void canDetectScheduleConflict5()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 30))
                .withDuration(Duration.ofHours(1))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20161109T100000", conflict);
    }
    
    @Test // starts in middle, ends in middle
    public void canDetectScheduleConflict6()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withDuration(Duration.ofMinutes(20))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20161109T100000", conflict);
    }
    
    @Test // starts in middle, ends outside
    public void canDetectScheduleConflict7()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withDuration(Duration.ofHours(1))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20161109T100000", conflict);
    }
    
    @Test // test individual conflict
    public void canDetectScheduleConflict8()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.DAILY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 12, 1, 10, 30))
                .withDuration(Duration.ofHours(1));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20151201T100000", conflict);
    }

    
    @Test // starts before, ends outside
    public void canDetectScheduleConflict9()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 30))
                .withDuration(Duration.ofHours(2))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20161109T100000", conflict);
    }
    
    @Test // individual event conflict
    public void canDetectScheduleConflict10()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.DAILY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2016, 5, 1, 10, 0))
                .withDuration(Duration.ofHours(1));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertEquals("20160501T100000", conflict);
    }
    
    @Test // return null when no conflict
    public void canDetectNoScheduleConflict()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 7, 30))
                .withDuration(Duration.ofHours(1))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertNull(conflict);
    }
    
    @Test // end when existing starts
    public void canDetectNoScheduleConflict2()
    {
        VEvent existingVEvent = new VEvent()
            .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
            .withDuration(Duration.ofHours(1))
            .withRecurrenceRule(new RecurrenceRuleValue()
                    .withFrequency(FrequencyType.YEARLY));
        List<VEvent> list = Arrays.asList(existingVEvent);
        VEvent newVEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDuration(Duration.ofHours(1))
                .withRecurrenceRule(new RecurrenceRuleValue()
                        .withFrequency(FrequencyType.DAILY));
        String conflict = DateTimeUtilities.checkScheduleConflict(newVEvent, list);
        assertNull(conflict);
    }

    
//    @Test
//    public void canDetectScheduleConflictDuringImport()
//    {
//        VCalendar c = new VCalendar();
//        c.addVComponent(getDaily1());
//        String content = 
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "DTSTART:20160406T100000" + System.lineSeparator() +
//                "DTEND:20160406T123000" + System.lineSeparator() +
//                "END:VEVENT";
//        VEvent newVComponent = (VEvent) c.importVComponent(content);
//        assertEquals(1, c.getVEvents().size()); // show imported component wasn't accepted
//        assertEquals("4.1;Event conflict with 20150110T080000-0@jfxtras.org, 20160406T100000", newVComponent.getRequestStatus().get(0).getValue());
//    }
}
