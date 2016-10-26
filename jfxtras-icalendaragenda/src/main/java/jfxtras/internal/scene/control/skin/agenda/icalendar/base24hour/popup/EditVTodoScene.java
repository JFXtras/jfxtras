package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;

/**
 * Scene for editing descriptive properties and a {@link RecurrenceRule} in a {@link VTodo}.
 * A {@link EditVTodoTabPane} is set as the root node of the scene graph<br>
 * 
 * @author David Bal
 */
public class EditVTodoScene extends EditDisplayableScene
{
    public EditVTodoScene()
    {
        super(new EditVTodoTabPane());
    }
    
    /**
     * @param vComponent - component to edit
     * @param startRecurrence - start of selected recurrence
     * @param endRecurrence - end of selected recurrence
     * @param categories - available category names
     */
    public EditVTodoScene(
            VTodo vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        this();
        setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
    
    EditVTodoScene setupData(
            VTodo vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        ((EditVTodoTabPane) getRoot()).setupData(vComponent, startRecurrence, endRecurrence, categories);
        return this;
    }
}
 
