package jfxtras.icalendarfx.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.VProperty;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.alarm.Action;
import jfxtras.icalendarfx.properties.component.alarm.RepeatCount;
import jfxtras.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.change.LastModified;
import jfxtras.icalendarfx.properties.component.change.Sequence;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.PercentComplete;
import jfxtras.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.properties.component.descriptive.Status;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Contact;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.icalendarfx.properties.component.time.DateTimeCompleted;
import jfxtras.icalendarfx.properties.component.time.DateTimeDue;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.properties.component.time.DurationProp;
import jfxtras.icalendarfx.properties.component.time.FreeBusyTime;
import jfxtras.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;
import jfxtras.icalendarfx.properties.component.timezone.TimeZoneURL;

public enum VPropertyElement
{
	ACTION ("ACTION", Action.class, Arrays.asList(ValueType.TEXT)),
    ATTACHMENT ("ATTACH", Attachment.class, Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY)),
    ATTENDEE ("ATTENDEE", Attendee.class, Arrays.asList(ValueType.CALENDAR_USER_ADDRESS)),
    CALENDAR_SCALE ("CALSCALE", CalendarScale.class, Arrays.asList(ValueType.TEXT)),
    CATEGORIES ("CATEGORIES", Categories.class, Arrays.asList(ValueType.TEXT)),
    CLASSIFICATION ("CLASS", Classification.class, Arrays.asList(ValueType.TEXT)),
    COMMENT ("COMMENT", Comment.class, Arrays.asList(ValueType.TEXT)),
    CONTACT ("CONTACT", Contact.class, Arrays.asList(ValueType.TEXT)),
    DATE_TIME_COMPLETED ("COMPLETED", DateTimeCompleted.class, Arrays.asList(ValueType.DATE_TIME)),
    DATE_TIME_CREATED ("CREATED",  DateTimeCreated.class, Arrays.asList(ValueType.DATE_TIME)),
    DATE_TIME_DUE ("DUE", DateTimeDue.class, Arrays.asList(ValueType.DATE_TIME, ValueType.DATE)),
    DATE_TIME_END ("DTEND", DateTimeEnd.class, Arrays.asList(ValueType.DATE_TIME, ValueType.DATE)),
    DATE_TIME_STAMP ("DTSTAMP", DateTimeStamp.class, Arrays.asList(ValueType.DATE_TIME)),
    DATE_TIME_START ("DTSTART", DateTimeStart.class, Arrays.asList(ValueType.DATE_TIME, ValueType.DATE)),
    DESCRIPTION ("DESCRIPTION", Description.class, Arrays.asList(ValueType.TEXT)),
    DURATION ("DURATION", DurationProp.class, Arrays.asList(ValueType.DURATION)),
    EXCEPTION_DATE_TIMES ("EXDATE", ExceptionDates.class, Arrays.asList(ValueType.DATE_TIME, ValueType.DATE)),
    FREE_BUSY_TIME ("FREEBUSY", FreeBusyTime.class, Arrays.asList(ValueType.PERIOD)),
    GEOGRAPHIC_POSITION ("GEO", GeographicPosition.class, Arrays.asList(ValueType.TEXT)), // TODO - SHOULD BE FLOAT - TWO OF THEM
    LAST_MODIFIED ("LAST-MODIFIED", LastModified.class, Arrays.asList(ValueType.DATE_TIME)),
    LOCATION ("LOCATION", Location.class, Arrays.asList(ValueType.TEXT)),
    METHOD ("METHOD", jfxtras.icalendarfx.properties.calendar.Method.class, Arrays.asList(ValueType.TEXT)),
    NON_STANDARD_PROPERTY ("X-", NonStandardProperty.class, Arrays.asList(ValueType.values())),
    ORGANIZER ("ORGANIZER", Organizer.class, Arrays.asList(ValueType.CALENDAR_USER_ADDRESS)),
    PERCENT_COMPLETE ("PERCENT-COMPLETE", PercentComplete.class, Arrays.asList(ValueType.INTEGER)),
    PRIORITY ("PRIORITY", Priority.class, Arrays.asList(ValueType.INTEGER)),
    PRODUCT_IDENTIFIER ("PRODID", ProductIdentifier.class, Arrays.asList(ValueType.TEXT)),
    RECURRENCE_DATE_TIMES ("RDATE", RecurrenceDates.class, Arrays.asList(ValueType.DATE_TIME, ValueType.DATE, ValueType.PERIOD)),
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", RecurrenceId.class, Arrays.asList(ValueType.DATE_TIME, ValueType.DATE)),
    RECURRENCE_RULE ("RRULE", RecurrenceRule.class, Arrays.asList(ValueType.RECURRENCE_RULE)),
    RELATED_TO ("RELATED-TO", RelatedTo.class, Arrays.asList(ValueType.TEXT)),
    REPEAT_COUNT ("REPEAT", RepeatCount.class, Arrays.asList(ValueType.INTEGER)),
    REQUEST_STATUS ("REQUEST-STATUS", RequestStatus.class, Arrays.asList(ValueType.TEXT)),
    RESOURCES ("RESOURCES", Resources.class, Arrays.asList(ValueType.TEXT)),
    SEQUENCE ("SEQUENCE", Sequence.class, Arrays.asList(ValueType.INTEGER)),
    STATUS ("STATUS", Status.class, Arrays.asList(ValueType.TEXT)),
    SUMMARY ("SUMMARY", Summary.class, Arrays.asList(ValueType.TEXT)),
    TIME_TRANSPARENCY ("TRANSP", TimeTransparency.class, Arrays.asList(ValueType.TEXT)),
    TIME_ZONE_IDENTIFIER_PROPERTY ("TZID", TimeZoneIdentifier.class, Arrays.asList(ValueType.TEXT)),
    TIME_ZONE_NAME ("TZNAME", TimeZoneName.class, Arrays.asList(ValueType.TEXT)),
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", TimeZoneOffsetFrom.class, Arrays.asList(ValueType.UTC_OFFSET)),
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", TimeZoneOffsetTo.class, Arrays.asList(ValueType.UTC_OFFSET)),
    TIME_ZONE_URL ("TZURL", TimeZoneURL.class, Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER)),
    TRIGGER ("TRIGGER", Trigger.class, Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME)),
    UNIQUE_IDENTIFIER ("UID", UniqueIdentifier.class, Arrays.asList(ValueType.TEXT)),
    UNIFORM_RESOURCE_LOCATOR ("URL", UniformResourceLocator.class, Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER)),
    VERSION ("VERSION", Version.class, Arrays.asList(ValueType.TEXT))
	;
    
    // Map to match up class to enum
    private final static Map<Class<? extends VElement>, VPropertyElement> CLASS_MAP = makeEnumFromClassMap();
    private static Map<Class<? extends VElement>, VPropertyElement> makeEnumFromClassMap()
    {
    	Map<Class<? extends VElement>, VPropertyElement> map = new HashMap<>();
    	VPropertyElement[] values = VPropertyElement.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
	public static VPropertyElement fromClass(Class<? extends VElement> vElementClass)
	{
		return CLASS_MAP.get(vElementClass);
	}
    	
	/*
	 * CONSTRUCTOR
	 */
    private String name;
    @Override  public String toString() { return name; }

    private Class<? extends VProperty> myClass;
	public Class<? extends VProperty> elementClass() { return myClass; }

    private List<ValueType> valueTypes;
    public List<ValueType> allowedValueTypes() { return valueTypes; }
    public ValueType defaultValueType() { return valueTypes.get(0); }
    
    VPropertyElement(String name, Class<? extends VProperty> myClass, List<ValueType> valueTypes)
    {
        this.name = name;
        this.valueTypes = valueTypes;
        this.myClass = myClass;
    }
	
	public static List<String> names = Arrays
			.stream(values())
			.map(v -> v.name)
			.collect(Collectors.toList());
	
    private static final Map<Class<? extends VProperty>, List<ValueType>> ALLOWED_VALUE_TYPES_MAP = 
    		 Arrays.stream(VPropertyElement.values())
 			.collect(Collectors.toMap(
 					v -> v.elementClass(),
 					v -> v.allowedValueTypes()
 					));
	public static Collection<ValueType> propertyAllowedValueTypes(Class<? extends VProperty> class1)
	{
		return ALLOWED_VALUE_TYPES_MAP.get(class1);
	}
	public static ValueType defaultValueType(Class<? extends VProperty> class1)
	{
		return ALLOWED_VALUE_TYPES_MAP.get(class1).get(0);
	}
}
