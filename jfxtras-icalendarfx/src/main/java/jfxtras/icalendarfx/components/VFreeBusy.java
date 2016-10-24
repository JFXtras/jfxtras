package jfxtras.icalendarfx.components;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.relationship.Contact;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.FreeBusyTime;

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
    public ObjectProperty<Contact> contactProperty()
    {
        if (contact == null)
        {
            contact = new SimpleObjectProperty<>(this, PropertyType.CONTACT.toString());
            orderer().registerSortOrderProperty(contact);
        }
        return contact;
    }
    private ObjectProperty<Contact> contact;
    public Contact getContact() { return contactProperty().get(); }
    public void setContact(String contact) { setContact(Contact.parse(contact)); }
    public void setContact(Contact contact) { contactProperty().set(contact); }
    public VFreeBusy withContact(Contact contact) { setContact(contact); return this; }
    public VFreeBusy withContact(String contact) { PropertyType.CONTACT.parse(this, contact); return this; }
    
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
    @Override public ObjectProperty<DateTimeEnd> dateTimeEndProperty()
    {
        if (dateTimeEnd == null)
        {
            dateTimeEnd = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_END.toString());
            orderer().registerSortOrderProperty(dateTimeEnd);
            dateTimeEnd.addListener((observable, oldValue, newValue) -> checkDateTimeEndConsistency());
        }
        return dateTimeEnd;
    }
    @Override
    public DateTimeEnd getDateTimeEnd() { return (dateTimeEnd == null) ? null : dateTimeEndProperty().get(); }
    private ObjectProperty<DateTimeEnd> dateTimeEnd;
    
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
    // TODO - MAKE A LIST
    public ObjectProperty<FreeBusyTime> freeBusyTimeProperty()
    {
        if (freeBusyTime == null)
        {
            freeBusyTime = new SimpleObjectProperty<>(this, PropertyType.FREE_BUSY_TIME.toString());
            orderer().registerSortOrderProperty(freeBusyTime);
        }
        return freeBusyTime;
    }
    private ObjectProperty<FreeBusyTime> freeBusyTime;
    public FreeBusyTime getFreeBusyTime() { return freeBusyTimeProperty().get(); }
    public void setFreeBusyTime(String freeBusyTime) { setFreeBusyTime(FreeBusyTime.parse(freeBusyTime)); }
    public void setFreeBusyTime(List<Pair<ZonedDateTime, TemporalAmount>> freeBusyTime) { setFreeBusyTime(new FreeBusyTime(freeBusyTime)); }
    public void setFreeBusyTime(FreeBusyTime freeBusyTime) { freeBusyTimeProperty().set(freeBusyTime); }
    public VFreeBusy withFreeBusyTime(FreeBusyTime freeBusyTime) { setFreeBusyTime(freeBusyTime); return this; }
    public VFreeBusy withFreeBusyTime(List<Pair<ZonedDateTime, TemporalAmount>> freeBusyTime) { setFreeBusyTime(freeBusyTime); return this; }
    public VFreeBusy withFreeBusyTime(String freeBusyTime) { PropertyType.FREE_BUSY_TIME.parse(this, freeBusyTime); return this; }
 
    /*
     * CONSTRUCTORS
     */
    public VFreeBusy() { super(); }
    
//    public VFreeBusy(String contentLines)
//    {
//        super(contentLines);
//    }
    
    /** Copy constructor */
    public VFreeBusy(VFreeBusy source)
    {
        super(source);
    }
    
//    @Override
//    public Reviser newRevisor() { return new ReviserVFreeBusy(this); }
        
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        String dtendError = VDateTimeEnd.errorsDateTimeEnd(this);
        if (dtendError != null)
        {
            errors.add(dtendError);
        }
//
//        if (getDateTimeEnd() != null)
//        {
//            if (getDateTimeStart() != null)
//            {
//                DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
//                DateTimeType endType = DateTimeUtilities.DateTimeType.of(getDateTimeEnd().getValue());
//                boolean isDateTimeEndMatch = startType == endType;
//                if (! isDateTimeEndMatch)
//                {
//                    errors.add("The value type of DTEND MUST be the same as the DTSTART property (" + endType + ", " + startType);
//                }
//            }
//        }
        return Collections.unmodifiableList(errors);
    }
        
//    @Override
//    public void checkDateTimeStartConsistency()
//    {
////        VComponentDateTimeEnd.super.checkDateTimeEndConsistency();
//    }
    
    /** Parse content lines into calendar component object */
    public static VFreeBusy parse(String contentLines)
    {
        VFreeBusy component = new VFreeBusy();
        component.parseContent(contentLines);
        return component;
    }
}
