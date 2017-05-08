package jfxtras.icalendarfx.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;

/**
 * Interface for {@link Attendee} property
 * 
 * @author David Bal
 *
 * @param <T>  concrete subclass
 */
public interface VAttendee<T> extends VComponent
{
    /**
     * <p>This property defines an "Attendee" within a calendar component.<br>
     * RFC 5545 iCalendar 3.8.4.1 page 107</p>
     * 
     * <p>Examples:
     * <ul>
     * <li>ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":<br>
     *  mailto:joecool@example.com
     * <li>ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe<br>
     *  :mailto:jdoe@example.com
     *  </ul>
     *  </p>
     */
    List<Attendee> getAttendees();
    void setAttendees(List<Attendee> properties);
    /**
     *  Sets the value of the {@link #attendeesProperty()} }
     *  
     *  @return - this class for chaining
     */
    default T withAttendees(List<Attendee> attendees)
    {
    	if (getAttendees() == null)
    	{
    		setAttendees(new ArrayList<>());
    	}
    	getAttendees().addAll(attendees);
    	if (attendees != null)
    	{
    		attendees.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    /**
     * Sets the value of the {@link #attendeesProperty()} from a vararg of {@link Attendee} objects.
     * 
     * @return - this class for chaining
     */    
    default T withAttendees(Attendee...attendees)
    {
    	return withAttendees(Arrays.asList(attendees));
    }
    /**
     * <p>Sets the value of the {@link #attendeesProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link Attendee} objects.</p>
     * 
     * @return - this class for chaining
     */    
    default T withAttendees(String...attendees)
    {
        List<Attendee> list = Arrays.stream(attendees)
            .map(c -> Attendee.parse(c))
            .collect(Collectors.toList());
        return withAttendees(list);
    }
}
