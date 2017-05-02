package jfxtras.scene.control.agenda.icalendar.editors.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import net.balsoftware.icalendar.components.VTodo;
import net.balsoftware.icalendar.properties.component.time.DateTimeDue;
import net.balsoftware.icalendar.utilities.DateTimeUtilities;

/**
 * Reviser for {@link VTodo}
 * 
 * @author David Bal
 *
 */
public class ReviserVTodo extends ReviserLocatable<ReviserVTodo, VTodo>
{
    public ReviserVTodo(VTodo component)
    {
        super(component);
    }
    
    @Override
    public void adjustDateTime(VTodo vComponentEditedCopy)
    {
        super.adjustDateTime(vComponentEditedCopy);
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        if (vComponentEditedCopy.getDuration() != null)
        {
            vComponentEditedCopy.setDuration(duration);
        } else if (vComponentEditedCopy.getDateTimeDue() != null)
        {
            Temporal due = vComponentEditedCopy.getDateTimeStart().getValue().plus(duration);
            vComponentEditedCopy.setDateTimeDue(new DateTimeDue(due));
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
}
