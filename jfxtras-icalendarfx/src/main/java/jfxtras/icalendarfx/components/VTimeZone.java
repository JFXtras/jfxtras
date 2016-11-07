package jfxtras.icalendarfx.components;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.change.LastModified;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneURL;

/**
 * VTIMEZONE
 * Time Zone Component
 * RFC 5545 iCalendar 3.6.5. page 62
 * 
 * A time zone is unambiguously defined by the set of time
      measurement rules determined by the governing body for a given
      geographic area.  These rules describe, at a minimum, the base
      offset from UTC for the time zone, often referred to as the
      Standard Time offset.  Many locations adjust their Standard Time
      forward or backward by one hour, in order to accommodate seasonal
      changes in number of daylight hours, often referred to as Daylight
      Saving Time.  Some locations adjust their time by a fraction of an
      hour.  Standard Time is also known as Winter Time.  Daylight
      Saving Time is also known as Advanced Time, Summer Time, or Legal
      Time in certain countries.  The following table shows the changes
      in time zone rules in effect for New York City starting from 1967.
      Each line represents a description or rule for a particular
      observance.

                         Effective Observance Rule

     +-----------+--------------------------+--------+--------------+
     | Date      | (Date-Time)              | Offset | Abbreviation |
     +-----------+--------------------------+--------+--------------+
     | 1967-1973 | last Sun in Apr, 02:00   | -0400  | EDT          |
     |           |                          |        |              |
     | 1967-2006 | last Sun in Oct, 02:00   | -0500  | EST          |
     |           |                          |        |              |
     | 1974-1974 | Jan 6, 02:00             | -0400  | EDT          |
     |           |                          |        |              |
     | 1975-1975 | Feb 23, 02:00            | -0400  | EDT          |
     |           |                          |        |              |
     | 1976-1986 | last Sun in Apr, 02:00   | -0400  | EDT          |
     |           |                          |        |              |
     | 1987-2006 | first Sun in Apr, 02:00  | -0400  | EDT          |
     |           |                          |        |              |
     | 2007-*    | second Sun in Mar, 02:00 | -0400  | EDT          |
     |           |                          |        |              |
     | 2007-*    | first Sun in Nov, 02:00  | -0500  | EST          |
     +-----------+--------------------------+--------+--------------+

   Note: The specification of a global time zone registry is not
         addressed by this document and is left for future study.
         However, implementers may find the TZ database [TZDB] a useful
         reference.  It is an informal, public-domain collection of time
         zone information, which is currently being maintained by
         volunteer Internet participants, and is used in several
         operating systems.  This database contains current and
         historical time zone information for a wide variety of
         locations around the globe; it provides a time zone identifier
         for every unique time zone rule set in actual use since 1970,
         with historical data going back to the introduction of standard
         time.

      Interoperability between two calendaring and scheduling
      applications, especially for recurring events, to-dos or journal
      entries, is dependent on the ability to capture and convey date
      and time information in an unambiguous format.  The specification
      of current time zone information is integral to this behavior.

      If present, the "VTIMEZONE" calendar component defines the set of
      Standard Time and Daylight Saving Time observances (or rules) for
      a particular time zone for a given interval of time.  The
      "VTIMEZONE" calendar component cannot be nested within other
      calendar components.  Multiple "VTIMEZONE" calendar components can
      exist in an iCalendar object.  In this situation, each "VTIMEZONE"
      MUST represent a unique time zone definition.  This is necessary
      for some classes of events, such as airline flights, that start in
      one time zone and end in another.

      The "VTIMEZONE" calendar component MUST include the "TZID"
      property and at least one definition of a "STANDARD" or "DAYLIGHT"
      sub-component.  The "STANDARD" or "DAYLIGHT" sub-component MUST
      include the "DTSTART", "TZOFFSETFROM", and "TZOFFSETTO"
      properties.

      An individual "VTIMEZONE" calendar component MUST be specified for
      each unique "TZID" parameter value specified in the iCalendar
      object.  In addition, a "VTIMEZONE" calendar component, referred
      to by a recurring calendar component, MUST provide valid time zone
      information for all recurrence instances.

      Each "VTIMEZONE" calendar component consists of a collection of
      one or more sub-components that describe the rule for a particular
      observance (either a Standard Time or a Daylight Saving Time
      observance).  The "STANDARD" sub-component consists of a
      collection of properties that describe Standard Time.  The
      "DAYLIGHT" sub-component consists of a collection of properties
      that describe Daylight Saving Time.  In general, this collection
      of properties consists of:

      *  the first onset DATE-TIME for the observance;

      *  the last onset DATE-TIME for the observance, if a last onset is
         known;

      *  the offset to be applied for the observance;

      *  a rule that describes the day and time when the observance
         takes effect;

      *  an optional name for the observance.

      For a given time zone, there may be multiple unique definitions of
      the observances over a period of time.  Each observance is
      described using either a "STANDARD" or "DAYLIGHT" sub-component.
      The collection of these sub-components is used to describe the
      time zone for a given period of time.  The offset to apply at any
      given time is found by locating the observance that has the last
      onset date and time before the time in question, and using the
      offset value from that observance.

      The top-level properties in a "VTIMEZONE" calendar component are:

      The mandatory "TZID" property is a text value that uniquely
      identifies the "VTIMEZONE" calendar component within the scope of
      an iCalendar object.

      The optional "LAST-MODIFIED" property is a UTC value that
      specifies the date and time that this time zone definition was
      last updated.

      The optional "TZURL" property is a url value that points to a
      published "VTIMEZONE" definition.  "TZURL" SHOULD refer to a
      resource that is accessible by anyone who might need to interpret
      the object.  This SHOULD NOT normally be a "file" URL or other URL
      that is not widely accessible.

      The collection of properties that are used to define the
      "STANDARD" and "DAYLIGHT" sub-components include:

      The mandatory "DTSTART" property gives the effective onset date
      and local time for the time zone sub-component definition.
      "DTSTART" in this usage MUST be specified as a date with a local
      time value.

      The mandatory "TZOFFSETFROM" property gives the UTC offset that is
      in use when the onset of this time zone observance begins.
      "TZOFFSETFROM" is combined with "DTSTART" to define the effective
      onset for the time zone sub-component definition.  For example,
      the following represents the time at which the observance of
      Standard Time took effect in Fall 1967 for New York City:

       DTSTART:19671029T020000

       TZOFFSETFROM:-0400

      The mandatory "TZOFFSETTO" property gives the UTC offset for the
      time zone sub-component (Standard Time or Daylight Saving Time)
      when this observance is in use.

      The optional "TZNAME" property is the customary name for the time
      zone.  This could be used for displaying dates.

      The onset DATE-TIME values for the observance defined by the time
      zone sub-component is defined by the "DTSTART", "RRULE", and
      "RDATE" properties.

      The "RRULE" property defines the recurrence rule for the onset of
      the observance defined by this time zone sub-component.  Some
      specific requirements for the usage of "RRULE" for this purpose
      include:

      *  If observance is known to have an effective end date, the
         "UNTIL" recurrence rule parameter MUST be used to specify the
         last valid onset of this observance (i.e., the UNTIL DATE-TIME
         will be equal to the last instance generated by the recurrence
         pattern).  It MUST be specified in UTC time.

      *  The "DTSTART" and the "TZOFFSETFROM" properties MUST be used
         when generating the onset DATE-TIME values (instances) from the
         "RRULE".

      The "RDATE" property can also be used to define the onset of the
      observance by giving the individual onset date and times.  "RDATE"
      in this usage MUST be specified as a date with local time value,
      relative to the UTC offset specified in the "TZOFFSETFROM"
      property.

      The optional "COMMENT" property is also allowed for descriptive
      explanatory text.

   Example:  The following are examples of the "VTIMEZONE" calendar
      component:

      This is an example showing all the time zone rules for New York
      City since April 30, 1967 at 03:00:00 EDT.

       BEGIN:VTIMEZONE
       TZID:America/New_York
       LAST-MODIFIED:20050809T050000Z
       BEGIN:DAYLIGHT
       DTSTART:19670430T020000
       RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z
       TZOFFSETFROM:-0500
       TZOFFSETTO:-0400
       TZNAME:EDT
       END:DAYLIGHT
       BEGIN:STANDARD
       DTSTART:19671029T020000
       RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z
       TZOFFSETFROM:-0400
       TZOFFSETTO:-0500
       TZNAME:EST
       END:STANDARD
       BEGIN:DAYLIGHT
       DTSTART:19740106T020000
       RDATE:19750223T020000
       TZOFFSETFROM:-0500
       TZOFFSETTO:-0400
       TZNAME:EDT
       END:DAYLIGHT
       BEGIN:DAYLIGHT
       DTSTART:19760425T020000
       RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z
       TZOFFSETFROM:-0500
       TZOFFSETTO:-0400
       TZNAME:EDT
       END:DAYLIGHT
       BEGIN:DAYLIGHT
       DTSTART:19870405T020000
       RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z
       TZOFFSETFROM:-0500
       TZOFFSETTO:-0400
       TZNAME:EDT
       END:DAYLIGHT
       BEGIN:DAYLIGHT
       DTSTART:20070311T020000
       RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU
       TZOFFSETFROM:-0500
       TZOFFSETTO:-0400
       TZNAME:EDT
       END:DAYLIGHT
       BEGIN:STANDARD
       DTSTART:20071104T020000
       RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU
       TZOFFSETFROM:-0400
       TZOFFSETTO:-0500
       TZNAME:EST
       END:STANDARD
       END:VTIMEZONE
 * 
 * @author David Bal
 *
 */
public class VTimeZone extends VCommon<VTimeZone> implements VLastModified<VTimeZone>
{
    /**
     * STANDARD or DAYLIGHT
     * Subcomponent of VTimeZone
     * Either StandardTime or DaylightSavingsTime.
     * Both classes have identical methods.
     * 
     * @author David Bal
     * @see DaylightSavingTime
     * @see StandardTime
     *
     */
    public ObservableList<StandardOrDaylight<?>> getStandardOrDaylight() { return standardOrDaylight; }
    private ObservableList<StandardOrDaylight<?>> standardOrDaylight;
    public void setStandardOrDaylight(ObservableList<StandardOrDaylight<?>> standardOrDaylight)
    {
        if (standardOrDaylight != null)
        {
            orderer().registerSortOrderProperty(standardOrDaylight);
        } else
        {
            orderer().unregisterSortOrderProperty(this.standardOrDaylight);
        }
        this.standardOrDaylight = standardOrDaylight;
    }
    public VTimeZone withStandardOrDaylight(ObservableList<StandardOrDaylight<?>> standardOrDaylight) { setStandardOrDaylight(standardOrDaylight); return this; }
    public VTimeZone withStandardOrDaylight(StandardOrDaylight<?>...standardOrDaylight)
    {
        if (getStandardOrDaylight() == null)
        {
            setStandardOrDaylight(FXCollections.observableArrayList(standardOrDaylight));
        } else
        {
            getStandardOrDaylight().addAll(standardOrDaylight);
        }
        return this;
    }
    
    /**
    * LAST-MODIFIED
    * RFC 5545, 3.8.7.3, page 138
    * 
    * This property specifies the date and time that the
    * information associated with the calendar component was last
    * revised in the calendar store.
    *
    * Note: This is analogous to the modification date and time for a
    * file in the file system.
    * 
    * The value MUST be specified as a date with UTC time.
    * 
    * Example:
    * LAST-MODIFIED:19960817T133000Z
    */
    @Override
    public ObjectProperty<LastModified> dateTimeLastModifiedProperty()
    {
        if (lastModified == null)
        {
            lastModified = new SimpleObjectProperty<>(this, PropertyType.LAST_MODIFIED.toString());
            orderer().registerSortOrderProperty(lastModified);
        }
        return lastModified;
    }
    @Override
    public LastModified getDateTimeLastModified() { return (lastModified == null) ? null : dateTimeLastModifiedProperty().get(); }
    private ObjectProperty<LastModified> lastModified;
    
    /**
     * TZID
     * Time Zone Identifier
     * RFC 5545, 3.8.3.1, page 102
     * 
     * To specify the identifier for the time zone definition for
     * a time component in the property value
     * 
     * LIMITATION: globally unique time zones are stored as strings and the ZoneID is null.
     * Only the toString and toContentLine methods will display the original string.  Another
     * method to convert the unique time zone string into a ZoneId is required.
     * 
     * EXAMPLE:
     * TZID:America/Los_Angeles
     */
    public ObjectProperty<TimeZoneIdentifier> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(timeZoneIdentifier);
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifier> timeZoneIdentifier;
    public TimeZoneIdentifier getTimeZoneIdentifier() { return timeZoneIdentifierProperty().get(); }
    public void setTimeZoneIdentifier(TimeZoneIdentifier timeZoneIdentifier) { timeZoneIdentifierProperty().set(timeZoneIdentifier); }
    public void setTimeZoneIdentifier(String timeZoneIdentifier) { setTimeZoneIdentifier(TimeZoneIdentifier.parse(timeZoneIdentifier)); }
    public VTimeZone withTimeZoneIdentifier(TimeZoneIdentifier timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return this; }
    public VTimeZone withTimeZoneIdentifier(String timeZoneIdentifier) { PropertyType.TIME_ZONE_IDENTIFIER.parse(this, timeZoneIdentifier); return this; }

    /**
     * TZURL
     * Time Zone URL
     * RFC 5545, 3.8.3.5, page 106
     * 
     * This property provides a means for a "VTIMEZONE" component
     * to point to a network location that can be used to retrieve an up-
     * to-date version of itself
     * 
     * EXAMPLES:
     * TZURL:http://timezones.example.org/tz/America-Los_Angeles.ics
     */
    public ObjectProperty<TimeZoneURL> timeZoneURLProperty()
    {
        if (timeZoneURL == null)
        {
            timeZoneURL = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(timeZoneURL);
        }
        return timeZoneURL;
    }
    private ObjectProperty<TimeZoneURL> timeZoneURL;
    public TimeZoneURL getTimeZoneURL() { return timeZoneURLProperty().get(); }
    public void setTimeZoneURL(TimeZoneURL timeZoneURL) { timeZoneURLProperty().set(timeZoneURL); }
    public void setTimeZoneURL(String timeZoneURL) { setTimeZoneURL(TimeZoneURL.parse(timeZoneURL)); }
    public void setTimeZoneURL(URI timeZoneURL) { timeZoneURLProperty().set(new TimeZoneURL(timeZoneURL)); }
    public VTimeZone withTimeZoneURL(TimeZoneURL timeZoneURL) { setTimeZoneURL(timeZoneURL); return this; }
    public VTimeZone withTimeZoneURL(URI timeZoneURL) { setTimeZoneURL(new TimeZoneURL(timeZoneURL)); return this; }
    public VTimeZone withTimeZoneURL(String timeZoneURL) { PropertyType.TIME_ZONE_URL.parse(this, timeZoneURL); return this; }

    
    /*
     * CONSTRUCTORS
     */
    public VTimeZone() { super(); }
    
//    public VTimeZone(String contentLines)
//    {
//        super(contentLines);
//    }
    
    public VTimeZone(VTimeZone source)
    {
        super(source);
    }
    
//    @Override
//    public Reviser newRevisor() { return new ReviserVTimeZone(this); }

    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getTimeZoneIdentifier() == null)
        {
            errors.add("TZID is REQUIRED and MUST NOT occur more than once");
        }
        if (getStandardOrDaylight() != null && (getStandardOrDaylight().size() > 0))
        {
            getStandardOrDaylight().forEach(c -> errors.addAll(c.errors()));
        } else
        {
            errors.add("No STANDARD or DAYLIGHT subcomponents exist.  At least one STANDARD or DAYLIGHT subcomponent MUST occur");
        }
        return Collections.unmodifiableList(errors);
    }
    
    @Override
    void addSubcomponent(VComponent subcomponent)
    {
        if (subcomponent instanceof StandardOrDaylight<?>)
        {
            final ObservableList<StandardOrDaylight<?>> list;
            if (getStandardOrDaylight() == null)
            {
                list = FXCollections.observableArrayList();
                setStandardOrDaylight(list);
            } else
            {
                list = getStandardOrDaylight();
            }
            list.add((StandardOrDaylight<?>) subcomponent);
        } else
        {
            throw new IllegalArgumentException("Unsuported subcomponent type:" + subcomponent.getClass().getSimpleName() +
                  " found inside " + name() + " component");
        }
    }
    
    @Override
    public void copyInto(VParent destination)
    {
        super.copyInto(destination);
        ((VChild) destination).setParent(getParent());
        VTimeZone castDestination = (VTimeZone) destination;
        if (getStandardOrDaylight() != null)
        {
            if (castDestination.getStandardOrDaylight() == null)
            {
                castDestination.setStandardOrDaylight(FXCollections.observableArrayList());
            }
            getStandardOrDaylight().forEach(s -> 
            {
                final StandardOrDaylight<?> newSubcomponent;
                if (s instanceof StandardTime)
                {
                    newSubcomponent = new StandardTime((StandardTime) s);
                } else if (s instanceof DaylightSavingTime)
                {
                    newSubcomponent = new DaylightSavingTime((DaylightSavingTime) s);                    
                } else
                {
                    throw new IllegalArgumentException("Unsupported time zone subcomponent class:" + s.getClass());
                }
                castDestination.getStandardOrDaylight().add(newSubcomponent);
            });
        }
    }
    
//    /** copy STANDARD and DAYLIGHT subcomponents */
//    @Override
//    public void copyChildrenFrom(VParent source)
//    {
//        super.copyChildrenFrom(source);
//        VTimeZone castSource = (VTimeZone) source;
//        if (castSource.getStandardOrDaylight() != null)
//        {
//            if (getStandardOrDaylight() == null)
//            {
//                setStandardOrDaylight(FXCollections.observableArrayList());
//            }
//            castSource.getStandardOrDaylight().forEach(a -> 
//            {
//                final StandardOrDaylight<?> newComponent;
//                if (a instanceof StandardTime)
//                {
//                    newComponent = new StandardTime((StandardTime) a);
//                } else if (a instanceof DaylightSavingTime)
//                {
//                    newComponent = new DaylightSavingTime((DaylightSavingTime) a);                    
//                } else
//                {
//                    throw new IllegalArgumentException("Unsupported time zone subcomponent class:" + a.getClass());
//                }
//                getStandardOrDaylight().add(newComponent);
//            });
//        }
//    }
    
    @Override // include STANDARD or DAYLIGHT Subcomponents
    public boolean equals(Object obj)
    {
        VTimeZone testObj = (VTimeZone) obj;
        final boolean isVAlarmsEqual;
        if (getStandardOrDaylight() != null)
        {
            if (testObj.getStandardOrDaylight() == null)
            {
                isVAlarmsEqual = false;
            } else
            {
                isVAlarmsEqual = getStandardOrDaylight().equals(testObj.getStandardOrDaylight());
            }
        } else
        {
            isVAlarmsEqual = true;
        }
        return isVAlarmsEqual && super.equals(obj);
    }
    
    @Override // include STANDARD or DAYLIGHT Subcomponents
    public int hashCode()
    {
        int hash = super.hashCode();
        if (getStandardOrDaylight() != null)
        {
            Iterator<StandardOrDaylight<?>> i = getStandardOrDaylight().iterator();
            while (i.hasNext())
            {
                Object property = i.next();
                hash = (31 * hash) + property.hashCode();
            }
        }
        return hash;
    }

    /** Parse content lines into calendar component object */
    public static VTimeZone parse(String contentLines)
    {
        VTimeZone component = new VTimeZone();
        component.parseContent(contentLines);
        return component;
    }
}
