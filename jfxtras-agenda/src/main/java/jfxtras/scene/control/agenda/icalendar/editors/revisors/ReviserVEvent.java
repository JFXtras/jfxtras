package jfxtras.scene.control.agenda.icalendar.editors.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.List;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

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
    List<PropertyType> findChangedProperties(VEvent vComponentEditedCopy, VEvent vComponentOriginalCopy)
    {
        List<PropertyType> changedProperties = super.findChangedProperties(vComponentEditedCopy, vComponentOriginalCopy);
        TemporalAmount durationNew = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        TemporalAmount durationOriginal = getVComponentCopyEdited().getActualDuration();

        if (! durationOriginal.equals(durationNew))
        {
            if (getVComponentCopyEdited().getDateTimeEnd() != null)
            {
                changedProperties.add(PropertyType.DATE_TIME_END);                    
            } else if (getVComponentCopyEdited().getDuration() == null)
            {
                changedProperties.add(PropertyType.DURATION);                    
            }
        }      
        return changedProperties;
    }
    
    @Override
    public List<PropertyType> dialogRequiredProperties()
    {
        List<PropertyType> list = super.dialogRequiredProperties();
        list.addAll(Arrays.asList(
                        PropertyType.DESCRIPTION,
                        PropertyType.DURATION,
                        PropertyType.GEOGRAPHIC_POSITION,
                        PropertyType.LOCATION,
                        PropertyType.PRIORITY,
                        PropertyType.RESOURCES
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
