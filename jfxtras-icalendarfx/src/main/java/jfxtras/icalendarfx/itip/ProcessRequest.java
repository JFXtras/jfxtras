package jfxtras.icalendarfx.itip;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;

/** 
 * 
 <h2>3.2.2.  REQUEST</h2>
   <pre>

   The "REQUEST" method in a "VEVENT" component provides the following
   scheduling functions:

   o  Invite "Attendees" to an event.

   o  Reschedule an existing event.

   o  Response to a "REFRESH" request.

   o  Update the details of an existing event, without rescheduling it.

   o  Update the status of "Attendees" of an existing event, without
      rescheduling it.

   o  Reconfirm an existing event, without rescheduling it.

   o  Forward a "VEVENT" to another uninvited CU.

   o  For an existing "VEVENT" calendar component, delegate the role of
      "Attendee" to another CU.

   o  For an existing "VEVENT" calendar component, change the role of
      "Organizer" to another CU.

   The "Organizer" originates the "REQUEST".  The recipients of the
   "REQUEST" method are the CUs invited to the event, the "Attendees".
   "Attendees" use the "REPLY" method to convey attendance status to the
   "Organizer".

   The "UID" and "SEQUENCE" properties are used to distinguish the
   various uses of the "REQUEST" method.  If the "UID" property value in
   the "REQUEST" is not found on the recipient's calendar, then the
   "REQUEST" is for a new "VEVENT" calendar component.  If the "UID"
   property value is found on the recipient's calendar, then the
   "REQUEST" is for a rescheduling, an update, or a reconfirmation of
   the "VEVENT" calendar component.

   For the "REQUEST" method, multiple "VEVENT" components in a single
   iCalendar object are only permitted for components with the same
   "UID" property.  That is, a series of recurring events may have
   instance-specific information.  In this case, multiple "VEVENT"
   components are needed to express the entire series.

   This method type is an iCalendar object that conforms to the
   following property constraints:

             +----------------------------------------------+
             | Constraints for a METHOD:REQUEST of a VEVENT |
             +----------------------------------------------+

   +--------------------+----------+-----------------------------------+
   | Component/Property | Presence | Comment                           |
   +--------------------+----------+-----------------------------------+
   | METHOD             | 1        | MUST be REQUEST.                  |
   |                    |          |                                   |
   | VEVENT             | 1+       | All components MUST have the same |
   |                    |          | UID.                              |
   |   ATTENDEE         | 1+       |                                   |
   |   DTSTAMP          | 1        |                                   |
   |   DTSTART          | 1        |                                   |
   |   ORGANIZER        | 1        |                                   |
   |   SEQUENCE         | 0 or 1   | MUST be present if value is       |
   |                    |          | greater than 0; MAY be present if |
   |                    |          | 0.                                |
   |   SUMMARY          | 1        | Can be null.                      |
   |   UID              | 1        |                                   |
   |   ATTACH           | 0+       |                                   |
   |   CATEGORIES       | 0+       |                                   |
   |   CLASS            | 0 or 1   |                                   |
   |   COMMENT          | 0+       |                                   |
   |   CONTACT          | 0+       |                                   |
   |   CREATED          | 0 or 1   |                                   |
   |   DESCRIPTION      | 0 or 1   | Can be null.                      |
   |   DTEND            | 0 or 1   | If present, DURATION MUST NOT be  |
   |                    |          | present.                          |
   |   DURATION         | 0 or 1   | If present, DTEND MUST NOT be     |
   |                    |          | present.                          |
   |   EXDATE           | 0+       |                                   |
   |   GEO              | 0 or 1   |                                   |
   |   LAST-MODIFIED    | 0 or 1   |                                   |
   |   LOCATION         | 0 or 1   |                                   |
   |   PRIORITY         | 0 or 1   |                                   |
   |   RDATE            | 0+       |                                   |
   |   RECURRENCE-ID    | 0 or 1   | Only if referring to an instance  |
   |                    |          | of a recurring calendar           |
   |                    |          | component.  Otherwise, it MUST    |
   |                    |          | NOT be present.                   |
   |   RELATED-TO       | 0+       |                                   |
   |   REQUEST-STATUS   | 0        |                                   |
   |   RESOURCES        | 0+       |                                   |
   |   RRULE            | 0 or 1   |                                   |
   |   STATUS           | 0 or 1   | MAY be one of                     |
   |                    |          | TENTATIVE/CONFIRMED.              |
   |   TRANSP           | 0 or 1   |                                   |
   |   URL              | 0 or 1   |                                   |
   |   IANA-PROPERTY    | 0+       |                                   |
   |   X-PROPERTY       | 0+       |                                   |
   |                    |          |                                   |
   |   VALARM           | 0+       |                                   |
   |                    |          |                                   |
   | VTIMEZONE          | 0+       | MUST be present if any date/time  |
   |                    |          | refers to a timezone.             |
   |                    |          |                                   |
   | IANA-COMPONENT     | 0+       |                                   |
   | X-COMPONENT        | 0+       |                                   |
   |                    |          |                                   |
   | VFREEBUSY          | 0        |                                   |
   |                    |          |                                   |
   | VJOURNAL           | 0        |                                   |
   |                    |          |                                   |
   | VTODO              | 0        |                                   |
   +--------------------+----------+-----------------------------------+
   </pre>
   <p>
   This default class to process requests has limited capability.  It will process changes made
   by the organizer, but it will NOT do the following:
   <ul>
   <li>Invite "Attendees" to an event.
   <li>Reconfirm an existing event, without rescheduling it
   <li>
   If those features are desired then extend this class and add them.
 * @author David Bal   
 */
public class ProcessRequest extends ProcessPublish
{
    @Override
    public List<String> process(VCalendar mainVCalendar, VCalendar iTIPMessage)
    {
        List<String> log = new ArrayList<>();
        // Check UID from iTIPMessage is already present
        Iterator<VComponent> componentIterator = iTIPMessage.getAllVComponents().iterator();
        while (componentIterator.hasNext())
        {
            VPersonal<?> myComponent = (VPersonal<?>) componentIterator.next();
            if (myComponent.getAttendees() != null)
            {
                throw new IllegalArgumentException("Can't process REQUEST, handling Attendees is not implemented");
            }
            UniqueIdentifier uid = myComponent.getUniqueIdentifier();
            if (uid == null)
            {
                throw new IllegalArgumentException("Can't process REQUEST, VComponent has null UID");
            }
            if (mainVCalendar.uidComponentsMap().get(uid.getValue()) == null)
            {
                throw new IllegalArgumentException("Can't process REQUEST, VComponent UID is not present in main VCalendar");
            }
        }
        log.addAll(super.process(mainVCalendar, iTIPMessage));
        return log;
    }
}
