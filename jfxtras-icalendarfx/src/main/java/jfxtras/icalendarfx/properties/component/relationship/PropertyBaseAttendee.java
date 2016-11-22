package jfxtras.icalendarfx.properties.component.relationship;

import java.net.URI;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.Delegatees;
import jfxtras.icalendarfx.parameters.Delegators;
import jfxtras.icalendarfx.parameters.GroupMembership;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.parameters.ParticipationRole;
import jfxtras.icalendarfx.parameters.ParticipationStatus;
import jfxtras.icalendarfx.parameters.RSVP;
import jfxtras.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;
import jfxtras.icalendarfx.properties.PropAttendee;

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
    public CalendarUser getCalendarUser() { return (calendarUser != null) ? calendarUser.get() : null; }
    @Override
    public ObjectProperty<CalendarUser> calendarUserProperty()
    {
        if (calendarUser == null)
        {
            calendarUser = new SimpleObjectProperty<>(this, ParameterType.CALENDAR_USER_TYPE.toString());
            orderer().registerSortOrderProperty(calendarUser);
        }
        return calendarUser;
    }
    private ObjectProperty<CalendarUser> calendarUser;
    @Override
    public void setCalendarUser(CalendarUser calendarUser) { calendarUserProperty().set(calendarUser); }
    public void setCalendarUser(String value) { setCalendarUser(CalendarUser.parse(value)); }
    public U withCalendarUser(CalendarUser type) { setCalendarUser(type); return (U) this; }
    public U withCalendarUser(CalendarUserType type) { setCalendarUser(new CalendarUser(type)); return (U) this; }
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
    public Delegators getDelegators() { return (delegators != null) ? delegators.get() : null; }
    @Override
    public ObjectProperty<Delegators> delegatorsProperty()
    {
        if (delegators == null)
        {
            delegators = new SimpleObjectProperty<>(this, ParameterType.DELEGATORS.toString());
            orderer().registerSortOrderProperty(delegators);
        }
        return delegators;
    }
    private ObjectProperty<Delegators> delegators;
    @Override
    public void setDelegators(Delegators delegators) { delegatorsProperty().set(delegators); }
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
    public Delegatees getDelegatees() { return (delegatees == null) ? null : delegatees.get(); }
    @Override
    public ObjectProperty<Delegatees> delegateesProperty()
    {
        if (delegatees == null)
        {
            delegatees = new SimpleObjectProperty<>(this, ParameterType.DELEGATEES.toString());
            orderer().registerSortOrderProperty(delegatees);
        }
        return delegatees;
    }
    private ObjectProperty<Delegatees> delegatees;
    @Override
    public void setDelegatees(Delegatees delegatees) { delegateesProperty().set(delegatees); }
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
    public GroupMembership getGroupMembership() { return (groupMembership == null) ? null : groupMembership.get(); }
    @Override
    public ObjectProperty<GroupMembership> groupMembershipProperty()
    {
        if (groupMembership == null)
        {
            groupMembership = new SimpleObjectProperty<>(this, ParameterType.GROUP_OR_LIST_MEMBERSHIP.toString());
            orderer().registerSortOrderProperty(groupMembership);
        }
        return groupMembership;
    }
    private ObjectProperty<GroupMembership> groupMembership;
    @Override
    public void setGroupMembership(GroupMembership groupMembership) { groupMembershipProperty().set(groupMembership); }
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
    public RSVP getRSVP() { return (rsvp != null) ? rsvp.get() : null; }
    @Override
    public ObjectProperty<RSVP> rsvpProperty()
    {
        if (rsvp == null)
        {
            rsvp = new SimpleObjectProperty<>(this, ParameterType.RSVP_EXPECTATION.toString());
            orderer().registerSortOrderProperty(rsvp);
        }
        return rsvp;
    }
    private ObjectProperty<RSVP> rsvp;
    @Override
    public void setRSVP(RSVP rsvp) { rsvpProperty().set(rsvp); }
    public void setRSVP(String content) { setRSVP(RSVP.parse(content)); }
    public U withRSVP(RSVP type) { setRSVP(type); return (U) this; }
    public U withRSVP(Boolean type) { setRSVP(new RSVP(type)); return (U) this; }
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
    public ParticipationStatus getParticipationStatus() { return (participationStatus != null) ? participationStatus.get() : null; }
    @Override
    public ObjectProperty<ParticipationStatus> participationStatusProperty()
    {
        if (participationStatus == null)
        {
            participationStatus = new SimpleObjectProperty<>(this, ParameterType.PARTICIPATION_STATUS.toString());
            orderer().registerSortOrderProperty(participationStatus);
        }
        return participationStatus;
    }
    private ObjectProperty<ParticipationStatus> participationStatus;
    @Override
    public void setParticipationStatus(ParticipationStatus participation) { participationStatusProperty().set(participation); }
    public void setParticipationStatus(String content) { setParticipationStatus(ParticipationStatus.parse(content)); }
    public U withParticipationStatus(ParticipationStatus type) { setParticipationStatus(type); return (U) this; }
    public U withParticipationStatus(ParticipationStatusType type) { setParticipationStatus(new ParticipationStatus(type)); return (U) this; }
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
    public ParticipationRole getParticipationRole() { return (participationRole != null) ? participationRole.get() : null; }
    @Override
    public ObjectProperty<ParticipationRole> participationRoleProperty()
    {
        if (participationRole == null)
        {
            participationRole = new SimpleObjectProperty<>(this, ParameterType.PARTICIPATION_ROLE.toString());
            orderer().registerSortOrderProperty(participationRole);
        }
        return participationRole;
    }
    private ObjectProperty<ParticipationRole> participationRole;
    @Override
    public void setParticipationRole(ParticipationRole participationRole) { participationRoleProperty().set(participationRole); }
    public void setParticipationRole(String content) { setParticipationRole(ParticipationRole.parse(content)); }
    public U withParticipationRole(ParticipationRole type) { setParticipationRole(type); return (U) this; }
    public U withParticipationRole(ParticipationRoleType type) { setParticipationRole(new ParticipationRole(type)); return (U) this; }
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
