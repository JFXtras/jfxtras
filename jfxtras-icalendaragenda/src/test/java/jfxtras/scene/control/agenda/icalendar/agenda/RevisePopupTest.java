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
package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

@Ignore // fails
public class RevisePopupTest extends AgendaTestAbstract
{
    @Test
    public void canEditOne()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        // drag to new location
        moveTo("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);

        // edit property
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");

        // save changes to THIS AND FUTURE
        clickOn("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
        	c.getSelectionModel().select(ChangeDialogOption.ONE);
        });
        clickOn("#changeDialogOkButton");
       
        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<String> summaries = agenda.appointments().stream()
                .sorted((a1, a2) -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime()))
                .map(a -> a.getSummary())
                .collect(Collectors.toList());
        List<String> expectedSummaries = Arrays.asList(
                "Daily1 Summary",
                "Daily1 Summary",
                "new summary",
                "Daily1 Summary",
                "Daily1 Summary",
                "Daily1 Summary"
                );
        assertEquals(expectedSummaries, summaries);
        assertEquals(2, agenda.getVCalendar().getVEvents().size());
        agenda.getVCalendar().getVEvents().sort(VPrimary.DTSTART_COMPARATOR);
        VEvent vComponentOriginal = agenda.getVCalendar().getVEvents().get(0);
        VEvent vComponentIndividual = agenda.getVCalendar().getVEvents().get(1);

        assertEquals(ICalendarStaticComponents.getDaily1(), vComponentOriginal);
        VEvent expectedVComponentIndividual = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart("20151111T100000")
                .withDateTimeEnd("20151111T110000")
                .withDateTimeStamp(new DateTimeStamp(vComponentIndividual.getDateTimeStamp()))
                .withRecurrenceId("20151111T100000")
                .withRecurrenceRule((RecurrenceRule) null)
                .withSummary("new summary")
                .withSequence(1);
        assertEquals(expectedVComponentIndividual, vComponentIndividual);
    }
}
