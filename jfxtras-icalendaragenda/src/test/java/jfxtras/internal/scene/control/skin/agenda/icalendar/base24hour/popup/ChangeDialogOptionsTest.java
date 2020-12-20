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
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;

@Ignore // fails with NPE
public class ChangeDialogOptionsTest extends VEventPopupTestBase
{
    @Test
    public void describleChangeProducesThreeOptions()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    categories());
        });

        // Get properties
        TextField summaryTextField = find("#summaryTextField");
        
        // Make changes
        summaryTextField.setText("new summary");
        clickOn("#saveComponentButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        List<ChangeDialogOption> expectedItems = Arrays.asList(ChangeDialogOption.ONE, ChangeDialogOption.THIS_AND_FUTURE, ChangeDialogOption.ALL);
        assertEquals(expectedItems , comboBox.getItems());
        clickOn("#changeDialogCancelButton");
        clickOn("#cancelComponentButton");
    }
    
    @Test
    public void repeatChangeProducesTwoOptions()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    categories());
        });

        clickOn("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
                
        // Make changes
        clickOn("#saveRepeatButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        List<ChangeDialogOption> expectedItems = Arrays.asList(ChangeDialogOption.THIS_AND_FUTURE, ChangeDialogOption.ALL);
        assertEquals(expectedItems , comboBox.getItems());
        clickOn("#changeDialogCancelButton");
        clickOn("#cancelRepeatButton");
    }
}
