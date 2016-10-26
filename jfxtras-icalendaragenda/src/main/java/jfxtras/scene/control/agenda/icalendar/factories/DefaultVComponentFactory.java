package jfxtras.scene.control.agenda.icalendar.factories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Default factory to create a {@link VDisplayable} from {@link Appointment}
 * 
 * @author David Bal
 *
 */
public class DefaultVComponentFactory extends VComponentFactory<Appointment>
{
    final private Organizer organizer;
    
    public DefaultVComponentFactory(Organizer organizer)
    {
        this.organizer = organizer;
    }
    
    @Override
    public VDisplayable<?> createVComponent(Appointment appointment)
    {
        final VDisplayable<?> newVComponent;
        ZonedDateTime dtCreated = ZonedDateTime.now(ZoneId.of("Z"));
        String summary = ((appointment.getSummary() == null) || appointment.getSummary().isEmpty()) ? null : appointment.getSummary();
        String description = ((appointment.getDescription() == null) || appointment.getDescription().isEmpty()) ? null : appointment.getDescription();
        String category = (appointment.getAppointmentGroup() == null) ? null : appointment.getAppointmentGroup().getDescription();
        String location = ((appointment.getLocation() == null) || appointment.getLocation().isEmpty()) ? null : appointment.getLocation();
        Temporal dtstart = (appointment.isWholeDay()) ? LocalDate.from(appointment.getStartTemporal()) : appointment.getStartTemporal();
        Temporal dtend = (appointment.isWholeDay()) ? LocalDate.from(appointment.getEndTemporal()) : appointment.getEndTemporal();
        
        boolean hasEnd = appointment.getEndTemporal() != null;
        if (hasEnd)
        {
            newVComponent = new VEvent();
            newVComponent.withOrganizer(organizer);
            newVComponent.withSummary(summary);
            newVComponent.withCategories(category);
            newVComponent.withDateTimeStart(dtstart);
            ((VEvent) newVComponent).withDateTimeEnd(dtend);
            ((VEvent) newVComponent).withDescription(description);
            ((VEvent) newVComponent).setLocation(location);
            newVComponent.setDateTimeCreated(dtCreated);
            newVComponent.setDateTimeStamp(dtCreated);
            newVComponent.setUniqueIdentifier(); // using default UID generator
            // TODO - GRADLE WON'T ALLOW CHAINING AS SEEN BELOW.  WHY???
//            newVComponent = new VEvent()
//                    .withOrganizer(organizer)
//                    .withSummary(summary)
//                    .withCategories(category)
//                    .withDateTimeStart(dtstart)
//                    .withDateTimeEnd(dtend)
//                    .withDescription(description)
//                    .withLocation(location)
//                    .withDateTimeCreated(dtCreated)
//                    .withDateTimeStamp(dtCreated)
//                    .withUniqueIdentifier(); // using default UID generator
        } else
        {
            newVComponent = new VTodo();
            newVComponent.withOrganizer(organizer);
            newVComponent.withSummary(summary);
            newVComponent.withCategories(category);
            newVComponent.withDateTimeStart(dtstart);
            ((VTodo) newVComponent).withDescription(description);
            ((VTodo) newVComponent).setLocation(location);
            newVComponent.setDateTimeCreated(dtCreated);
            newVComponent.setDateTimeStamp(dtCreated);
            newVComponent.setUniqueIdentifier(); // using default UID generator
            // TODO - GRADLE WON'T ALLOW CHAINING AS SEEN BELOW.  WHY???
//          newVComponent = new VTodo()
//          .withOrganizer(organizer)
//          .withSummary(summary)
//          .withCategories(category)
//          .withDateTimeStart(dtstart)
//          .withDescription(description)
//          .withLocation(location)
//          .withDateTimeCreated(dtCreated)
//          .withDateTimeStamp(dtCreated)
//          .withUniqueIdentifier();
        }
        /* Note: If other VComponents are to be supported then other tests to determine
         * which type of VComponent the Appointment represents will need to be created.
         */
        return newVComponent;
    }
}
