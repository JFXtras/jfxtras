package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;

/**
 * Simple factory to create {@link EditDisplayableScene} objects.  Contains two methods to create scenes.
 * One takes only a VComponent as a parameter and builds an empty {@link EditDisplayableScene}.
 * The second takes a VComponent and an array of parameters required to completely
 * initialize the {@link EditDisplayableScene}.
 * 
 * @author David Bal
 *
 */
public class SimpleEditSceneFactory
{
    /**
     * Create a Stage to edit the type of VComponent passed as a parameter
     * <p>
     * Parameters array must contain the following:<br>
     * (0) VCalendar - parent VCalendar<br>
     * (1) Temporal startRecurrence - start of selected recurrence<br>
     * (2) Temporal endRecurrence - end of selected recurrence<br>
     * (3) String List categories - available category names<br>
     * 
     * @param vComponent - VComponent to be edited
     * @param params - necessary parameters, packed in an array, to edit the VComponent
     * @return
     */
    // TODO - REPLACE params WITH REGULAR PARAMETERS
    public static EditDisplayableScene newScene (VComponent vComponent, Object[] params)
    {
        // params[0] is VCalendar, handled below
        Temporal startRecurrence = (Temporal) params[1];       // startRecurrence - start of selected recurrence
        Temporal endRecurrence = (Temporal) params[2];         // endRecurrence - end of selected recurrence
        List<String> categories = (List<String>) params[3];    // categories - available category names

        if (vComponent instanceof VEvent)
        {
            return new EditVEventScene()
                    .setupData(
                        (VEvent) vComponent,                        // vComponent - component to edit
                        startRecurrence,                            // startRecurrence - start of selected recurrence
                        endRecurrence,                              // endRecurrence - end of selected recurrence
                        categories                                  // categories - available category names
                    );
        } else if (vComponent instanceof VTodo)
        {
            return new EditVTodoScene()
                    .setupData(
                        (VTodo) vComponent,                        // vComponent - component to edit
                        startRecurrence,                           // startRecurrence - start of selected recurrence
                        endRecurrence,                             // endRecurrence - end of selected recurrence
                        categories                                 // categories - available category names
                    );
           
        } else if (vComponent instanceof VJournal)
        {
            return new EditVJournalScene()
                    .setupData(
                        (VJournal) vComponent,                      // vComponent - component to edit
                        startRecurrence,                            // startRecurrence - start of selected recurrence
                        endRecurrence,                              // endRecurrence - end of selected recurrence
                        categories                                  // categories - available category names
                    );
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");          
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
    
    /**
     * Create a Stage to edit the type of VComponent passed as a parameter
     * 
     * @param vComponent  component to edit
     * @param startRecurrence  start of selected recurrence
     * @param endRecurrence  end of selected recurrence
     * @param categories  available category names
     * @return  the new Stage containing an edit popup 
     */
    public static EditDisplayableScene newScene (
            VComponent vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        if (vComponent instanceof VEvent)
        {
            return new EditVEventScene()
                    .setupData(
                        (VEvent) vComponent,                        // vComponent - component to edit
                        startRecurrence,                            // startRecurrence - start of selected recurrence
                        endRecurrence,                              // endRecurrence - end of selected recurrence
                        categories                                  // categories - available category names
                    );
        } else if (vComponent instanceof VTodo)
        {
            return new EditVTodoScene()
                    .setupData(
                        (VTodo) vComponent,                        // vComponent - component to edit
                        startRecurrence,                           // startRecurrence - start of selected recurrence
                        endRecurrence,                             // endRecurrence - end of selected recurrence
                        categories                                 // categories - available category names
                    );
           
        } else if (vComponent instanceof VJournal)
        {
            return new EditVJournalScene()
                    .setupData(
                        (VJournal) vComponent,                      // vComponent - component to edit
                        startRecurrence,                            // startRecurrence - start of selected recurrence
                        endRecurrence,                              // endRecurrence - end of selected recurrence
                        categories                                  // categories - available category names
                    );
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");          
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
    
    /** Create an empty {@link EditDisplayableScene} */
    public static EditDisplayableScene newScene (VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new EditVEventScene();
        } else if (vComponent instanceof VTodo)
        {
            return new EditVTodoScene();
        } else if (vComponent instanceof VJournal)
        {
            return new EditVJournalScene();
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");          
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
}
