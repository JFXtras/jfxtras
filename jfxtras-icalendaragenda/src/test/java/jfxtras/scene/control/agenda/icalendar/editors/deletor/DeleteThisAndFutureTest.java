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
package jfxtras.scene.control.agenda.icalendar.editors.deletor;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.DeleterVEvent;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.SimpleDeleterFactory;

public class DeleteThisAndFutureTest
{
    @Test
    public void canDeleteThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWeeklyZoned();
        mainVCalendar.addChild(vComponentOriginal);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withStartOriginalRecurrence(LocalDateTime.of(2016, 5, 16, 10, 0).atZone(ZoneId.of("America/Los_Angeles")))
                .delete();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTAMP:20151110T080000Z" + System.lineSeparator() +
                "DESCRIPTION:WeeklyZoned Description" + System.lineSeparator() +
                "SUMMARY:WeeklyZoned Summary" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "UID:20150110T080000-003@jfxtras.org" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "RECURRENCE-ID;TZID=America/Los_Angeles;RANGE=THISANDFUTURE:20160516T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
            String iTIPMessage = iTIPmessages.stream()
                    .map(v -> v.toString())
                    .collect(Collectors.joining(System.lineSeparator()));
            assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // makes sure when recurrence deleted the parent gets an EXDATE
    public void canDeleteThisAndFutureWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);
        final List<VEvent> vComponents = mainVCalendar.getVEvents();

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRuleValue) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 15, 10, 0);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .delete();

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "RECURRENCE-ID;RANGE=THISANDFUTURE:20160515T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPmessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
