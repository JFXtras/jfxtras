package jfxtras.icalendarfx.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.components.StandardOrDaylight;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VAttendee;
import jfxtras.icalendarfx.components.VCommon;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.components.VDateTimeEnd;
import jfxtras.icalendarfx.components.VDescribable;
import jfxtras.icalendarfx.components.VDescribable2;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VDuration;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VLastModified;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.components.VRepeatable;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.Method;
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

/**
 * For each VComponent property (RFC 5545, 3.8, page 80) contains the following: <br>
 * <br>
 * Property name {@link #toString()} <br>
 * Allowed property value type (first is default value type) {@link PropertyType#allowedValueTypes()}<br>
 * Allowed parameters {@link #allowedParameters()}<br>
 * Property class {@link #getPropertyClass()}<br>
 * Method to get property from component {@link #getProperty(VComponent)}<br>
 * Method to parse property string into parent component {@link #parse(VComponent, String)}<br>
 * Method to copy property into new parent component {@link #copyProperty(Property, VComponent)}<br>
 * 
 * @author David Bal
 *
 */
public enum PropertyType
{
    // Alarm
    ACTION ("ACTION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Action.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getAction();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            Action child = Action.parse(propertyContent);
            VAlarm castComponent = (VAlarm) vParent;
            castComponent.setAction(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            Action propertyCopy = new Action((Action) childSource);
            castDestination.setAction(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // property class
    ATTACHMENT ("ATTACH" // property name
            , Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY) // valid property value types, first is default
            , Arrays.asList(ParameterType.FORMAT_TYPE, ParameterType.INLINE_ENCODING, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , Attachment.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDescribable<?> castComponent = (VDescribable<?>) vComponent;
            return castComponent.getAttachments();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDescribable<?> castComponent = (VDescribable<?>) vParent;
            final ObservableList<Attachment<?>> list;
            if (castComponent.getAttachments() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setAttachments(list);
            } else
            {
                list = castComponent.getAttachments();
            }
            Attachment<?> child = Attachment.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDescribable<?> castDestination = (VDescribable<?>) destination;
            final ObservableList<Attachment<?>> list;
            if (castDestination.getAttachments() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setAttachments(list);
            } else
            {
                list = castDestination.getAttachments();
            }
            list.add(new Attachment<>((Attachment<?>) childSource));
        }
    },

    ATTENDEE ("ATTENDEE"    // property name
            , Arrays.asList(ValueType.CALENDAR_USER_ADDRESS) // valid property value types, first is default
            , Arrays.asList(ParameterType.COMMON_NAME, ParameterType.CALENDAR_USER_TYPE, ParameterType.DELEGATEES,
                    ParameterType.DELEGATORS, ParameterType.DIRECTORY_ENTRY_REFERENCE,
                    ParameterType.GROUP_OR_LIST_MEMBERSHIP, ParameterType.LANGUAGE, ParameterType.PARTICIPATION_ROLE,
                    ParameterType.PARTICIPATION_STATUS, ParameterType.RSVP_EXPECTATION, ParameterType.SENT_BY,
                    ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , Attendee.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAttendee<?> castComponent = (VAttendee<?>) vComponent;
            return castComponent.getAttendees();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VAttendee<?> castComponent = (VAttendee<?>) vParent;
            final ObservableList<Attendee> list;
            if (castComponent.getAttendees() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setAttendees(list);
            } else
            {
                list = castComponent.getAttendees();
            }
            Attendee child = Attendee.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VAttendee<?> castDestination = (VAttendee<?>) destination;
            final ObservableList<Attendee> list;
            if (castDestination.getAttendees() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setAttendees(list);
            } else
            {
                list = castDestination.getAttendees();
            }
            list.add(new Attendee((Attendee) childSource));
        }
    },
    // Calendar property
    CALENDAR_SCALE ("CALSCALE" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , CalendarScale.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Descriptive
    CATEGORIES ("CATEGORIES" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , Categories.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getCategories();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            final ObservableList<Categories> list;
            if (castComponent.getCategories() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setCategories(list);
            } else
            {
                list = castComponent.getCategories();
            }
            Categories child = Categories.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            final ObservableList<Categories> list;
            if (castDestination.getCategories() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setCategories(list);
            } else
            {
                list = castDestination.getCategories();
            }
            list.add(new Categories((Categories) childSource));
        }
    },
    // Descriptive
    CLASSIFICATION ("CLASS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Classification.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getClassification();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            Classification child = Classification.parse(propertyContent);
            castComponent.setClassification(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            Classification propertyCopy = new Classification((Classification) childSource);
            castDestination.setClassification(propertyCopy);
        }
    },
    // Descriptive
    COMMENT ("COMMENT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.NON_STANDARD), // allowed parameters
            Comment.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPrimary<?> castProperty = (VPrimary<?>) vComponent;
            return castProperty.getComments();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPrimary<?> castComponent = (VPrimary<?>) vParent;
            final ObservableList<Comment> list;
            if (castComponent.getComments() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setComments(list);
            } else
            {
                list = castComponent.getComments();
            }
            Comment child = Comment.parse(propertyContent);
            list.add(child);
            return child;
        }
        
        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPrimary<?> castDestination = (VPrimary<?>) destination;
            final ObservableList<Comment> list;
            if (castDestination.getComments() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setComments(list);
            } else
            {
                list = castDestination.getComments();
            }
            list.add(new Comment((Comment) childSource));
        }
    },
    // Relationship
    CONTACT ("CONTACT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.NON_STANDARD), // allowed parameters
            Contact.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            if (vComponent instanceof VFreeBusy)
            {// VJournal has one Contact
                VFreeBusy castComponent = (VFreeBusy) vComponent;
                return castComponent.getContact();                
            } else
            { // Other components have a list of Contacts
                VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
                return castComponent.getContacts();
            }
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            Contact child = Contact.parse(propertyContent);
            if (vParent instanceof VFreeBusy)
            {// VJournal has one Contact
                VFreeBusy castComponent = (VFreeBusy) vParent;
                castComponent.setContact(child);                
            } else
            { // Other components have a list of Contacts
                VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
                final ObservableList<Contact> list;
                if (castComponent.getContacts() == null)
                {
                    list = FXCollections.observableArrayList();
                    castComponent.setContacts(list);
                } else
                {
                    list = castComponent.getContacts();
                }
                list.add(child);
            }
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            if (destination instanceof VFreeBusy)
            { // VFreeBusy has one Contact
                VFreeBusy castDestination = (VFreeBusy) destination;
                Contact propertyCopy = new Contact((Contact) childSource);
                castDestination.setContact(propertyCopy);
            } else
            { // Other components have a list of Contacts
                VDisplayable<?> castDestination = (VDisplayable<?>) destination;
                final ObservableList<Contact> list;
                if (castDestination.getContacts() == null)
                {
                    list = FXCollections.observableArrayList();
                    castDestination.setContacts(list);
                } else
                {
                    list = castDestination.getContacts();
                }
                list.add(new Contact((Contact) childSource));
            }
        }
    },
    // Date and Time
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DateTimeCompleted.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeCompleted();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vParent;
            DateTimeCompleted child = DateTimeCompleted.parse(propertyContent);
            castComponent.setDateTimeCompleted(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VTodo castDestination = (VTodo) destination;
            DateTimeCompleted propertyCopy = new DateTimeCompleted((DateTimeCompleted) childSource);
            castDestination.setDateTimeCompleted(propertyCopy);
        }
    },
    // Change management
    DATE_TIME_CREATED ("CREATED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DateTimeCreated.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getDateTimeCreated();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            DateTimeCreated child = DateTimeCreated.parse(propertyContent);
            castComponent.setDateTimeCreated(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            DateTimeCreated propertyCopy = new DateTimeCreated((DateTimeCreated) childSource);
            castDestination.setDateTimeCreated(propertyCopy);
        }
    },
    // Date and time
    DATE_TIME_DUE ("DUE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DateTimeDue.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeDue();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vParent;
            DateTimeDue child = DateTimeDue.parse(propertyContent);
            castComponent.setDateTimeDue(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VTodo castDestination = (VTodo) destination;
            DateTimeDue propertyCopy = new DateTimeDue((DateTimeDue) childSource);
            castDestination.setDateTimeDue(propertyCopy);
        }
    },
    // Date and Time
    DATE_TIME_END ("DTEND", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DateTimeEnd.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDateTimeEnd<?> castComponent = (VDateTimeEnd<?>) vComponent;
            return castComponent.getDateTimeEnd();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDateTimeEnd<?> castComponent = (VDateTimeEnd<?>) vParent;
            DateTimeEnd child = DateTimeEnd.parse(propertyContent);
            castComponent.setDateTimeEnd(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDateTimeEnd<?> castDestination = (VDateTimeEnd<?>) destination;
            DateTimeEnd propertyCopy = new DateTimeEnd((DateTimeEnd) childSource);
            castDestination.setDateTimeEnd(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Change management
    DATE_TIME_STAMP ("DTSTAMP", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DateTimeStamp.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPersonal<?> castComponent = (VPersonal<?>) vComponent;
            return castComponent.getDateTimeStamp();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPersonal<?> castComponent = (VPersonal<?>) vParent;
            DateTimeStamp child = DateTimeStamp.parse(propertyContent);
            castComponent.setDateTimeStamp(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPersonal<?> castDestination = (VPersonal<?>) destination;
            DateTimeStamp propertyCopy = new DateTimeStamp((DateTimeStamp) childSource);
            castDestination.setDateTimeStamp(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    DATE_TIME_START ("DTSTART", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DateTimeStart.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPrimary<?> castComponent = (VPrimary<?>) vComponent;
            return castComponent.getDateTimeStart();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPrimary<?> castComponent = (VPrimary<?>) vParent;
            DateTimeStart child = DateTimeStart.parse(propertyContent);
            castComponent.setDateTimeStart(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPrimary<?> castDestination = (VPrimary<?>) destination;
            DateTimeStart propertyCopy = new DateTimeStart((DateTimeStart) childSource);
            castDestination.setDateTimeStart(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent )
        {
            return (parent instanceof VEvent) ? true : false;
        }
    },
    // Descriptive
    DESCRIPTION ("DESCRIPTION",
            Arrays.asList(ValueType.TEXT),
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.NON_STANDARD),
            Description.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            if (vComponent instanceof VJournal)
            {// VJournal has list of Descriptions
                VJournal castComponent = (VJournal) vComponent;
                return castComponent.getDescriptions();                
            } else
            { // Other components have only one Description
                VDescribable2<?> castComponent = (VDescribable2<?>) vComponent;
                return castComponent.getDescription();
            }
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            Description child = Description.parse(propertyContent);
            if (vParent instanceof VJournal)
            { // VJournal has list of Descriptions
                VJournal castComponent = (VJournal) vParent;
                final ObservableList<Description> list;
                if (castComponent.getDescriptions() == null)
                {
                    list = FXCollections.observableArrayList();
                    castComponent.setDescriptions(list);
                } else
                {
                    list = castComponent.getDescriptions();
                }
                list.add(child);
            } else
            { // Other components have only one Description
                VDescribable2<?> castComponent = (VDescribable2<?>) vParent;
                if (castComponent.getDescription() == null)
                {
                    castComponent.setDescription(child);                                
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
                }
            }
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            if (destination instanceof VJournal)
            { // VJournal has list of Descriptions
                VJournal castDestination = (VJournal) destination;
                final ObservableList<Description> list;
                if (castDestination.getDescriptions() == null)
                {
                    list = FXCollections.observableArrayList();
                    castDestination.setDescriptions(list);
                } else
                {
                    list = castDestination.getDescriptions();
                }
                list.add(new Description((Description) childSource));
            } else
            { // Other components have only one Description
                VDescribable2<?> castDestination = (VDescribable2<?>) destination;
                Description propertyCopy = new Description((Description) childSource);
                castDestination.setDescription(propertyCopy);
            }
        }
    },
    // Date and Time
    DURATION ("DURATION", // property name
            Arrays.asList(ValueType.DURATION), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            DurationProp.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDuration<?> castComponent = (VDuration<?>) vComponent;
            return castComponent.getDuration();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            DurationProp child = DurationProp.parse(propertyContent);            
            VDuration<?> castComponent = (VDuration<?>) vParent;
            if (castComponent.getDuration() == null)
            {
                castComponent.setDuration(child);                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDuration<?> castDestination = (VDuration<?>) destination;
            DurationProp propertyCopy = new DurationProp((DurationProp) childSource);
            castDestination.setDuration(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            ExceptionDates.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getExceptionDates();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            ExceptionDates child = ExceptionDates.parse(propertyContent);
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            final ObservableList<ExceptionDates> list;
            if (castComponent.getExceptionDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setExceptionDates(list);
            } else
            {
                list = castComponent.getExceptionDates();
            }

            String error = VRepeatable.checkPotentialRecurrencesConsistency(list, child);
            if (error == null)
            {
                list.add(child);
                return child;
            }
            return null; // invalid content
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            final ObservableList<ExceptionDates> list;
            if (castDestination.getExceptionDates() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setExceptionDates(list);
            } else
            {
                list = castDestination.getExceptionDates();
            }
            list.add(new ExceptionDates((ExceptionDates) childSource));
        }
    },
    // Date and Time
    FREE_BUSY_TIME ("FREEBUSY", // property name
            Arrays.asList(ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.FREE_BUSY_TIME_TYPE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            FreeBusyTime.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VFreeBusy castComponent = (VFreeBusy) vComponent;
            return castComponent.getFreeBusyTime();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VFreeBusy castComponent = (VFreeBusy) vParent;
            FreeBusyTime child = FreeBusyTime.parse(propertyContent);
            castComponent.setFreeBusyTime(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VFreeBusy castDestination = (VFreeBusy) destination;
            FreeBusyTime propertyCopy = new FreeBusyTime((FreeBusyTime) childSource);
            castDestination.setFreeBusyTime(propertyCopy);
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", // property name
            Arrays.asList(ValueType.TEXT), // In GeographicPosition there are two doubles for latitude and longitude
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            GeographicPosition.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vComponent;
            return castComponent.getGeographicPosition();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vParent;
            GeographicPosition child = GeographicPosition.parse(propertyContent);
            castComponent.setGeographicPosition(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VLocatable<?> castDestination = (VLocatable<?>) destination;
            GeographicPosition propertyCopy = new GeographicPosition((GeographicPosition) childSource);
            castDestination.setGeographicPosition(propertyCopy);
        }
    },
    // Change management
    LAST_MODIFIED ("LAST-MODIFIED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            LastModified.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VLastModified<?> castComponent = (VLastModified<?>) vComponent;
            return castComponent.getDateTimeLastModified();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VLastModified<?> castComponent = (VLastModified<?>) vParent;
            LastModified child = LastModified.parse(propertyContent);
            castComponent.setDateTimeLastModified(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VLastModified<?> castDestination = (VLastModified<?>) destination;
            LastModified propertyCopy = new LastModified((LastModified) childSource);
            castDestination.setDateTimeLastModified(propertyCopy);
        }
    },
    // Descriptive
    LOCATION ("LOCATION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.NON_STANDARD), // allowed parameters
            Location.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vComponent;
            return castComponent.getLocation();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vParent;
            Location child = Location.parse(propertyContent);
            castComponent.setLocation(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VLocatable<?> castDestination = (VLocatable<?>) destination;
            Location propertyCopy = new Location((Location) childSource);
            castDestination.setLocation(propertyCopy);
        }
    },
    // Calendar property
    METHOD ("METHOD" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , Method.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Miscellaneous
    NON_STANDARD ("X-", // property name begins with X- prefix
            Arrays.asList(ValueType.values()), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterType.values()), // all parameters allowed
//            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.LANGUAGE), // allowed parameters (RFC 5545 says only IANA, non-standard, and language property parameters can be specified on this property, but examples of other parameters are in RFC 5545, so I am allowing all parameters)
            NonStandardProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VCommon<?> castComponent = (VCommon<?>) vComponent;
            return castComponent.getNonStandard();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VCommon<?> castComponent = (VCommon<?>) vParent;
            final ObservableList<NonStandardProperty> list;
            if (castComponent.getNonStandard() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setNonStandard(list);
            } else
            {
                list = castComponent.getNonStandard();
            }
            NonStandardProperty child = NonStandardProperty.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VCommon<?> castDestination = (VCommon<?>) destination;
            final ObservableList<NonStandardProperty> list;
            if (castDestination.getNonStandard() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setNonStandard(list);
            } else
            {
                list = castDestination.getNonStandard();
            }
            list.add(new NonStandardProperty((NonStandardProperty) childSource));
        }
    },
    // Relationship
    ORGANIZER ("ORGANIZER", // name
            Arrays.asList(ValueType.CALENDAR_USER_ADDRESS), // valid property value types, first is default
            Arrays.asList(ParameterType.COMMON_NAME, ParameterType.DIRECTORY_ENTRY_REFERENCE, ParameterType.LANGUAGE,
                    ParameterType.SENT_BY, ParameterType.NON_STANDARD), // allowed parameters
            Organizer.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPersonal<?> castComponent = (VPersonal<?>) vComponent;
            return castComponent.getOrganizer();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPersonal<?> castComponent = (VPersonal<?>) vParent;
            Organizer child = Organizer.parse(propertyContent);
            castComponent.setOrganizer(child);
            return child;
        }
        
        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPersonal<?> castDestination = (VPersonal<?>) destination;
            Organizer propertyCopy = new Organizer((Organizer) childSource);
            castDestination.setOrganizer(propertyCopy);
        }
    },
    // Descriptive
    PERCENT_COMPLETE ("PERCENT-COMPLETE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            PercentComplete.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getPercentComplete();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vParent;
            PercentComplete child = PercentComplete.parse(propertyContent);
            castComponent.setPercentComplete(child);
            return child;
        }
        
        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VTodo castDestination = (VTodo) destination;
            PercentComplete propertyCopy = new PercentComplete((PercentComplete) childSource);
            castDestination.setPercentComplete(propertyCopy);
        }
    },
    // Descriptive
    PRIORITY ("PRIORITY", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Priority.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vComponent;
            return castComponent.getPriority();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vParent;
            Priority child = Priority.parse(propertyContent);
            castComponent.setPriority(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VLocatable<?> castDestination = (VLocatable<?>) destination;
            Priority propertyCopy = new Priority((Priority) childSource);
            castDestination.setPriority(propertyCopy);
        }
    },
    // Calendar property
    PRODUCT_IDENTIFIER ("PRODID" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , ProductIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Recurrence
    RECURRENCE_DATE_TIMES ("RDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE, ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            RecurrenceDates.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VRepeatable<?> castComponent = (VRepeatable<?>) vComponent;
            return castComponent.getRecurrenceDates();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VRepeatable<?> castComponent = (VRepeatable<?>) vParent;
            final ObservableList<RecurrenceDates> list;
            if (castComponent.getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRecurrenceDates(list);
            } else
            {
                list = castComponent.getRecurrenceDates();
            }
            RecurrenceDates child = RecurrenceDates.parse(propertyContent);
            list.add(child);
            return child;
        }
        
        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VRepeatable<?> castDestination = (VRepeatable<?>) destination;
            final ObservableList<RecurrenceDates> list;
            if (castDestination.getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setRecurrenceDates(list);
            } else
            {
                list = castDestination.getRecurrenceDates();
            }
            list.add(new RecurrenceDates((RecurrenceDates) childSource));
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.RECURRENCE_IDENTIFIER_RANGE, ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.NON_STANDARD), // allowed parameters
            RecurrenceId.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getRecurrenceId();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            RecurrenceId child = RecurrenceId.parse(propertyContent);
            castComponent.setRecurrenceId(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            RecurrenceId propertyCopy = new RecurrenceId((RecurrenceId) childSource);
            castDestination.setRecurrenceId(propertyCopy);
        }
    },
    // Recurrence
    RECURRENCE_RULE ("RRULE", // property name
            Arrays.asList(ValueType.RECURRENCE_RULE), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            RecurrenceRule.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VRepeatable<?> castComponent = (VRepeatable<?>) vComponent;
            return castComponent.getRecurrenceRule();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VRepeatable<?> castComponent = (VRepeatable<?>) vParent;
            RecurrenceRule child = RecurrenceRule.parse(propertyContent);
            castComponent.setRecurrenceRule(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VRepeatable<?> castDestination = (VRepeatable<?>) destination;
            RecurrenceRule propertyCopy = new RecurrenceRule((RecurrenceRule) childSource);
            castDestination.setRecurrenceRule(propertyCopy);
        }
    },
    // Relationship
    RELATED_TO ("RELATED-TO", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.RELATIONSHIP_TYPE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            RelatedTo.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getRelatedTo();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            final ObservableList<RelatedTo> list;
            if (castComponent.getRelatedTo() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRelatedTo(list);
            } else
            {
                list = castComponent.getRelatedTo();
            }
            RelatedTo child = RelatedTo.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            final ObservableList<RelatedTo> list;
            if (castDestination.getRelatedTo() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setRelatedTo(list);
            } else
            {
                list = castDestination.getRelatedTo();
            }
            list.add(new RelatedTo((RelatedTo) childSource));
        }
    },
    // Alarm
    REPEAT_COUNT ("REPEAT", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            RepeatCount.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getRepeatCount();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vParent;
            RepeatCount child = RepeatCount.parse(propertyContent);
            castComponent.setRepeatCount(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            RepeatCount propertyCopy = new RepeatCount((RepeatCount) childSource);
            castDestination.setRepeatCount(propertyCopy);
        }
    },
    // Miscellaneous
    REQUEST_STATUS ("REQUEST-STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            RequestStatus.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPersonal<?> castComponent = (VPersonal<?>) vComponent;
            return castComponent.getRequestStatus();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPersonal<?> castComponent = (VPersonal<?>) vParent;
            final ObservableList<RequestStatus> list;
            if (castComponent.getRequestStatus() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRequestStatus(list);
            } else
            {
                list = castComponent.getRequestStatus();
            }
            RequestStatus child = RequestStatus.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPersonal<?> castDestination = (VPersonal<?>) destination;
            final ObservableList<RequestStatus> list;
            if (castDestination.getRequestStatus() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setRequestStatus(list);
            } else
            {
                list = castDestination.getRequestStatus();
            }
            list.add(new RequestStatus((RequestStatus) childSource));
        }
    },
    // Descriptive
    RESOURCES ("RESOURCES", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.NON_STANDARD), // allowed parameters
            Resources.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VLocatable<?> castProperty = (VLocatable<?>) vComponent;
            return castProperty.getResources();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VLocatable<?> castComponent = (VLocatable<?>) vParent;
            final ObservableList<Resources> list;
            if (castComponent.getResources() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setResources(list);
            } else
            {
                list = castComponent.getResources();
            }
            Resources child = Resources.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VLocatable<?> castDestination = (VLocatable<?>) destination;
            final ObservableList<Resources> list;
            if (castDestination.getResources() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setResources(list);
            } else
            {
                list = castDestination.getResources();
            }
            list.add(new Resources((Resources) childSource));
        }
    },
    // Change management
    SEQUENCE ("SEQUENCE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Sequence.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getSequence();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            Sequence child = Sequence.parse(propertyContent);
            castComponent.setSequence(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            Sequence propertyCopy = new Sequence((Sequence) childSource);
            castDestination.setSequence(propertyCopy);
        }
    },
    // Descriptive
    STATUS ("STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Status.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vComponent;
            return castComponent.getStatus();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDisplayable<?> castComponent = (VDisplayable<?>) vParent;
            Status child = Status.parse(propertyContent);
            castComponent.setStatus(child);
            return child;            
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDisplayable<?> castDestination = (VDisplayable<?>) destination;
            Status propertyCopy = new Status((Status) childSource);
            castDestination.setStatus(propertyCopy);
        }
    },
    // Descriptive
    SUMMARY ("SUMMARY", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE,
                    ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Summary.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VDescribable<?> castComponent = (VDescribable<?>) vComponent;
            return castComponent.getSummary();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VDescribable<?> castComponent = (VDescribable<?>) vParent;
            Summary child = Summary.parse(propertyContent);
            castComponent.setSummary(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VDescribable<?> castDestination = (VDescribable<?>) destination;
            Summary propertyCopy = new Summary((Summary) childSource);
            castDestination.setSummary(propertyCopy);
        }
    },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VEvent castComponent = (VEvent) vComponent;
            return castComponent.getTimeTransparency();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VEvent castComponent = (VEvent) vParent;
            TimeTransparency child = TimeTransparency.parse(propertyContent);
            castComponent.setTimeTransparency(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VEvent castDestination = (VEvent) destination;
            TimeTransparency propertyCopy = new TimeTransparency((TimeTransparency) childSource);
            castDestination.setTimeTransparency(propertyCopy);
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            TimeZoneIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneIdentifier();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vParent;
            TimeZoneIdentifier child = TimeZoneIdentifier.parse(propertyContent);
            castComponent.setTimeZoneIdentifier(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneIdentifier propertyCopy = new TimeZoneIdentifier((TimeZoneIdentifier) childSource);
            castDestination.setTimeZoneIdentifier(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent )
        {
            return (parent instanceof VTimeZone) ? true : false;
        }
    },
    // Time Zone
    TIME_ZONE_NAME ("TZNAME", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castProperty = (StandardOrDaylight<?>) vComponent;
            return castProperty.getTimeZoneNames();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vParent;
            final ObservableList<TimeZoneName> list;
            if (castComponent.getTimeZoneNames() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setTimeZoneNames(list);
            } else
            {
                list = castComponent.getTimeZoneNames();
            }
            TimeZoneName child = TimeZoneName.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            final ObservableList<TimeZoneName> list;
            if (castDestination.getTimeZoneNames() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setTimeZoneNames(list);
            } else
            {
                list = castDestination.getTimeZoneNames();
            }
            list.add(new TimeZoneName((TimeZoneName) childSource));
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetFrom();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vParent;
            TimeZoneOffsetFrom child = TimeZoneOffsetFrom.parse(propertyContent);
            castComponent.setTimeZoneOffsetFrom(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetFrom propertyCopy = new TimeZoneOffsetFrom((TimeZoneOffsetFrom) childSource);
            castDestination.setTimeZoneOffsetFrom(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetTo();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vParent;
            TimeZoneOffsetTo child = TimeZoneOffsetTo.parse(propertyContent);
            castComponent.setTimeZoneOffsetTo(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetTo propertyCopy = new TimeZoneOffsetTo((TimeZoneOffsetTo) childSource);
            castDestination.setTimeZoneOffsetTo(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneURL();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vParent;
            TimeZoneURL child = TimeZoneURL.parse(propertyContent);
            castComponent.setTimeZoneURL(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneURL propertyCopy = new TimeZoneURL((TimeZoneURL) childSource);
            castDestination.setTimeZoneURL(propertyCopy);
        }
    },
    // Alarm
    TRIGGER ("TRIGGER", // property name
            Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.ALARM_TRIGGER_RELATIONSHIP, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            Trigger.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getTrigger();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vParent;
            Trigger<?> child = Trigger.parse(propertyContent);
            castComponent.setTrigger(child);
            return child;            
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            Trigger<?> propertyCopy = new Trigger<>((Trigger<?>) childSource);
            castDestination.setTrigger(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Relationship
    UNIQUE_IDENTIFIER ("UID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            UniqueIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPersonal<?> castComponent = (VPersonal<?> ) vComponent;
            return castComponent.getUniqueIdentifier();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPersonal<?> castComponent = (VPersonal<?> ) vParent;
            UniqueIdentifier child = UniqueIdentifier.parse(propertyContent);
            castComponent.setUniqueIdentifier(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPersonal<?> castDestination = (VPersonal<?>) destination;
            UniqueIdentifier propertyCopy = new UniqueIdentifier((UniqueIdentifier) childSource);
            castDestination.setUniqueIdentifier(propertyCopy);
        }
    },
    // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD), // allowed parameters
            UniformResourceLocator.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VPersonal<?> castComponent = (VPersonal<?> ) vComponent;
            return castComponent.getUniformResourceLocator();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VPersonal<?> castComponent = (VPersonal<?> ) vParent;
            UniformResourceLocator child = UniformResourceLocator.parse(propertyContent);
            castComponent.setUniformResourceLocator(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            VPersonal<?> castDestination = (VPersonal<?>) destination;
            UniformResourceLocator propertyCopy = new UniformResourceLocator((UniformResourceLocator) childSource);
            castDestination.setUniformResourceLocator(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Calendar property
    VERSION ("VERSION" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD) // allowed parameters
            , Version.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VCalendar vCalendar = (VCalendar) vParent;
            Version child = Version.parse(propertyContent);
            vCalendar.setVersion(child);
            return child;
        }

        @Override
        public void copyProperty(Property<?> childSource, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    };
    
    private static Map<String, PropertyType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, PropertyType> makeEnumFromNameMap()
    {
        Map<String, PropertyType> map = new HashMap<>();
        PropertyType[] values = PropertyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static PropertyType enumFromName(String propertyName)
    {
        final PropertyType prop;
        if (propertyName.length() < 3) return null; // minimum property name is 3 characters
        if (propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString()))
        {
            prop = PropertyType.NON_STANDARD;
//        } else if ((IANAProperty.getRegisteredIANAPropertys() != null) && IANAProperty.getRegisteredIANAPropertys().contains(propertyName))
//        {
//            prop = PropertyType.IANA_PROPERTY;            
        } else
        {
            prop = enumFromNameMap.get(propertyName);   
        }
        return prop;
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Property>, PropertyType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Property>, PropertyType> makeEnumFromClassMap()
    {
        Map<Class<? extends Property>, PropertyType> map = new HashMap<>();
        PropertyType[] values = PropertyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static PropertyType enumFromClass(Class<? extends VElement> myClass)
    {
        return enumFromClassMap.get(myClass);
    }
    
    private Class<? extends Property> myClass;
    public Class<? extends Property> getPropertyClass() { return myClass; }

    @Override
    public String toString() { return name; }
    private String name;
    
    private List<ValueType> valueTypes;
    public List<ValueType> allowedValueTypes() { return valueTypes; }

    private List<ParameterType> allowedParameters;
    public List<ParameterType> allowedParameters() { return allowedParameters; }
    
    PropertyType(String name, List<ValueType> valueTypes, List<ParameterType> allowedParameters, Class<? extends Property> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.valueTypes = valueTypes;
        this.myClass = myClass;
    }
    /*
     * ABSTRACT METHODS
     */
    /** Returns associated Property<?> or List<Property<?>> */
    abstract public Object getProperty(VComponent vComponent);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
    abstract public VChild parse(VParent vParent, String propertyContent);
//    abstract public VChild parse(VParent vParent, String propertyContent);

    /** copies the associated property from the source component to the destination component */
    abstract public void copyProperty(Property<?> childSource, VComponent destination);
//    abstract public void copyProperty(Property<?> childSource, VComponent destination);
    
    /** If property is required returns true, false otherwise */
    public boolean isRequired(VParent parent ) { return false; }
}
