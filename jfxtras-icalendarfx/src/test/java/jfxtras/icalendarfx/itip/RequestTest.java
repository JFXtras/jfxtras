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
package jfxtras.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarStaticComponents;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

public class RequestTest
{
    @Test
    public void canShiftWeeklyAll() // shift day of weekly
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWeekly3();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() + // one day later
                "DTEND:20151110T103000" + System.lineSeparator() + // one day later, half hour longer
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=TU" + System.lineSeparator() + // Changed Monday to Tuesday
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        VEvent expectedVComponent = ICalendarStaticComponents.getWeekly3()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRuleValue()
                                .withFrequency(FrequencyType.WEEKLY)
                                .withByRules(new ByDay(DayOfWeek.TUESDAY)))
                .withSummary("Edited summary")
                .withSequence(1);
        assertEquals(expectedVComponent, myComponent);
    }
    
    @Test
    public void canShiftMonthlyAll() // shift day of week with monthly ordinal
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getMonthly7();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() + // one day later
                "DTEND:20151110T103000" + System.lineSeparator() + // one day later, half hour longer
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=MONTHLY;BYDAY=3TU" + System.lineSeparator() + // Changed Monday to Tuesday
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        VEvent expectedVComponent = ICalendarStaticComponents.getWeekly3()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRuleValue()
                                .withFrequency(FrequencyType.MONTHLY)
                                .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.TUESDAY, 3))))
                .withSummary("Edited summary")
                .withSequence(1);
        assertEquals(expectedVComponent, myComponent);
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR"
                ;
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent vComponentExpected = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDate.of(2015, 11, 8))
                .withDateTimeEnd(LocalDate.of(2015, 11, 9))
                .withSummary("Edited summary")
                .withSequence(1);
        
        assertEquals(vComponentExpected, vComponents.get(0));
    }
    
    @Test
    public void canRemoveExceptionDate()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDailyWithException1();
        final List<VEvent> vComponents = new ArrayList<>(Arrays.asList(vComponentOriginal));
        mainVCalendar.setVEvents(vComponents);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group03" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T113000" + System.lineSeparator() +
                "DESCRIPTION:Daily2 Description" + System.lineSeparator() +
                "SUMMARY:Daily2 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-005@jfxtras.org" + System.lineSeparator() +
                "RRULE:COUNT=6;FREQ=DAILY;INTERVAL=3" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR"
                ;
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent vComponentExpected = ICalendarStaticComponents.getDaily2()
                .withSequence(1);
        
        assertEquals(vComponentExpected, vComponents.get(0));
    }
}
