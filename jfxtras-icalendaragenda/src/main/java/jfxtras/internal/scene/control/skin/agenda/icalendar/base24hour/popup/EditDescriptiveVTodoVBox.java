package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/** 
 * Controller for editing descriptive properties in a {@link VTodo}
 * 
 * @author David Bal
 */
public class EditDescriptiveVTodoVBox extends EditDescriptiveLocatableVBox<VTodo>
{
    public EditDescriptiveVTodoVBox()
    {
        super();
        endLabel.setText( getResources().getString("due.time") );
    }
    
    @Override
    public void setupData(
//            Appointment appointment,
            VTodo vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {        
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
            vComponent.setDateTimeDue(end);
        }
        
        super.setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
}
