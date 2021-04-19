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
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.agenda.AgendaTestAbstract;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

@Ignore // fails
public class PopupReviseThisAndFutureTest extends VEventPopupTestBase
{
    @Test
    public void canEditThisAndFuture()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 11, 10, 0), // start selected instance
                    LocalDateTime.of(2015, 11, 11, 11, 0), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });

       // edit property
       TextField summaryTextField = find("#summaryTextField");
       summaryTextField.setText("new summary");

       // save changes to THIS AND FUTURE
       clickOn("#saveComponentButton");
       ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
       TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.THIS_AND_FUTURE));
       clickOn("#changeDialogOkButton");

       String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
               .map(v -> v.toString())
               .collect(Collectors.joining(System.lineSeparator()));
       String dtstamp = iTIPMessage.split(System.lineSeparator())[27];
       String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toString();
       assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time
       String uid = iTIPMessage.split(System.lineSeparator())[28];
       assertFalse(uid.equals(vevent.getUniqueIdentifier().toString()));
       String expectediTIPMessage =
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:REQUEST" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151109T100000" + System.lineSeparator() +
               "DTEND:20151109T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:Daily1 Summary" + System.lineSeparator() +
               "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
               "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "RRULE:FREQ=DAILY;UNTIL=20151110T180000Z" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "SEQUENCE:1" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR" + System.lineSeparator() +
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:PUBLISH" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151111T100000" + System.lineSeparator() +
               "DTEND:20151111T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:new summary" + System.lineSeparator() +
               dtstamp + System.lineSeparator() +
               uid + System.lineSeparator() +
               "RRULE:FREQ=DAILY" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR";
       assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
