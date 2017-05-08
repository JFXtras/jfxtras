package jfxtras.scene.control.agenda.icalendar.editors.deleters;

import java.util.List;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;

/**
 * Interface for the delete behavior of a VComponent
 * 
 * <p>Delete options include:
 * <ul>
 * <li>One
 * <li>All
 * <li>This-and-Future
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 */
public interface Deleter
{
    /** Revise list of iTIP VCalendar components that represent the canceled calendar components. */
    // TODO - DETERMINE IF A LIST IS REQUIRED OR JUST ONE VCALENDAR
    List<VCalendar> delete();
    
 // TODO - ADD A INITIALIZE METHOD FOR AN ARRAY OF INPUT OBJECT PARAMETERS
    
    public static VCalendar emptyCanceliTIPMessage()
    {
        return new VCalendar()
                .withMethod(MethodType.CANCEL)
                .withProductIdentifier(ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER)
                .withVersion(new Version());
    }
}
