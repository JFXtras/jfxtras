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
