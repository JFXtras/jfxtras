package jfxtras.scene.control.agenda.icalendar.factories;

import jfxtras.icalendarfx.components.VDisplayable;

public abstract class VComponentFactory<R>
{
    /** Create VComponent from a recurrence.  The recurrence is tested to determine which type of VComponent should
     * be created, such as VEVENT or VTODO
     * 
     * @param recurrence - recurrence as basis for VComponent
     * @return - new VComponent
     */
    abstract public VDisplayable<?> createVComponent(R recurrence);
}
