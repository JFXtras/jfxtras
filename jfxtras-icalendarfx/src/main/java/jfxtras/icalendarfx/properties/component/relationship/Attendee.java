package jfxtras.icalendarfx.properties.component.relationship;

import java.net.URI;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.CommonName;
import jfxtras.icalendarfx.parameters.Delegatees;
import jfxtras.icalendarfx.parameters.Delegators;
import jfxtras.icalendarfx.parameters.DirectoryEntry;
import jfxtras.icalendarfx.parameters.GroupMembership;
import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.parameters.ParticipationRole;
import jfxtras.icalendarfx.parameters.ParticipationStatus;
import jfxtras.icalendarfx.parameters.RSVP;
import jfxtras.icalendarfx.parameters.SentBy;

/**
 * <h2>3.8.4.1.  Attendee</h2>

   <p>Property Name:  ATTENDEE</p>

   <p>Purpose:  This property defines an "Attendee" within a calendar
      component.</p>

   <p>Value Type:  CAL-ADDRESS</p>

   <p>Property Parameters:  IANA, {@link NonStandardParameter non-standard},
      {@link Language language}, {@link CalendarUser calendar user type},
      {@link GroupMembership group or list membership}, {@link ParticipationRole participation role},
      {@link ParticipationStatus participation status}, {@link RSVP RSVP expectation},
      {@link Delegatees delegatee}, {@link Delegators delegator}, {@link SentBy sent by},
      {@link CommonName common name}, or {@link DirectoryEntry directory entry reference} property parameters can be
      specified on this property.</p>

   <p>Conformance:  This property MUST be specified in an iCalendar object
      that specifies a group-scheduled calendar entity.  This property
      MUST NOT be specified in an iCalendar object when publishing the
      calendar information (e.g., NOT in an iCalendar object that
      specifies the publication of a calendar user's busy time, event,
      to-do, or journal).  This property is not specified in an
      iCalendar object that specifies only a time zone definition or
      that defines calendar components that are not group-scheduled
      components, but are components only on a single user's calendar.</p>

   <p>Description:  This property MUST only be specified within calendar
      components to specify participants, non-participants, and the
      chair of a group-scheduled calendar entity.  The property is
      specified within an "EMAIL" category of the {@link VAlarm VALARM} calendar
      component to specify an email address that is to receive the email
      type of iCalendar alarm.</p>

      <p>The property parameter {@link CommonName CN} is for the common or displayable name
      associated with the calendar address; {@link ParticipationRole ROLE}, for the intended
      role that the attendee will have in the calendar component;
      {@link ParticipationStatus PARTSTAT}, for the status of the attendee's participation;
      {@link RSVP}, for indicating whether the favor of a reply is requested;
      {@link CalendarUser CUTYPE, to indicate the type of calendar user; {@link GroupMembership MEMBER}, to
      indicate the groups that the attendee belongs to; {@link Delegatees DELEGATED-TO},
      to indicate the calendar users that the original request was
      delegated to; and {@link Delegators DELEGATED-FROM}, to indicate whom the request
      was delegated from; {@link SentBy SENT-BY}, to indicate whom is acting on
      behalf of the {@link Attendee ATTENDEE}; and {@link DirectoryEntry DIR}, to indicate the URI that
      points to the directory information corresponding to the attendee.
      These property parameters can be specified on an {@link Attendee ATTENDEE}
      property in either a {@link VEvent VEVENT}, {@link VTodo VTODO}, or {@link VJournal VJOURNAL} calendar
      component.  They MUST NOT be specified in an {@link Attendee ATTENDEE} property
      in a {@link VFreeBusy VFREEBUSY} or {@link VAlarm VALARM} calendar component.  If the
      {@link Language LANGUAGE} property parameter is specified, the identified
      language applies to the {@link CommonName CN} parameter.</p>

      <p>A recipient delegated a request MUST inherit the {@link RSVP} and {@link ParticipationRole ROLE}
      values from the attendee that delegated the request to them.</p>

      <p>Multiple attendees can be specified by including multiple
      {@link Attendee ATTENDEE} properties within the calendar component.</p>

  <p>Format Definition:  This property is defined by the following notation:
  <ul>
  <li>attendee
    <ul>
    <li>"ATTENDEE" attparam ":" cal-address CRLF
    </ul>
  <li>attparam
    <ul>
    <li>The following are OPTIONAL, but MUST NOT occur more than once.
      <ul>
      <li>";" {@link CalendarUser Calendar user type}
      <li>";" {@link Delegators}
      <li>";" {@link Delegatees}
      <li>";" {@link CommonName Common name}
      <li>";" {@link DirectoryEntry Directory entry}
      <li>";" {@link GroupMembership Group or list membership}
      <li>";" {@link Language Language for text}
      <li>";" {@link RSVP}
      <li>";" {@link ParticipationRole Participation role}
      <li>";" {@link ParticipationStatus Participation status}
      <li>";" {@link SentBy Sent by}
      </ul>
      <li>The following are OPTIONAL, and MAY occur more than once.
      <ul>
        <li>other-param
          <ul>
          <li>";" {@link NonStandardParameter}
          <li>";" {@link IANAParameter}
          </ul>
      </ul>
    </ul>
  </ul>
  
  <p>Example:  The following is an example of this property:
  <ul>
    <li>The following are examples of this property's use for a to-do:
      <ul>
      <li>ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":mailto:joecool@example.com
      <li>ATTENDEE;DELEGATED-FROM="mailto:immud@example.com":mailto:ildoit@example.com
      </ul>
    <li>The following is an example of this property used for specifying multiple attendees to an event:
      <ul>
      <li>ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=TENTATIVE;CN=Henry Cabot:mailto:hcabot@example.com
      <li>ATTENDEE;ROLE=REQ-PARTICIPANT;DELEGATED-FROM="mailto:bob@example.com";PARTSTAT=ACCEPTED;CN=Jane Doe:mailto:jdoe@example.com
      </ul>
    <li>The following is an example of this property with a URI to the directory information associated with the attendee:
      <ul>
      <li>ATTENDEE;CN=John Smith;DIR="ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)":mailto:jimdo@example.com
      </ul>
    <li>The following is an example of this property with "delegatee" and "delegator" information for an event:
      <ul>
      <li>ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=TENTATIVE;DELEGATED-FROM="mailto:iamboss@example.com";CN=Henry Cabot:mailto:hcabot@example.com
      <li>ATTENDEE;ROLE=NON-PARTICIPANT;PARTSTAT=DELEGATED;DELEGATED-TO="mailto:hcabot@example.com";CN=The Big Cheese:mailto:iamboss@example.com
      <li>ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe:mailto:jdoe@example.com
      </ul>
    <li>The following is an example of this property's use when another calendar user is acting on behalf of the "Attendee":
      <ul>
      <li>ATTENDEE;SENT-BY=mailto:jan_doe@example.com;CN=John Smith:mailto:jsmith@example.com
      </ul>
  </ul>
  </p>
  RFC 5545                       iCalendar                  September 2009
 * @author David Bal
 */
public class Attendee extends PropertyBaseAttendee<URI, Attendee>
{
    /** Create new Attendee with property value set to input parameter */
    public Attendee(URI value)
    {
        super(value);
    }
    
    /** Create deep copy of source Attendee */
    public Attendee(Attendee source)
    {
        super(source);
    }
    
    /** Create default Attendee with no value set */
    Attendee()
    {
        super();
    }

    /** Create new Attendee by parsing unfolded calendar content */
    public static Attendee parse(String unfoldedContent)
    {
        Attendee property = new Attendee();
        property.parseContent(unfoldedContent);
        URI.class.cast(property.getValue()); // ensure value class type matches parameterized type
        return property;
    }
}
