package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/** Controller for editing descriptive properties in a {@link VEvent}
 * 
 * @author David Bal
 */
public class EditDescriptiveVEventVBox extends EditDescriptiveLocatableVBox<VEvent>
{
    public EditDescriptiveVEventVBox()
    {
        super();
        endLabel.setText(getResources().getString("end.time"));
    }
    
    @Override
    public void setupData(
            VEvent vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
            vComponent.setDateTimeEnd(end);
        }
        super.setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
}
