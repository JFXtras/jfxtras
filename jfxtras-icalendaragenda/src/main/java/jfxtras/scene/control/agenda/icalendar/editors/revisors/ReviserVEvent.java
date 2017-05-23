package jfxtras.scene.control.agenda.icalendar.editors.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

;

/**
 * Reviser for {@link VEvent}
 * 
 * @author David Bal
 *
 */
public class ReviserVEvent extends ReviserLocatable<ReviserVEvent, VEvent>
{
    public ReviserVEvent(VEvent component)
    {
        super(component);
    }
    
    /** Adjust start and end date/time */
    @Override
    public void adjustDateTime(VEvent vComponentEditedCopy)
    {
        super.adjustDateTime(vComponentEditedCopy);
        adjustDateTimeEndOrDuration(vComponentEditedCopy);
    }

    private void adjustDateTimeEndOrDuration(VEvent vComponentEditedCopy)
    {
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        if (vComponentEditedCopy.getDuration() != null)
        {
            vComponentEditedCopy.setDuration(duration);
        } else if (vComponentEditedCopy.getDateTimeEnd() != null)
        {
            Temporal dtend = vComponentEditedCopy.getDateTimeStart().getValue().plus(duration);
            vComponentEditedCopy.setDateTimeEnd(new DateTimeEnd(dtend));
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
    
    @Override
    Collection<VPropertyElement> findChangedProperties(VEvent vComponentEditedCopy, VEvent vComponentOriginalCopy)
    {
        Collection<VPropertyElement> changedProperties = super.findChangedProperties(vComponentEditedCopy, vComponentOriginalCopy);
        TemporalAmount durationNew = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        TemporalAmount durationOriginal = getVComponentCopyEdited().getActualDuration();

        if (! durationOriginal.equals(durationNew))
        {
            if (getVComponentCopyEdited().getDateTimeEnd() != null)
            {
                changedProperties.add(VPropertyElement.DATE_TIME_END);                    
            } else if (getVComponentCopyEdited().getDuration() == null)
            {
                changedProperties.add(VPropertyElement.DURATION);                    
            }
        }      
        return changedProperties;
    }
    
    @Override
    public List<VPropertyElement> dialogRequiredProperties()
    {
        List<VPropertyElement> list = super.dialogRequiredProperties();
        list.addAll(Arrays.asList(
        		VPropertyElement.DESCRIPTION,
        		VPropertyElement.DURATION,
        		VPropertyElement.GEOGRAPHIC_POSITION,
        		VPropertyElement.LOCATION,
        		VPropertyElement.PRIORITY,
        		VPropertyElement.RESOURCES
                        ));
        return list;
    }
    
    @Override
    void editOne(VEvent vComponentEditedCopy)
    {
        super.editOne(vComponentEditedCopy);
        adjustDateTimeEndOrDuration(vComponentEditedCopy);
    }
}
