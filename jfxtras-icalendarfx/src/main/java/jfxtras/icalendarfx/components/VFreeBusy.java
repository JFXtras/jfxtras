package jfxtras.icalendarfx.components;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VDateTimeEnd;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.properties.component.relationship.Contact;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.FreeBusyTime;
import jfxtras.icalendarfx.utilities.Pair;

/**
 * VFREEBUSY
 * Free/Busy Component
 * RFC 5545 iCalendar 3.6.4. page 59
 * 
A "VFREEBUSY" calendar component is a grouping of
      component properties that represents either a request for free or
      busy time information, a reply to a request for free or busy time
      information, or a published set of busy time information.

      When used to request free/busy time information, the "ATTENDEE"
      property specifies the calendar users whose free/busy time is
      being requested; the "ORGANIZER" property specifies the calendar
      user who is requesting the free/busy time; the "DTSTART" and
      "DTEND" properties specify the window of time for which the free/
      busy time is being requested; the "UID" and "DTSTAMP" properties
      are specified to assist in proper sequencing of multiple free/busy
      time requests.

      When used to reply to a request for free/busy time, the "ATTENDEE"
      property specifies the calendar user responding to the free/busy
      time request; the "ORGANIZER" property specifies the calendar user
      that originally requested the free/busy time; the "FREEBUSY"
      property specifies the free/busy time information (if it exists);
      and the "UID" and "DTSTAMP" properties are specified to assist in
      proper sequencing of multiple free/busy time replies.

      When used to publish busy time, the "ORGANIZER" property specifies
      the calendar user associated with the published busy time; the
      "DTSTART" and "DTEND" properties specify an inclusive time window
      that surrounds the busy time information; the "FREEBUSY" property
      specifies the published busy time information; and the "DTSTAMP"
      property specifies the DATE-TIME that iCalendar object was
      created.

      The "VFREEBUSY" calendar component cannot be nested within another
      calendar component.  Multiple "VFREEBUSY" calendar components can
      be specified within an iCalendar object.  This permits the
      grouping of free/busy information into logical collections, such
      as monthly groups of busy time information.

      The "VFREEBUSY" calendar component is intended for use in
      iCalendar object methods involving requests for free time,
      requests for busy time, requests for both free and busy, and the
      associated replies.

      Free/Busy information is represented with the "FREEBUSY" property.
      This property provides a terse representation of time periods.
      One or more "FREEBUSY" properties can be specified in the
      "VFREEBUSY" calendar component.

      When present in a "VFREEBUSY" calendar component, the "DTSTART"
      and "DTEND" properties SHOULD be specified prior to any "FREEBUSY"
      properties.

      The recurrence properties ("RRULE", "RDATE", "EXDATE") are not
      permitted within a "VFREEBUSY" calendar component.  Any recurring
      events are resolved into their individual busy time periods using
      the "FREEBUSY" property.

   Example:  The following is an example of a "VFREEBUSY" calendar
      component used to request free or busy time information:

       BEGIN:VFREEBUSY
       UID:19970901T082949Z-FA43EF@example.com
       ORGANIZER:mailto:jane_doe@example.com
       ATTENDEE:mailto:john_public@example.com
       DTSTART:19971015T050000Z
       DTEND:19971016T050000Z
       DTSTAMP:19970901T083000Z
       END:VFREEBUSY
 * 
 * @author David Bal
 *
 */
public class VFreeBusy extends VPersonal<VFreeBusy> implements VDateTimeEnd<VFreeBusy>
{
    /**
     * CONTACT:
     * RFC 5545 iCalendar 3.8.4.2. page 109
     * This property is used to represent contact information or
     * alternately a reference to contact information associated with the
     * calendar component.
     * 
     * NOTE: This property can only occur once in VFreeBusy (multiple times allowed
     * for other components)
     * 
     * Example:
     * CONTACT;ALTREP="ldap://example.com:6666/o=ABC%20Industries\,
     *  c=US???(cn=Jim%20Dolittle)":Jim Dolittle\, ABC Industries\,
     *  +1-919-555-1234
     */
    private Contact contact;
    public Contact getContact() { return contact; }
    public void setContact(String contact) { setContact(Contact.parse(contact)); }
    public void setContact(Contact contact)
    {
    	orderChild(this.contact, contact);
    	this.contact = contact;
	}
    public VFreeBusy withContact(Contact contact)
    {
    	setContact(contact);
    	return this;
	}
    public VFreeBusy withContact(String contact)
    {
    	setContact(Contact.parse(contact));
    	return this;
	}
    
    /**
     * DTEND
     * Date-Time End (for local-date)
     * RFC 5545, 3.8.2.2, page 95
     * 
     * This property specifies when the calendar component ends.
     * 
     * The value type of this property MUST be the same as the "DTSTART" property, and
     * its value MUST be later in time than the value of the "DTSTART" property.
     * 
     * Example:
     * DTEND;VALUE=DATE:19980704
     */
    
    @Override
    public DateTimeEnd getDateTimeEnd() { return dateTimeEnd; }
    private DateTimeEnd dateTimeEnd;
    @Override
	public void setDateTimeEnd(DateTimeEnd dateTimeEnd)
    {
    	orderChild(this.dateTimeEnd, dateTimeEnd);
    	this.dateTimeEnd = dateTimeEnd;
	}
    
    /**
     * FREEBUSY
     * Free/Busy Time
     * RFC 5545, 3.8.2.6, page 100
     * 
     * This property defines one or more free or busy time intervals.
     * 
     * These time periods can be specified as either a start
     * and end DATE-TIME or a start DATE-TIME and DURATION.  The date and
     * time MUST be a UTC time format.  Internally, the values are stored only as 
     * start DATE-TIME and DURATION.  Any values entered as start and end as both
     * DATE-TIME are converted to the start DATE-TIME and DURATION.
     * 
     * Examples:
     * FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M
     * FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H
     *  ,19970308T230000Z/19970309T000000Z
     * 
     * Note: The above example is converted and outputed as the following:
     * FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H
     *  ,19970308T230000Z/PT1H
     */
    private FreeBusyTime freeBusyTime;
    public FreeBusyTime getFreeBusyTime() { return freeBusyTime; }
    public void setFreeBusyTime(String freeBusyTime) { setFreeBusyTime(FreeBusyTime.parse(freeBusyTime)); }
    public void setFreeBusyTime(List<Pair<ZonedDateTime, TemporalAmount>> freeBusyTime) { setFreeBusyTime(new FreeBusyTime(freeBusyTime)); }
    public void setFreeBusyTime(FreeBusyTime freeBusyTime)
    {
    	orderChild(this.freeBusyTime, freeBusyTime);
    	this.freeBusyTime = freeBusyTime;
	}
    public VFreeBusy withFreeBusyTime(FreeBusyTime freeBusyTime)
    {
    	setFreeBusyTime(freeBusyTime);
    	return this;
	}
    public VFreeBusy withFreeBusyTime(List<Pair<ZonedDateTime, TemporalAmount>> freeBusyTime)
    {
    	setFreeBusyTime(freeBusyTime);
    	return this;
	}
    public VFreeBusy withFreeBusyTime(String freeBusyTime)
    {
    	setFreeBusyTime(FreeBusyTime.parse(freeBusyTime));
    	return this;
	}
    
	@Override
	public List<VFreeBusy> calendarList()
	{
		if (getParent() != null)
		{
			VCalendar cal = (VCalendar) getParent();
			return cal.getVFreeBusies();
		}
		return null;
	}
 
    /*
     * CONSTRUCTORS
     */
    public VFreeBusy() { super(); }
    
    /** Copy constructor */
    public VFreeBusy(VFreeBusy source)
    {
        super(source);
    }
        
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        List<String> dtendError = VDateTimeEnd.errorsDateTimeEnd(this);
        errors.addAll(dtendError);
        return Collections.unmodifiableList(errors);
    }
    
    /**
     * Creates a new VFreeBusy calendar component by parsing a String of iCalendar content lines
     *
     * @param content  the text to parse, not null
     * @return  the parsed VFreeBusy
     */
    public static VFreeBusy parse(String content)
    {
    	return VFreeBusy.parse(new VFreeBusy(), content);
    }
}
