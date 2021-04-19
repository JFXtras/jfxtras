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

import java.time.temporal.Temporal;
import java.util.List;

import javafx.fxml.FXML;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.EditChoiceDialog;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.SimpleRevisorFactory;

/** 
 * TabPane for editing descriptive properties and a {@link RecurrenceRule} for a {@link VJournal}.
 * 
 * @author David Bal
 */
public class EditVJournalTabPane extends EditDisplayableTabPane<VJournal, EditDescriptiveVJournalVBox>
{
    public EditVJournalTabPane( )
    {
        super();
        editDescriptiveVBox = new EditDescriptiveVJournalVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new EditRecurrenceRuleVJournalVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    @FXML void handleSaveButton()
    {
        super.handleSaveButton();        
        Object[] params = new Object[] {
                vComponentOriginal,
                EditChoiceDialog.EDIT_DIALOG_CALLBACK,
                editDescriptiveVBox.startOriginalRecurrence,
                editDescriptiveVBox.startRecurrenceProperty.get(),
                vComponentCopy,
                vComponentOriginal
        };
        List<VCalendar> result = SimpleRevisorFactory.newReviser(vComponentCopy, params).revise();
        iTIPMessagesProperty().set(result);
//        List<VJournal> result = (List<VJournal>) SimpleRevisorFactory.newReviser(vComponent, params).revise();
//        newVComponentsProperty().set(result);
//        isFinished.set(result);
   }
    
    @Override
    void removeEmptyProperties()
    {
        if (editDescriptiveVBox.descriptionTextArea.getText().isEmpty())
        {
            vComponentCopy.setDescriptions(null);
        }
    }
    
    @Override
    public void setupData(
            VJournal vComponentOriginal,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        this.vComponentOriginal = vComponentOriginal;
        vComponentCopy = new VJournal(vComponentOriginal);
        super.setupData(vComponentCopy, startRecurrence, endRecurrence, categories);
//        vComponentOriginal = new VJournal(vComponent);
    }
}
