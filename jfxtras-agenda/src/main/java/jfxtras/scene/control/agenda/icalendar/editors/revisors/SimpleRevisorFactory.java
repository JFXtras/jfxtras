package jfxtras.scene.control.agenda.icalendar.editors.revisors;

import java.time.temporal.Temporal;
import java.util.Map;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;

/**
 * <p>Simple factory to create {@link Reviser} objects.  Two methods to create scenes
 * exist.  One takes only a VComponent as a parameter and builds an empty {@link Reviser}.
 * The second takes a VComponent and an array of parameters required to completely
 * initialize the {@link Reviser}.</p>
 * 
 * <p>The parameters array contains the following:
 * <ul>
 * <li>{@code Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>} - callback for user dialog
 * <li>Temporal - endRecurrence, end of selected recurrence
 * <li>Temporal - startRecurrence, start of selected recurrence
 * <li>Temporal - startOriginalRecurrence, start of selected recurrence
 * <li>VComponent - VComponent with non-time changes applied
 * <li>VComponent - copy of original unchanged VComponent
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 */
public class SimpleRevisorFactory
{
    /** New reviser with all parameters packaged in an array */
    public static Reviser newReviser(VComponent vComponent, Object[] params)
    {
        if (vComponent instanceof VEvent)
        {
            if (params.length != 6) throw new IllegalArgumentException("Can't create Reviser: Paramaters should be 6 not" + params.length);
            return new ReviserVEvent((VEvent) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[0])
                    .withEndRecurrence((Temporal) params[1])
                    .withStartOriginalRecurrence((Temporal) params[2])
                    .withStartRecurrence((Temporal) params[3])
                    .withVComponentCopyEdited((VEvent) params[4])
                    .withVComponentOriginal((VEvent) params[5]);
        } else if (vComponent instanceof VTodo)
        {
            if (params.length != 6) throw new IllegalArgumentException("Can't create Reviser: Paramaters should be 6 not" + params.length);
            return new ReviserVTodo((VTodo) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[0])
                    .withEndRecurrence((Temporal) params[1])
                    .withStartOriginalRecurrence((Temporal) params[2])
                    .withStartRecurrence((Temporal) params[3])
                    .withVComponentCopyEdited((VTodo) params[4])
                    .withVComponentOriginal((VTodo) params[5]);
        } else if (vComponent instanceof VJournal)
        {
            // Note: array is different - endRecurrence is omitted
            if (params.length != 5) throw new IllegalArgumentException("Can't create Reviser: Paramaters should be 6 not" + params.length);
            return new ReviserVJournal((VJournal) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[0])
                    .withStartOriginalRecurrence((Temporal) params[1])
                    .withStartRecurrence((Temporal) params[2])
                    .withVComponentCopyEdited((VJournal) params[3])
                    .withVComponentOriginal((VJournal) params[4]);
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
    
    /** New reviser without parameters */
    public static Reviser newReviser(VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new ReviserVEvent((VEvent) vComponent);
        } else if (vComponent instanceof VTodo)
        {
            return new ReviserVTodo((VTodo) vComponent);            
        } else if (vComponent instanceof VJournal)
        {
            return new ReviserVJournal((VJournal) vComponent);            
        } else if (vComponent instanceof VFreeBusy)
        {
            return new ReviserVFreeBusy((VFreeBusy) vComponent);            
        } else if (vComponent instanceof VTimeZone)
        {
            return new ReviserVTimeZone((VTimeZone) vComponent);            
        } else if (vComponent instanceof VAlarm)
        {
            return new ReviserVAlarm((VAlarm) vComponent);            
        } else if (vComponent instanceof StandardTime)
        {
            return new ReviserStandardTime((StandardTime) vComponent);            
        } else if (vComponent instanceof DaylightSavingTime)
        {
            return new ReviserDaylightSavingTime((DaylightSavingTime) vComponent);            
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
}
