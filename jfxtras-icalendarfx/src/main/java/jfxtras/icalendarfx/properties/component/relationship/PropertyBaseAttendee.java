package jfxtras.icalendarfx.properties.component.relationship;

import java.net.URI;
import java.util.List;

import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.Delegatees;
import jfxtras.icalendarfx.parameters.Delegators;
import jfxtras.icalendarfx.parameters.GroupMembership;
import jfxtras.icalendarfx.parameters.ParticipationRole;
import jfxtras.icalendarfx.parameters.ParticipationStatus;
import jfxtras.icalendarfx.parameters.RSVP;
import jfxtras.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;
import jfxtras.icalendarfx.properties.PropAttendee;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.properties.component.relationship.PropertyBaseAttendee;
import jfxtras.icalendarfx.properties.component.relationship.PropertyBaseCalendarUser;

/**
 * Abstract class for Attendee and unknown properties
 * 
 * @author David Bal
 *
 * @param <U> - subclass
 * @param <T> - property value type
 * @see Attendee
 * @see Organizer
 */
public abstract class PropertyBaseAttendee<T,U> extends PropertyBaseCalendarUser<T,U> implements PropAttendee<T>
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
    @Override
    public CalendarUser getCalendarUser() { return calendarUser; }
    private CalendarUser calendarUser;
    @Override
    public void setCalendarUser(CalendarUser calendarUser)
    {
    	orderChild(this.calendarUser, calendarUser);
    	this.calendarUser = calendarUser;
	}
    public void setCalendarUser(String value) { setCalendarUser(CalendarUser.parse(value)); }
    public U withCalendarUser(CalendarUser calendarUser) { setCalendarUser(calendarUser); return (U) this; }
    public U withCalendarUser(CalendarUserType calendarUser) { setCalendarUser(new CalendarUser(calendarUser)); return (U) this; }
    public U withCalendarUser(String content) { setCalendarUser(content); return (U) this; }    

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
    @Override
    public Delegators getDelegators() { return delegators; }
    private Delegators delegators;
    @Override
    public void setDelegators(Delegators delegators)
    {
    	orderChild(delegators);
    	this.delegators = delegators;
	}
    public void setDelegators(String content) { setDelegators(Delegators.parse(content)); }
    public U withDelegators(Delegators delegators) { setDelegators(delegators); return (U) this; }
    public U withDelegators(List<URI> delegators) { setDelegators(new Delegators(delegators)); return (U) this; }
    public U withDelegators(String content) { setDelegators(content); return (U) this; }    

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
    @Override
    public Delegatees getDelegatees() { return delegatees; }
    private Delegatees delegatees;
    @Override
    public void setDelegatees(Delegatees delegatees)
    {
    	orderChild(delegatees);
    	this.delegatees = delegatees;
	}
    public void setDelegatees(String content) { setDelegatees(Delegatees.parse(content)); }
    public U withDelegatees(Delegatees delegatees) { setDelegatees(delegatees); return (U) this; }
    public U withDelegatees(List<URI> values) { setDelegatees(new Delegatees(values)); return (U) this; }
    public U withDelegatees(String content) { setDelegatees(content); return (U) this; }    

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
    @Override
    public GroupMembership getGroupMembership() { return groupMembership; }
    private GroupMembership groupMembership;
    @Override
    public void setGroupMembership(GroupMembership groupMembership)
    {
    	orderChild(groupMembership);
    	this.groupMembership = groupMembership;
	}
    public void setGroupMembership(String content) { setGroupMembership(GroupMembership.parse(content)); }
    public U withGroupMembership(GroupMembership groupMembership) { setGroupMembership(groupMembership); return (U) this; }
    public U withGroupMembership(List<URI> values) { setGroupMembership(new GroupMembership(values)); return (U) this; }
    public U withGroupMembership(String content) { setGroupMembership(content); return (U) this; }    

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
    @Override
    public RSVP getRSVP() { return rsvp; }
    private RSVP rsvp;
    @Override
    public void setRSVP(RSVP rsvp)
    {
    	orderChild(rsvp);
    	this.rsvp = rsvp;
	}
    public void setRSVP(String content) { setRSVP(RSVP.parse(content)); }
    public U withRSVP(RSVP rsvp) { setRSVP(rsvp); return (U) this; }
    public U withRSVP(Boolean rsvp) { setRSVP(new RSVP(rsvp)); return (U) this; }
    public U withRSVP(String content) { setRSVP(content); return (U) this; }   
    
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
    @Override
    public ParticipationStatus getParticipationStatus() { return participationStatus; }
    private ParticipationStatus participationStatus;
    @Override
    public void setParticipationStatus(ParticipationStatus participationStatus)
    {
    	orderChild(participationStatus);
    	this.participationStatus = participationStatus;
	}
    public void setParticipationStatus(String content) { setParticipationStatus(ParticipationStatus.parse(content)); }
    public U withParticipationStatus(ParticipationStatus participationStatus) { setParticipationStatus(participationStatus); return (U) this; }
    public U withParticipationStatus(ParticipationStatusType participationStatus) { setParticipationStatus(new ParticipationStatus(participationStatus)); return (U) this; }
    public U withParticipationStatus(String content) { setParticipationStatus(content); return (U) this; }  

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
    @Override
    public ParticipationRole getParticipationRole() { return participationRole; }
    private ParticipationRole participationRole;
    @Override
    public void setParticipationRole(ParticipationRole participationRole)
    {
    	orderChild(participationRole);
    	this.participationRole = participationRole;
	}
    public void setParticipationRole(String content) { setParticipationRole(ParticipationRole.parse(content)); }
    public U withParticipationRole(ParticipationRole participationRole) { setParticipationRole(participationRole); return (U) this; }
    public U withParticipationRole(ParticipationRoleType participationRole) { setParticipationRole(new ParticipationRole(participationRole)); return (U) this; }
    public U withParticipationRole(String content) { setParticipationRole(content); return (U) this; }  

    /*
     * CONSTRUCTORS
     */
    
    public PropertyBaseAttendee(T value)
    {
        super(value);
    }
    
    public PropertyBaseAttendee(PropertyBaseAttendee<T,U> source)
    {
        super(source);
    }
    
    protected PropertyBaseAttendee()
    {
        super();
    }
}
