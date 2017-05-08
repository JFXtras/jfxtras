package jfxtras.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDescribable;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VLastModified;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.components.VRepeatable;
import jfxtras.icalendarfx.components.VRepeatableBase;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.icalendarfx.properties.component.change.LastModified;
import jfxtras.icalendarfx.properties.component.change.Sequence;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.icalendarfx.properties.component.descriptive.Status;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.icalendarfx.properties.component.relationship.Contact;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * <p>{@link VComponent} with the following properties
 * <ul>
 * <li>{@link Attachment ATTACH}
 * <li>{@link Categories CATEGORIES}
 * <li>{@link Classification CLASS}
 * <li>{@link Contact CONTACT}
 * <li>{@link DateTimeCreated CREATED}
 * <li>{@link ExceptionDates EXDATE}
 * <li>{@link LastModified LAST-MODIFIED}
 * <li>{@link RecurrenceId RECURRENCE-ID}
 * <li>{@link RelatedTo RELATED-TO}
 * <li>{@link RecurrenceRule RRULE}
 * <li>{@link Sequence SEQUENCE}
 * <li>{@link Status STATUS}
 * <li>{@link Summary SUMMARY}
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 * @param <T> concrete subclass
 */
public abstract class VDisplayable<T> extends VPersonal<T> implements VRepeatable<T>, VDescribable<T>, VLastModified<T>
{
    /**
     * This property provides the capability to associate a document object with a calendar component.
     * 
     *<p>Example:  The following is an example of this property:
     *<ul>
     *<li>ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
     *<li>ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/<br>
     *  reports/r-960812.ps
     *</ul>
     *</p>
     */
    @Override
    public List<Attachment<?>> getAttachments() { return attachments; }
    private List<Attachment<?>> attachments;
    @Override
    public void setAttachments(List<Attachment<?>> attachments)
    {
    	if (this.attachments != null)
    	{
    		this.attachments.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.attachments = attachments;
    	if (attachments != null)
    	{
    		attachments.forEach(e -> orderChild(e));
    	}
	}
    
    /**
     * CATEGORIES:
     * RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    public List<Categories> getCategories() { return categories; }
    private List<Categories> categories;
    public void setCategories(List<Categories> categories)
    {
    	if (this.categories != null)
    	{
    		this.categories.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.categories = categories;
    	if (categories != null)
    	{
    		categories.forEach(e -> orderChild(e));
    	}
	}
    public T withCategories(List<Categories> categories)
    {
    	if (getCategories() == null)
    	{
    		setCategories(new ArrayList<>());
    	}
    	getCategories().addAll(categories);
    	if (categories != null)
    	{
    		categories.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    public T withCategories(String...categories)
    {
        return withCategories(new Categories(categories));
    }
    public T withCategories(Categories...categories)
    {
    	withCategories(new ArrayList<>(Arrays.asList(categories)));
        return (T) this;
    }
    
    /**
     * CLASS
     * Classification
     * RFC 5545, 3.8.1.3, page 82
     * 
     * This property defines the access classification for a calendar component.
     * 
     * Example:
     * CLASS:PUBLIC
     */
    public Classification getClassification() { return classification; }
    private Classification classification;
    public void setClassification(String classification) { setClassification(Classification.parse(classification)); }
    public void setClassification(Classification classification)
    {
    	orderChild(this.classification, classification);
    	this.classification = classification;
	}
    public void setClassification(ClassificationType classification) { setClassification(new Classification(classification)); }
    public T withClassification(Classification classification)
    {
        setClassification(classification);
        return (T) this;
    }
    public T withClassification(ClassificationType classification)
    {
        setClassification(classification);
        return (T) this;
    }
    public T withClassification(String classification)
    {
        setClassification(classification);
        return (T) this;
    }
    
    /**
     * CONTACT:
     * RFC 5545 iCalendar 3.8.4.2. page 109
     * This property is used to represent contact information or
     * alternately a reference to contact information associated with the
     * calendar component.
     * 
     * Example:
     * CONTACT;ALTREP="ldap://example.com:6666/o=ABC%20Industries\,
     *  c=US???(cn=Jim%20Dolittle)":Jim Dolittle\, ABC Industries\,
     *  +1-919-555-1234
     */
    public List<Contact> getContacts() { return contacts; }
    private List<Contact> contacts;
    public void setContacts(List<Contact> contacts)
    {
    	if (this.contacts != null)
    	{
    		this.contacts.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.contacts = contacts;
    	if (contacts != null)
    	{
    		contacts.forEach(c -> orderChild(c));
    	}
	}
    public T withContacts(List<Contact> contacts)
    {
    	if (getContacts() == null)
    	{
    		setContacts(new ArrayList<>());
    	}
    	getContacts().addAll(contacts);
    	if (contacts != null)
    	{
    		contacts.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    public T withContacts(String...contacts)
    {
        List<Contact> list = Arrays.stream(contacts)
                .map(c -> Contact.parse(c))
                .collect(Collectors.toList());
        return withContacts(list);
    }
    public T withContacts(Contact...contacts)
    {
    	return withContacts(Arrays.asList(contacts));
    }
    
    /**
     * CREATED: Date-Time Created
     * RFC 5545 iCalendar 3.8.7.1 page 136
     * This property specifies the date and time that the calendar information was created.
     * This is analogous to the creation date and time for a file in the file system.
     * The value MUST be specified in the UTC time format.
     * 
     * Example:
     * CREATED:19960329T133000Z
     */
    public DateTimeCreated getDateTimeCreated() { return dateTimeCreated; }
    private DateTimeCreated dateTimeCreated;
    public void setDateTimeCreated(String dateTimeCreated) { setDateTimeCreated(DateTimeCreated.parse(dateTimeCreated)); }
    public void setDateTimeCreated(DateTimeCreated dateTimeCreated)
    {
    	orderChild(this.dateTimeCreated, dateTimeCreated);
    	this.dateTimeCreated = dateTimeCreated;
	}
    public void setDateTimeCreated(ZonedDateTime dateTimeCreated) { setDateTimeCreated(new DateTimeCreated(dateTimeCreated)); }
    public T withDateTimeCreated(ZonedDateTime dateTimeCreated)
    {
        setDateTimeCreated(dateTimeCreated);
        return (T) this;
    }
    public T withDateTimeCreated(String dateTimeCreated)
    {
        setDateTimeCreated(dateTimeCreated);
        return (T) this;
    }
    public T withDateTimeCreated(DateTimeCreated dateTimeCreated)
    {
        setDateTimeCreated(dateTimeCreated);
        return (T) this;
    }
    
   /** 
    * EXDATE
    * Exception Date-Times
    * RFC 5545 iCalendar 3.8.5.1, page 117.
    * 
    * This property defines the list of DATE-TIME exceptions for
    * recurring events, to-dos, journal entries, or time zone definitions.
    */
    public List<ExceptionDates> getExceptionDates() { return exceptionDates; }
    private List<ExceptionDates> exceptionDates;
    public void setExceptionDates(List<ExceptionDates> exceptionDates)
    {
    	if (this.exceptionDates != null)
    	{
    		this.exceptionDates.forEach(e -> orderChild(e, null)); // remove old elements
    	}
        this.exceptionDates = exceptionDates;
        if (exceptionDates != null)
        {
        	exceptionDates.forEach(e -> orderChild(e)); // order new elements
        }
    }
    public T withExceptionDates(List<ExceptionDates> exceptions)
    {
    	if (getExceptionDates() == null)
    	{
    		setExceptionDates(new ArrayList<>());
    	}
    	getExceptionDates().addAll(exceptions);
    	if (exceptions != null)
    	{
    		exceptions.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    public T withExceptionDates(String...exceptions)
    {
        List<ExceptionDates> list = Arrays.stream(exceptions)
                .map(c -> ExceptionDates.parse(c))
                .collect(Collectors.toList());  
        return withExceptionDates(list);
    }
    public T withExceptionDates(Temporal...exceptions)
    {
    	return withExceptionDates(new ExceptionDates(exceptions));
    }
    public T withExceptionDates(ExceptionDates...exceptions)
    {
    	return withExceptionDates(Arrays.asList(exceptions));
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
    public LastModified getDateTimeLastModified() { return lastModified; }
    private LastModified lastModified;
    @Override
	public void setDateTimeLastModified(LastModified lastModified)
    {
    	orderChild(this.lastModified, lastModified);
    	this.lastModified = lastModified;
	}
    // Other setters are default methods in interface
    
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public List<RecurrenceDates> getRecurrenceDates() { return recurrenceDates; }
    private List<RecurrenceDates> recurrenceDates;
    @Override
    public void setRecurrenceDates(List<RecurrenceDates> recurrenceDates)
    {
    	this.recurrenceDates = recurrenceDates;
    	if (recurrenceDates != null)
    	{
    		recurrenceDates.forEach(c -> orderChild(c));
    	}
	}

    /**
     * RECURRENCE-ID: Recurrence Identifier
     * RFC 5545 iCalendar 3.8.4.4 page 112
     * The property value is the original value of the "DTSTART" property of the 
     * recurrence instance before an edit that changed the value.
     * 
     * Example:
     * RECURRENCE-ID;VALUE=DATE:19960401
     */
    public RecurrenceId getRecurrenceId() { return recurrenceId; }
    private RecurrenceId recurrenceId;
    public void setRecurrenceId(RecurrenceId recurrenceId)
    {
    	orderChild(this.recurrenceId, recurrenceId);
    	this.recurrenceId = recurrenceId;
	}
    public void setRecurrenceId(String recurrenceId) { setRecurrenceId(RecurrenceId.parse(recurrenceId)); }
    public void setRecurrenceId(Temporal temporal)
    {
        if ((temporal instanceof LocalDate) || (temporal instanceof LocalDateTime) || (temporal instanceof ZonedDateTime))
        {
            if (getRecurrenceId() == null)
            {
                setRecurrenceId(new RecurrenceId(temporal));
            } else
            {
                getRecurrenceId().setValue(temporal);
            }
        } else
        {
            throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                    + temporal.getClass().getSimpleName() + " is not supported");
        }
    }
    public T withRecurrenceId(RecurrenceId recurrenceId)
    {
        setRecurrenceId(recurrenceId);
        return (T) this;
    }
    public T withRecurrenceId(String recurrenceId)
    {
        setRecurrenceId(recurrenceId);
        return (T) this;
    }
    public T withRecurrenceId(Temporal recurrenceId)
    {
        setRecurrenceId(recurrenceId);
        return (T) this;
    }
        
    /** Checks if RecurrenceId has same date-time type as DateTimeStart.  Returns String containing
     * error message if there is a problem, null otherwise. */
    String checkRecurrenceIdConsistency()
    {
        if (getRecurrenceId() != null && recurrenceParent() != null)
        {
            DateTimeType recurrenceIdType = DateTimeUtilities.DateTimeType.of(getRecurrenceId().getValue());
            DateTimeType parentDateTimeStartType = DateTimeUtilities.DateTimeType.of(recurrenceParent().getDateTimeStart().getValue());
            if (recurrenceIdType != parentDateTimeStartType)
            {
                return VPropertyElement.RECURRENCE_IDENTIFIER.toString() + ":RecurrenceId DateTimeType (" + recurrenceIdType +
                        ") must be same as the type of its parent's DateTimeStart (" + parentDateTimeStartType + ")";
            }
        }
        return null;
    }

    /**
     * RELATED-TO:
     * 3.8.4.5, RFC 5545 iCalendar, page 115
     * This property is used to represent a relationship or reference between
     * one calendar component and another.  By default, the property value points to another
     * calendar component's UID that has a PARENT relationship to the referencing object.
     * This field is null unless the object contains as RECURRENCE-ID value.
     * 
     * Example:
     * RELATED-TO:19960401-080045-4000F192713-0052@example.com
     */
    public List<RelatedTo> getRelatedTo() { return relatedTo; }
    private List<RelatedTo> relatedTo;
    public void setRelatedTo(List<RelatedTo> relatedTo)
    {
    	if (this.relatedTo != null)
    	{
    		this.relatedTo.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.relatedTo = relatedTo;
    	if (relatedTo != null)
    	{
        	relatedTo.forEach(c -> orderChild(c));
    	}
	}
    public T withRelatedTo(List<RelatedTo> relatedTo)
    {
    	if (getRelatedTo() == null)
    	{
    		setRelatedTo(new ArrayList<>());
    	}
    	getRelatedTo().addAll(relatedTo);
    	if (relatedTo != null)
    	{
    		relatedTo.forEach(c -> orderChild(c));
    	}
    	return (T) this;
	}
    public T withRelatedTo(String...relatedTo)
    {
        List<RelatedTo> list = Arrays.stream(relatedTo)
                .map(c -> RelatedTo.parse(c))
                .collect(Collectors.toList());
        return withRelatedTo(list);
    }
    public T withRelatedTo(RelatedTo...relatedTo)
    {
    	return withRelatedTo(Arrays.asList(relatedTo));
    }
    
    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override
    public RecurrenceRule getRecurrenceRule() { return recurrenceRule; }
    private RecurrenceRule recurrenceRule;
    @Override
	public void setRecurrenceRule(RecurrenceRule recurrenceRule)
    {
    	orderChild(this.recurrenceRule, recurrenceRule);
    	this.recurrenceRule = recurrenceRule;
	}
 
    /**
     * SEQUENCE:
     * RFC 5545 iCalendar 3.8.7.4. page 138
     * This property defines the revision sequence number of the calendar component within a sequence of revisions.
     * Example:  The following is an example of this property for a calendar
     * component that was just created by the "Organizer":
     *
     * SEQUENCE:0
     *
     * The following is an example of this property for a calendar
     * component that has been revised two different times by the
     * "Organizer":
     *
     * SEQUENCE:2
     */
    public Sequence getSequence() { return sequence; }
    private Sequence sequence;
    public void setSequence(Sequence sequence)
    {
    	orderChild(this.sequence, sequence);
    	this.sequence = sequence;
	}
    public void setSequence(String sequence) { setSequence(Sequence.parse(sequence)); }
    public void setSequence(Integer sequence) { setSequence(new Sequence(sequence)); }
    public T withSequence(Sequence sequence)
    {
        if (getSequence() == null)
        {
            setSequence(sequence);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withSequence(Integer sequence)
    {
        if (getSequence() == null)
        {
            setSequence(sequence);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withSequence(String sequence)
    {
        if (getSequence() == null)
        {
            setSequence(sequence);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public void incrementSequence()
    {
        if (getSequence() != null)
        {
            setSequence(getSequence().getValue()+1);            
        } else
        {
            setSequence(1);            
        }
    }
    
    /**
     * STATUS
     * RFC 5545 iCalendar 3.8.1.11. page 92
     * 
     * This property defines the overall status or confirmation for the calendar component.
     * 
     * Example:
     * STATUS:TENTATIVE
     */
    public Status getStatus() { return status; }
    private Status status;
    public void setStatus(Status status)
    {
    	orderChild(this.status, status);
    	this.status = status;
	}
    public void setStatus(String status) { setStatus(Status.parse(status)); }
    public void setStatus(StatusType status) { setStatus(new Status(status)); }
    public T withStatus(Status status)
    {
        setStatus(status);
        return (T) this;
    }
    public T withStatus(StatusType status)
    {
        setStatus(status);
        return (T) this;
    }
    public T withStatus(String status)
    {
        setStatus(status);
        return (T) this;
    }

    /**
     * SUMMARY
     * RFC 5545 iCalendar 3.8.1.12. page 93
     * 
     * This property defines a short summary or subject for the calendar component.
     * 
     * Example:
     * SUMMARY:Department Party
     */
    @Override
    public Summary getSummary() { return summary; }
    private Summary summary;
    @Override
	public void setSummary(Summary summary)
    {
    	orderChild(this.summary, summary);
    	this.summary = summary;
	}

    
    @Override
    void dateTimeStartListenerHook()
    {
        super.dateTimeStartListenerHook();
        String reccurenceIDErrorString = checkRecurrenceIdConsistency();
        if (reccurenceIDErrorString != null)
        {
            throw new RuntimeException(reccurenceIDErrorString);
        }
    }
    
    /*
     * CONSTRUCTORS
     */
    public VDisplayable() { super(); }
    
    public VDisplayable(VDisplayable<T> source)
    {
        super(source);
    }
    
    @Override
    @Deprecated // need to move to VCalendar
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        // get stream with recurrence rule (RRULE) and recurrence date (RDATE)
        Stream<Temporal> inStream = VRepeatable.super.streamRecurrences(start);

        // assign temporal comparator to match start type
        final Comparator<Temporal> temporalComparator = DateTimeUtilities.getTemporalComparator(start);
        
        // Handle Recurrence IDs
        final Stream<Temporal> stream2;
        List<VDisplayable<?>> children = recurrenceChildren();
        if (children != null)
        {
            // If present, remove recurrence ID original values
            List<Temporal> recurrenceIDTemporals = recurrenceChildren()
                    .stream()
                    .map(c -> c.getRecurrenceId().getValue())
                    .collect(Collectors.toList());
            stream2 = inStream.filter(t -> ! recurrenceIDTemporals.contains(t));
        } else
        {
            stream2 = inStream;
        }
        
        // If present, remove exceptions
        final Stream<Temporal> stream3;
        if (getExceptionDates() != null)
        {
            List<Temporal> exceptions = getExceptionDates()
                    .stream()
                    .flatMap(r -> r.getValue().stream())
                    .map(v -> v)
                    .sorted(temporalComparator)
                    .collect(Collectors.toList());
            stream3 = stream2.filter(d -> ! exceptions.contains(d));
        } else
        {
            stream3 = stream2;
        }
        
        if (getRecurrenceRule() == null)
        {
            return stream3; // no cache is no recurrence rule
        }
    	if (getRecurrenceRule().getValue().getCount() == null)
    	{
            return recurrenceCache().makeCache(stream3);  // make cache of start date/times
    	} else
    	{ // if RRULE has COUNT must start at DTSTART
    		return stream3;
    	}
    }

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache recurrenceCache = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceCache() { return recurrenceCache; }

    /*
     * RECURRENCE CHILDREN - (RECURRENCE-IDs AND MATCHING UID)
     */
    public List<VDisplayable<?>> recurrenceChildren()
    {
    	if ((getParent() != null) && (getRecurrenceId() == null))
    	{
    		UniqueIdentifier myUid = getUniqueIdentifier();
    		return calendarList()
    			.stream()
    			.map(c -> (VDisplayable<?>) c)
    			.filter(c -> ! (c == this))
    			.filter(c -> c.getUniqueIdentifier().equals(myUid))
				.filter(c -> c.getRecurrenceId() != null)
				.collect(Collectors.toList());
    	} else
    	{
    		return Collections.emptyList();
    	}
    }
    
    /*
     * RECURRENCE PARENT - (the VComponent with matching UID and no RECURRENCEID)
     */
    public VDisplayable<?> recurrenceParent()
    {
    	if (getParent() != null && (getRecurrenceId() != null))
    	{
    		UniqueIdentifier myUid = getUniqueIdentifier();
    		@SuppressWarnings("rawtypes")
			Optional<VDisplayable> recurrenceParent = calendarList()
    			.stream()
    			.map(c -> (VDisplayable) c)
    			.filter(c -> ! (c == this))
    			.filter(c -> c.getUniqueIdentifier().equals(myUid))
				.filter(c -> c.getRecurrenceId() == null)
				.findAny();
    		return (recurrenceParent.isPresent()) ? recurrenceParent.get() : null;
    	} else
    	{
    		return null;
    	}
    }

    /** returns list of orphaned recurrence components due to a change.  These
     * components should be deleted */
    public List<VDisplayable<?>> orphanedRecurrenceChildren()
    {
        boolean isRecurrenceParent = getRecurrenceId() == null;
        if (isRecurrenceParent)
        {
            VCalendar vCalendar = (VCalendar) getParent();
            if (vCalendar != null)
            {
                final UniqueIdentifier uid = getUniqueIdentifier();
                return calendarList()
                        .stream()
                        .map(v -> (VDisplayable<?>) v)
                        .filter(v -> v.getUniqueIdentifier().equals(uid))
                        .filter(v -> v.getRecurrenceId() != null)
                        .filter(v -> 
                        {
                            Temporal myRecurrenceID = v.getRecurrenceId().getValue();
                            Temporal cacheStart = recurrenceCache().getClosestStart(myRecurrenceID);
                            Temporal nextRecurrenceDateTime = getRecurrenceRule().getValue()
                                    .streamRecurrences(cacheStart)
                                    .filter(t -> ! DateTimeUtilities.isBefore(t, myRecurrenceID))
                                    .findFirst()
                                    .orElseGet(() -> null);
                            return ! Objects.equals(nextRecurrenceDateTime, myRecurrenceID);
                        })
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
    


    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        String reccurenceIDErrorString = checkRecurrenceIdConsistency();
        if (reccurenceIDErrorString != null)
        {
            errors.add(reccurenceIDErrorString);
        }
            
        // Tests from Repeatable
        errors.addAll(VRepeatableBase.errorsRepeatable(this));
        errors.addAll(VRepeatableBase.errorsRecurrence(getExceptionDates(), getDateTimeStart()));
        errors.addAll(VRepeatableBase.errorsRecurrence(getRecurrenceDates(), getDateTimeStart()));
        return errors;
    }
    
    /** Erase all date/time properties such as DTSTART, DTEND, DURATION, and DUE (which ever exist).  This
     * is necessary to prepare a CANCEL iTIP message for one recurrence instance. */
    public void eraseDateTimeProperties()
    {
        setDateTimeStart((DateTimeStart) null);
    }
}
