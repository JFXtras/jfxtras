package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** 
 * TabPane for editing descriptive properties and a {@link RecurrenceRule} for a {@link VTodo}.
 * 
 * @author David Bal
 */
public class EditVTodoTabPane extends EditLocatableTabPane<VTodo>
{
    public EditVTodoTabPane( )
    {
        super();
        editDescriptiveVBox = new EditDescriptiveVTodoVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new EditRecurrenceRuleVTodoVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    public void setupData(
            VTodo vComponentOriginal,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        this.vComponentOriginal = vComponentOriginal;
        vComponentCopy = new VTodo(vComponentOriginal);
        super.setupData(vComponentCopy, startRecurrence, endRecurrence, categories);
//        super.setupData(vComponent, startRecurrence, endRecurrence, categories);
//        vComponentOriginal = new VTodo(vComponent);
    }
}