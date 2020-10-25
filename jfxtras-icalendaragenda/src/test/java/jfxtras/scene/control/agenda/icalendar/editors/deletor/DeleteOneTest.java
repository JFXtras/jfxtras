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

public class DeleteOneTest
{
    @Test // deletes one instance of a repeating event
    public void canDeleteOneInstance()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.ONE)
                .withStartOriginalRecurrence(LocalDateTime.of(2016, 5, 16, 10, 0))
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
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
            String iTIPMessage = iTIPmessages.stream()
                    .map(v -> v.toString())
                    .collect(Collectors.joining(System.lineSeparator()));
            assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // Deletes VEvent with RECURRENCE-ID
    public void canDeleteRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addChild(vComponentOriginal);
        final List<VEvent> vComponents = mainVCalendar.getVEvents();
        
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRuleValue) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);

        List<VCalendar> iTIPmessages = ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentRecurrence))
                .withDialogCallback((m) -> null) // deletes recurrence - its an individual so no dialog needed
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .delete();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20160517T083000" + System.lineSeparator() +
//                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
            String iTIPMessage = iTIPmessages.stream()
                    .map(v -> v.toString())
                    .collect(Collectors.joining(System.lineSeparator()));
            assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
