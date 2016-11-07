package jfxtras.icalendarfx.components;

import java.util.List;
import java.util.Map;

import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.Method;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.utilities.UnfoldingStringIterator;

/**
 * <h2>RFC 5545, 3.6. Calendar Components</h2>
 * 
 * <p>The body of the iCalendar object consists of a sequence of calendar
 * properties and one or more calendar components.  The calendar
 * properties are attributes that apply to the calendar object as a
 * whole.  The calendar components are collections of properties that
 * express a particular calendar semantic.  For example, the calendar
 * component can specify an event, a to-do, a journal entry, time zone
 * information, free/busy time information, or an alarm.</p>
 *
 * <p>The body of the iCalendar object is defined by the following
 * notation:
 *
 *<ul>
 *<li>icalbody   = calprops component
 *<li>calprops
 *  <ul>
 *  <li>The following are REQUIRED, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link ProductIdentifier PRODID}
 *    <li>{@link Version VERSION}
 *    </ul>
 *  </ul>
 *  <ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link CalendarScale CALSCALE}
 *    <li>{@link Method METHOD}
 *    </ul>
 *  </ul>
 *  <ul>
 *  <li>The following are OPTIONAL, and MAY occur more than once.
 *    <ul>
 *    <li>{@link NonStandardProperty X-PROP}
 *    <li>IANA-PROP
 *    </ul>
 *  </ul>
 *<li>component
 *  <ul>
 *  <li>{@link VEvent VEVENT}
 *  <li>{@link VTodo VTODO}
 *  <li>{@link VJournal VJOURNAL}
 *  <li>{@link VFreeBusy VFREEBUSY}
 *  <li>{@link VTimeZone VTIMEZONE}
 *  <li>IANA-Comp (not implemented)
 *  <li>X-Comp (not implemented)
 *  </ul>
 *</ul>
 *
 * <P>An iCalendar object MUST include the {@link ProductIdentifier PRODID} and {@link Version VERSION} calendar
   properties.  In addition, it MUST include at least one calendar
   component.  Special forms of iCalendar objects are possible to
   publish just busy time (i.e., only a {@link VFreeBusy VFREEBUSY} calendar component)
   or time zone (i.e., only a {@link VTimeZone VTIMEZONE} calendar component)
   information.  In addition, a complex iCalendar object that is used to
   capture a complete snapshot of the contents of a calendar is possible
   (e.g., composite of many different calendar components).  More
   commonly, an iCalendar object will consist of just a single {@link VEvent VEVENT},
   {@link VTodo VTODO}, or {@link VJournal VJOURNAL} calendar component.  Applications MUST ignore
   x-comp and iana-comp values they don't recognize.  Applications that
   support importing iCalendar objects SHOULD support all of the
   component types defined in this document, and SHOULD NOT silently
   drop any components as that can lead to user data loss.</P
 * 
 * @author David Bal
 */
public interface VComponent extends VParent, VChild
{   
    /**
     * <p>Returns content line for a calendar component.  See RFC 5545 3.4
     * Contains component properties with their values and any parameters.</p>
     * <p>
     * The following is a example of iCalendar content text:
     *  <ul>
     *  BEGIN:VEVENT<br>
     *  UID:19970610T172345Z-AF23B2@example.com<br>
     *  DTSTAMP:19970610T172345Z<br>
     *  DTSTART:19970714T170000Z<br>
     *  DTEND:19970715T040000Z<br>
     *  SUMMARY:Bastille Day Party<br>
     *  END:VEVENT
     *  </ul>
     *  </p>
     * 
     * @return - the component content lines
     */
    @Override
    String toContent();
    
    /** Parse a VComponent from a {@code Iterator<String>}.
     *  Returns list of error strings for {@link RequestStatus} if collectErrorList is true,
     *  otherwise returns an empty list
     * 
     * @param contentLines  calendar component lines to parse
     * @param collectErrorMessages  true causes return List to contain error messages, false causes empty list
     * @return  list of error messages if collectErrorMessages is true, otherwise empty list
     */
    Map<VElement, List<String>> parseContent(UnfoldingStringIterator contentLines, boolean collectErrorMessages);
}
