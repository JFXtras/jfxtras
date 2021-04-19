/**
 * Copyright (c) 2011-2021, JFXtras
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
package jfxtras.scene.control.agenda.icalendar.editors.revisor;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.ReviserVEvent;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;

public class ReviseNonRepeatingTest
{
    @Test
    public void canEditIndividual()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160516T113000" + System.lineSeparator() +
                "DURATION:PT1H" + System.lineSeparator() +
                "DESCRIPTION:Individual Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-007@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canEditIndividual2() // with other components present
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        mainVCalendar.addChild(vComponentOriginal);
        final List<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        vComponents.add(ICalendarStaticComponents.getDaily1());

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160516T113000" + System.lineSeparator() +
                "DURATION:PT1H" + System.lineSeparator() +
                "DESCRIPTION:Individual Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-007@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canEditIndividual3() // delete property
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        mainVCalendar.addChild(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary((Summary) null);
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentCopyEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160516T113000" + System.lineSeparator() +
                "DURATION:PT1H" + System.lineSeparator() +
                "DESCRIPTION:Individual Description" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                vComponentEdited.getDateTimeStamp().toString() + System.lineSeparator() +
                "UID:20150110T080000-007@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
