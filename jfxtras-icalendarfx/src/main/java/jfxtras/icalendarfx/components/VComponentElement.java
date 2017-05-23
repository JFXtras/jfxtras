package jfxtras.icalendarfx.components;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VComponentElement;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;

/**
 * <p>Enumerated type containing all the {@link VComponent} elements that can be in a {@link VCalendar}</p>
 * 
 * @author David Bal
 *
 */
public enum VComponentElement
{
    // MAIN COMPONENTS
    VEVENT ("VEVENT", VEvent.class),
    VTODO ("VTODO", VTodo.class),
    VJOURNAL ("VJOURNAL", VJournal.class),
    VTIMEZONE ("VTIMEZONE", VTimeZone.class),
    VFREEBUSY ("VFREEBUSY", VFreeBusy.class),
    DAYLIGHT_SAVING_TIME ("DAYLIGHT", DaylightSavingTime.class),
    STANDARD_TIME ("STANDARD", StandardTime.class),
    VALARM ("VALARM", VAlarm.class)
    ;

    // Map to match up name to enum
    private static final Map<String, VComponentElement> NAME_MAP = Arrays.stream(values())
    		.collect(Collectors.toMap(
    				v -> v.toString(),
    				v -> v));
    public static VComponentElement fromName(String propertyName)
    {
        return NAME_MAP.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static final Map<Class<? extends VComponent>, VComponentElement> CLASS_MAP = Arrays.stream(values())
    		.collect(Collectors.toMap(
    				v -> v.elementClass(),
    				v -> v));
    /** get enum from map */
    public static VComponentElement fromClass(Class<? extends VElement> myClass)
    {
    	VComponentElement p = CLASS_MAP.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(VComponentElement.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    /*
     * FIELDS
     */
    private Class<? extends VComponent> myClass;
    public Class<? extends VComponent> elementClass() { return myClass; }
    
    private String name;
    @Override
    public String toString() { return name; }

    /*
     * CONSTRUCTOR
     */
    VComponentElement(String name, Class<? extends VComponent> myClass)
    {
        this.name = name;
        this.myClass = myClass;
    }
}
