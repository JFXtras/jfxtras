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
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.EditChoiceDialog;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;

/** 
 * TabPane for editing descriptive properties and a {@link RecurrenceRule} for a {@link VComponentLocatable}.
 * 
 * @author David Bal
 * 
 * @param <T> subclass of {@link VComponentLocatable}
 * @param <U> subclass of {@link EditDescriptiveVBox} associated with the subclass of {@link VComponentLocatable}
 */
public abstract class EditLocatableTabPane<T extends VLocatable<T>> extends EditDisplayableTabPane<T, EditDescriptiveLocatableVBox<T>>
{
    public EditLocatableTabPane( )
    {
        super();
    }
        
    @Override
    @FXML void handleSaveButton()
    {
        super.handleSaveButton();
        if (vComponentCopy.getRecurrenceRule() != null)
        {
            RecurrenceRuleValue rrule = vComponentCopy.getRecurrenceRule().getValue();
            if (rrule.getFrequency().getValue() == FrequencyType.WEEKLY)
            {
                if (recurrenceRuleVBox.dayOfWeekList.isEmpty())
                {
                    canNotHaveZeroDaysOfWeek();
                    return; // skip other operations, allow user to make changes and try again
                }
            }
        }
        Object[] params = new Object[] {
                EditChoiceDialog.EDIT_DIALOG_CALLBACK,
                editDescriptiveVBox.endNewRecurrence,
                editDescriptiveVBox.startOriginalRecurrence,
                editDescriptiveVBox.startRecurrenceProperty.get(),
                vComponentCopy,
                vComponentOriginal
        };
        List<VCalendar> result = SimpleRevisorFactory.newReviser(vComponentCopy, params).revise();
        iTIPMessagesProperty().set(result);
    }
    
    @Override
    void removeEmptyProperties()
    {
        super.removeEmptyProperties();
        if (editDescriptiveVBox.descriptionTextArea.getText().isEmpty())
        {
            vComponentCopy.setDescription((Description) null); 
        }
        if (editDescriptiveVBox.locationTextField.getText().isEmpty())
        {
            vComponentCopy.setLocation((Location) null); 
        }
    }
    
    // Displays an alert notifying at least one day of week must be present for weekly frequency
    private static void canNotHaveZeroDaysOfWeek()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Please select at least one day of the week.");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("zero_day_of_week_alert");
        alert.getDialogPane().lookupButton(buttonTypeOk).setId("zero_day_of_week_alert_button_ok");
        
        alert.showAndWait();
    }
}
