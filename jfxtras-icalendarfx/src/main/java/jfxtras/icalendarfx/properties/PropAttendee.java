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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties;

import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.Delegatees;
import jfxtras.icalendarfx.parameters.Delegators;
import jfxtras.icalendarfx.parameters.GroupMembership;
import jfxtras.icalendarfx.parameters.ParticipationRole;
import jfxtras.icalendarfx.parameters.ParticipationStatus;
import jfxtras.icalendarfx.parameters.RSVP;
import jfxtras.icalendarfx.properties.PropCalendarUser;
import jfxtras.icalendarfx.properties.component.relationship.PropertyBaseAttendee;

/**
 * Abstract class for Attendee and unknown properties
 * 
 * @author David Bal
 *
 * @param <U> - subclass
 * 
 * @see PropertyBaseAttendee
 */
public interface PropAttendee<U> extends PropCalendarUser<U>
{
    /**
     * CUTYPE
     * Calendar User Type
     * RFC 5545, 3.2.3, page 16
     * 
     * To identify the type of calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org
     */
    CalendarUser getCalendarUser();
    void setCalendarUser(CalendarUser calendarUser);  

    /**
     * DELEGATED-FROM
     * Delegators
     * RFC 5545, 3.2.4, page 17
     * 
     * To specify the calendar users that have delegated their
     *    participation to the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;DELEGATED-FROM="mailto:jsmith@example.com":mailto:
     *  jdoe@example.com
     */
    Delegators getDelegators();
    void setDelegators(Delegators delegators);

    /**
     * DELEGATED-TO
     * Delegatees
     * RFC 5545, 3.2.5, page 17
     * 
     * To specify the calendar users to whom the calendar user
     *    specified by the property has delegated participation.
     * 
     * Example:
     * ATTENDEE;DELEGATED-TO="mailto:jdoe@example.com","mailto:jqpublic
     *  @example.com":mailto:jsmith@example.com
     * 
     */
    Delegatees getDelegatees();
    void setDelegatees(Delegatees delegatees);
    
    /**
     * MEMBER
     * Group or List Membership
     * RFC 5545, 3.2.11, page 21
     * 
     * To specify the group or list membership of the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;MEMBER="mailto:projectA@example.com","mailto:pr
     *  ojectB@example.com":mailto:janedoe@example.com
     * 
     */
    GroupMembership getGroupMembership();
    void setGroupMembership(GroupMembership groupMembership);
    
    /**
     * RSVP
     * RSVP Expectation
     * RFC 5545, 3.2.17, page 26
     * 
     * To specify whether there is an expectation of a favor of a reply from the calendar user specified by the property value.
     * 
     * Example:
     * ATTENDEE;RSVP=TRUE:mailto:jsmith@example.com
     */
    RSVP getRSVP();
    void setRSVP(RSVP rsvp);
    
    /**
     * PARTSTAT
     * Participation Status
     * RFC 5545, 3.2.12, page 22
     * 
     * To specify the participation role for the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;PARTSTAT=DECLINED:mailto:jsmith@example.com
     */
    ParticipationStatus getParticipationStatus();
    void setParticipationStatus(ParticipationStatus participation); 

    /**
     * ROLE
     * Participation Role
     * RFC 5545, 3.2.16, page 25
     * 
     * To specify the participation role for the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;ROLE=CHAIR:mailto:mrbig@example.com
     */
    ParticipationRole getParticipationRole();
    void setParticipationRole(ParticipationRole participationRole);
}
