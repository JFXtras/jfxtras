package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** 
 * Scene for editing descriptive properties and a {@link RecurrenceRule} in a {@link VJournal}.
 * A {@link EditVJournalTabPane} is set as the root node of the scene graph<br>
 * 
 * @author David Bal
 */
public class EditVJournalScene extends EditDisplayableScene
{
    public EditVJournalScene()
    {
        super(new EditVJournalTabPane());
    }
    
    /**
     * @param vComponent - component to edit
     * @param vComponents - collection of components that vComponent is a member
     * @param startRecurrence - start of selected recurrence
     * @param endRecurrence - end of selected recurrence
     * @param categories - available category names
     */
    public EditVJournalScene(
            VJournal vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        this();
        setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
    
    EditVJournalScene setupData(
            VJournal vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        ((EditVJournalTabPane) getRoot()).setupData(vComponent, startRecurrence, endRecurrence, categories);
        return this;
    }
}
 
