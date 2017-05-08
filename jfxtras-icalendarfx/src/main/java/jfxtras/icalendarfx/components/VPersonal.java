package jfxtras.icalendarfx.components;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VAttendee;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.icalendarfx.utilities.Callback;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * Components with the following properties:
 * ATTENDEE, DTSTAMP, ORGANIZER, REQUEST-STATUS, UID, URL
 * 
 * @author David Bal
 *
 * @param <T> - implementation subclass
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 */
public abstract class VPersonal<T> extends VPrimary<T> implements VAttendee<T>
{
    /**
     * ATTENDEE: Attendee
     * RFC 5545 iCalendar 3.8.4.1 page 107
     * This property defines an "Attendee" within a calendar component.
     * 
     * Examples:
     * ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":
     *  mailto:joecool@example.com
     * ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe
     *  :mailto:jdoe@example.com
     */
    private List<Attendee> attendees;
    @Override
    public List<Attendee> getAttendees() { return attendees; }
    @Override
    public void setAttendees(List<Attendee> attendees)
    {
    	if (this.attendees != null)
    	{
    		this.attendees.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.attendees = attendees;
    	if (attendees != null)
    	{
    		attendees.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    private DateTimeStamp dateTimeStamp;
    public DateTimeStamp getDateTimeStamp() { return dateTimeStamp; }
    public void setDateTimeStamp(String dateTimeStamp) { setDateTimeStamp(DateTimeStamp.parse(dateTimeStamp)); }
    public void setDateTimeStamp(DateTimeStamp dateTimeStamp)
    {
    	orderChild(this.dateTimeStamp, dateTimeStamp);
    	this.dateTimeStamp = dateTimeStamp;
	}
    public void setDateTimeStamp(ZonedDateTime dateTimeStamp) { setDateTimeStamp(new DateTimeStamp(dateTimeStamp)); }
    public T withDateTimeStamp(ZonedDateTime dateTimeStamp)
    {
        setDateTimeStamp(dateTimeStamp);
        return (T) this;
    }
    public T withDateTimeStamp(String dateTimeStamp)
    {
        setDateTimeStamp(dateTimeStamp);
        return (T) this;
    }
    public T withDateTimeStamp(DateTimeStamp dateTimeStamp)
    {
        setDateTimeStamp(dateTimeStamp);
        return (T) this;
    }

    /**
     * ORGANIZER: Organizer
     * RFC 5545 iCalendar 3.8.4.3 page 111
     * This property defines the organizer for a calendar component
     * 
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     */
    public Organizer getOrganizer() { return organizer; }
    private Organizer organizer;
    public void setOrganizer(Organizer organizer)
    {
    	orderChild(this.organizer, organizer);
    	this.organizer = organizer;
	}
    public void setOrganizer(String organizer) { setOrganizer(Organizer.parse(organizer)); }
    public T withOrganizer(String organizer)
    {
        setOrganizer(organizer);
        return (T) this;
    }
    public T withOrganizer(Organizer organizer)
    {
        setOrganizer(organizer);
        return (T) this;
    }

    /**
     * REQUEST-STATUS: Request Status
     * RFC 5545 iCalendar 3.8.8.3 page 141
     * This property defines the status code returned for a scheduling request.
     * 
     * Examples:
     * REQUEST-STATUS:2.0;Success
     * REQUEST-STATUS:3.7;Invalid calendar user;ATTENDEE:
     *  mailto:jsmith@example.com
     * 
     */
    public List<RequestStatus> getRequestStatus() { return requestStatus; }
    private List<RequestStatus> requestStatus;
    public void setRequestStatus(List<RequestStatus> requestStatus)
    {
    	if (this.requestStatus != null)
    	{
    		this.requestStatus.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.requestStatus = requestStatus;
    	if (requestStatus != null)
    	{
    		requestStatus.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    public T withRequestStatus(List<RequestStatus> requestStatus)
    {
    	if (getRequestStatus() == null)
    	{
    		setRequestStatus(new ArrayList<>());
    	}
    	getRequestStatus().addAll(requestStatus);
    	if (requestStatus != null)
    	{
    		requestStatus.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    public T withRequestStatus(String...requestStatus)
    {
        List<RequestStatus> list = Arrays.stream(requestStatus)
                .map(c -> RequestStatus.parse(c))
                .collect(Collectors.toList());
        return withRequestStatus(list);
    }
    public T withRequestStatus(RequestStatus...requestStatus)
    {
    	return withRequestStatus(Arrays.asList(requestStatus));
    }

    /**
     * UID, Unique identifier
     * RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * required property
     * 
     * Example:
     * UID:19960401T080045Z-4000F192713-0052@example.com
     */
    private UniqueIdentifier uniqueIdentifier;
    public UniqueIdentifier getUniqueIdentifier() { return uniqueIdentifier; }
    public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier)
    {
    	orderChild(this.uniqueIdentifier, uniqueIdentifier);
    	this.uniqueIdentifier = uniqueIdentifier;
	}
    public void setUniqueIdentifier(String uniqueIdentifier) { setUniqueIdentifier(UniqueIdentifier.parse(uniqueIdentifier)); }
    /** Set uniqueIdentifier by calling uidGeneratorCallback */
    public void setUniqueIdentifier() { setUniqueIdentifier(getUidGeneratorCallback().call(null)); }
    public T withUniqueIdentifier(String uniqueIdentifier)
    {
        setUniqueIdentifier(uniqueIdentifier);
        return (T) this;
    }
    public T withUniqueIdentifier(UniqueIdentifier uniqueIdentifier)
    {
        setUniqueIdentifier(uniqueIdentifier);
        return (T) this;
    }
    /** Assign UID by using UID generator callback */
    public T withUniqueIdentifier()
    {
        setUniqueIdentifier(getUidGeneratorCallback().call(null));
        return (T) this;
    }
    
   
    /** Callback for creating unique uid values  */
    public Callback<Void, String> getUidGeneratorCallback()
    {
    	return uidGeneratorCallback;
	}
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback)
    {
    	this.uidGeneratorCallback = uidCallback;
	}
    /** set UID callback generator.  This MUST be set before using the no-arg withUniqueIdentifier if
     * not using default callback.
     */
    public T withUidGeneratorCallback(Callback<Void, String> uidCallback)
    {
        setUidGeneratorCallback(uidCallback);
        return (T) this;
    }

    
    /**
     * URL: Uniform Resource Locator
     * RFC 5545 iCalendar 3.8.4.6 page 116
     * This property defines a Uniform Resource Locator (URL)
     * associated with the iCalendar object
     * 
     * Example:
     * URL:http://example.com/pub/calendars/jsmith/mytime.ics
     */
    public UniformResourceLocator getURL() { return url; }
    private UniformResourceLocator url;
    public void setURL(UniformResourceLocator url)
    {
    	orderChild(this.url, url);
    	this.url = url;
	};
    public void setURL(String url) { setURL(UniformResourceLocator.parse(url)); };
    public void setURL(URI url) { setURL(new UniformResourceLocator(url)); };
    public T withURL(String url)
    {
        setURL(url);
        return (T) this;
    }
    public T withURL(URI url)
    {
        setURL(url);
        return (T) this;
    }
    public T withURL(UniformResourceLocator url)
    {
        setURL(url);
        return (T) this;
    }
    
    /*
     * CONSTRUCTORS
     */
    VPersonal() { super(); }
    
    public VPersonal(VPersonal<T> source)
    {
        super(source);
    }
    
//    @Override
//    public Map<VElement, List<String>> parseContent(Iterator<String> lineIterator, boolean useRequestStatus)
//    {
//        Map<VElement, List<String>> statusMessages = super.parseContent(lineIterator, useRequestStatus);
//        if (useRequestStatus)
//        { // Set REQUEST-STATUS for each message
//        	statusMessages.entrySet()
//	            .stream()
//	            .flatMap(e -> e.getValue().stream())
//	            .forEach(e -> addChild(RequestStatus.parse(e)));
//        }
//        return statusMessages;
//    }
    
//    @Override
//    @Override
//	public List<Message> parseContent(Iterator<String> lineIterator)
//    {
//    	List<Message> statusMessages = super.parseContent(lineIterator);
////        if (useRequestStatus)
////        { // Set REQUEST-STATUS for each message
////        	statusMessages.entrySet()
////	            .stream()
////	            .flatMap(e -> e.getValue().stream())
////	            .forEach(e -> addChild(RequestStatus.parse(e)));
////        }
//        return statusMessages;
//    }
    
    @Override
    public List<String> errors()
    {
//        List<String> errors = new ArrayList<>();
        List<String> errors = super.errors();
        if (getDateTimeStamp() == null)
        {
            errors.add("DTSTAMP is not present.  DTSTAMP is REQUIRED and MUST NOT occur more than once");
        }
        if (getUniqueIdentifier() == null)
        {
            errors.add("UID is not present.  UID is REQUIRED and MUST NOT occur more than once");
        }
        return errors;
    }
}
