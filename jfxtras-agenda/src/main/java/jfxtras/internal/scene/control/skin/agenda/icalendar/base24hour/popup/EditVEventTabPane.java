package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** 
 * TabPane for editing descriptive properties and a {@link RecurrenceRule} for a {@link VEvent}.
 * 
 * @author David Bal
 */
public class EditVEventTabPane extends EditLocatableTabPane<VEvent>
{
    public EditVEventTabPane( )
    {
        super();
        editDescriptiveVBox = new EditDescriptiveVEventVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new EditRecurrenceRuleVEventVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    public void setupData(
            VEvent vComponentOriginal,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        this.vComponentOriginal = vComponentOriginal;
        vComponentCopy = new VEvent(vComponentOriginal);
        super.setupData(vComponentCopy, startRecurrence, endRecurrence, categories);
    }
}
