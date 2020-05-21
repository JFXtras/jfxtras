/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
