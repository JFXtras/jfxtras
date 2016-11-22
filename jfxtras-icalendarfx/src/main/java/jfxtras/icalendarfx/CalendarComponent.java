package jfxtras.icalendarfx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropertyType;

/**
 * <p>Enumerated type containing all the {@link VChild} elements that can be in a {@link VCalendar}</p>
 * 
 * @author David Bal
 *
 */
// TODO - SPLIT INTO TWO ENUMS - ONE FOR COMPONENTS, ONE FOR VCALENDAR PROPERTIES
public enum CalendarComponent
{
    // MAIN COMPONENTS
    VEVENT ("VEVENT",
            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_CREATED,
            PropertyType.DATE_TIME_END, PropertyType.DATE_TIME_STAMP, PropertyType.DATE_TIME_START,
            PropertyType.DESCRIPTION, PropertyType.DURATION, PropertyType.EXCEPTION_DATE_TIMES,
            PropertyType.GEOGRAPHIC_POSITION, PropertyType.LAST_MODIFIED,
            PropertyType.LOCATION, PropertyType.NON_STANDARD, PropertyType.ORGANIZER, PropertyType.PRIORITY,
            PropertyType.RECURRENCE_DATE_TIMES, PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO,
            PropertyType.RECURRENCE_RULE, PropertyType.REQUEST_STATUS,  PropertyType.RESOURCES, PropertyType.SEQUENCE,
            PropertyType.STATUS, PropertyType.SUMMARY, PropertyType.TIME_TRANSPARENCY, PropertyType.UNIQUE_IDENTIFIER,
            PropertyType.UNIFORM_RESOURCE_LOCATOR),
//            true,
            VEvent.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VEvent e = new VEvent();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVEvents().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            destination.addVComponent(new VEvent((VEvent) child));
//            final ObservableList<VEvent> list;
//            if (destination.getVEvents() == null)
//            {
//                list = FXCollections.observableArrayList();
//                destination.setVEvents(list);
//            } else
//            {
//                list = destination.getVEvents();
//            }
//            list.add(new VEvent((VEvent) child));
        }
    },
    VTODO ("VTODO",
            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_COMPLETED,
            PropertyType.DATE_TIME_CREATED, PropertyType.DATE_TIME_DUE, PropertyType.DATE_TIME_STAMP,
            PropertyType.DATE_TIME_START, PropertyType.DESCRIPTION, PropertyType.DURATION,
            PropertyType.EXCEPTION_DATE_TIMES, PropertyType.GEOGRAPHIC_POSITION,
            PropertyType.LAST_MODIFIED, PropertyType.LOCATION,  PropertyType.NON_STANDARD, PropertyType.ORGANIZER,
            PropertyType.PERCENT_COMPLETE, PropertyType.PRIORITY, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO, PropertyType.RECURRENCE_RULE,
            PropertyType.REQUEST_STATUS, PropertyType.RESOURCES, PropertyType.SEQUENCE, PropertyType.STATUS,
            PropertyType.SUMMARY, PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
//            true,
            VTodo.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VTodo e = new VTodo();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVTodos().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            destination.addVComponent(new VTodo((VTodo) child));
        }

    },
    VJOURNAL ("VJOURNAL",
            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_CREATED,
            PropertyType.DATE_TIME_STAMP, PropertyType.DATE_TIME_START, PropertyType.DESCRIPTION,
            PropertyType.EXCEPTION_DATE_TIMES, PropertyType.LAST_MODIFIED,
            PropertyType.NON_STANDARD, PropertyType.ORGANIZER, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO, PropertyType.RECURRENCE_RULE, 
            PropertyType.REQUEST_STATUS, PropertyType.SEQUENCE, PropertyType.STATUS, PropertyType.SUMMARY,
            PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
//            true,
            VJournal.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VJournal e = new VJournal();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVJournals().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            destination.addVComponent(new VJournal((VJournal) child));
        }

    },
    VTIMEZONE ("VTIMEZONE",
            Arrays.asList(PropertyType.LAST_MODIFIED, PropertyType.NON_STANDARD,
            PropertyType.TIME_ZONE_IDENTIFIER, PropertyType.TIME_ZONE_URL),
//            true,
            VTimeZone.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VTimeZone e = new VTimeZone();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVTimeZones().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            destination.addVComponent(new VTimeZone((VTimeZone) child));
        }

    },
    VFREEBUSY ("VFREEBUSY",
            Arrays.asList(PropertyType.ATTENDEE, PropertyType.COMMENT, PropertyType.CONTACT,
            PropertyType.DATE_TIME_END, PropertyType.DATE_TIME_STAMP, PropertyType.DATE_TIME_START,
            PropertyType.FREE_BUSY_TIME, PropertyType.NON_STANDARD, PropertyType.ORGANIZER,
            PropertyType.REQUEST_STATUS, PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
//            true,
            VFreeBusy.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VFreeBusy e = new VFreeBusy();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVFreeBusies().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            throw new RuntimeException("not implemented");
            
        }
    },
    // TODO - Below should probably be removed?
    // NON-MAIN COMPONENTS - MUST BE NESTED IN A MAIN COMPONENT
    DAYLIGHT_SAVING_TIME ("DAYLIGHT",
            Arrays.asList(PropertyType.COMMENT, PropertyType.DATE_TIME_START,
            PropertyType.NON_STANDARD, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_RULE, PropertyType.TIME_ZONE_NAME, PropertyType.TIME_ZONE_OFFSET_FROM,
            PropertyType.TIME_ZONE_OFFSET_TO),
//            false,
            DaylightSavingTime.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            throw new RuntimeException("Not a main component - must be embedded inside a VTimeZone");
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            throw new RuntimeException("not implemented");
        }

    },
    STANDARD_TIME ("STANDARD",
            Arrays.asList(PropertyType.COMMENT, PropertyType.DATE_TIME_START,
            PropertyType.NON_STANDARD, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_RULE, PropertyType.TIME_ZONE_NAME, PropertyType.TIME_ZONE_OFFSET_FROM,
            PropertyType.TIME_ZONE_OFFSET_TO),
//            false,
            StandardTime.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            throw new RuntimeException("Not a main component - must be embedded inside a VTimeZone");
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            throw new RuntimeException("not implemented");
            
        }

    },
    VALARM ("VALARM",
            Arrays.asList(PropertyType.ACTION, PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.DESCRIPTION,
            PropertyType.DURATION, PropertyType.NON_STANDARD, PropertyType.REPEAT_COUNT,
            PropertyType.SUMMARY, PropertyType.TRIGGER),
//            false,
            VAlarm.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            throw new RuntimeException("Not a main component - must be embedded inside a VEvent or VTodo");
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
            throw new RuntimeException("not implemented");
            
        }

    };
//    // CALENDAR PROPERTIES
//    CALENDAR_SCALE (ComponentProperty.CALENDAR_SCALE.toString(), null, true, CalendarScale.class)
//    {
//        @Override
//        public VElement parse(VCalendar vCalendar, List<String> contentLines)
//        {
//            final String line;
//            if (contentLines.size() == 1)
//            {
//                line = contentLines.get(0);
//            } else
//            {
//                throw new IllegalArgumentException(toString() + " can only have one line of content");
//            }
//            CalendarScale property = CalendarScale.parse(line);
//            vCalendar.setCalendarScale(property);
//            return property;
//        }
//
//        @Override
//        public void copyChild(VChild child, VCalendar destination)
//        {
//            throw new RuntimeException("not implemented");
//            
//        }
//    },
//    METHOD (ComponentProperty.METHOD.toString(), null, true, Method.class)
//    {
//        @Override
//        public VElement parse(VCalendar vCalendar, List<String> contentLines)
//        {
//            final String line;
//            if (contentLines.size() == 1)
//            {
//                line = contentLines.get(0);
//            } else
//            {
//                throw new IllegalArgumentException(toString() + " can only have one line of content");
//            }
//            Method property = Method.parse(line);
//            vCalendar.setMethod(property);
//            return property;
//        }
//
//        @Override
//        public void copyChild(VChild child, VCalendar destination)
//        {
//            throw new RuntimeException("not implemented");
//            
//        }
//    },
//    PRODUCT_IDENTIFIER (ComponentProperty.PRODUCT_IDENTIFIER.toString(), null, true, ProductIdentifier.class)
//    {
//        @Override
//        public VElement parse(VCalendar vCalendar, List<String> contentLines)
//        {
//            final String line;
//            if (contentLines.size() == 1)
//            {
//                line = contentLines.get(0);
//            } else
//            {
//                throw new IllegalArgumentException(toString() + " can only have one line of content");
//            }
//            ProductIdentifier property = ProductIdentifier.parse(line);
//            vCalendar.setProductIdentifier(property);
//            return property;
//        }
//
//        @Override
//        public void copyChild(VChild child, VCalendar destination)
//        {
//            throw new RuntimeException("not implemented");
//            
//        }
//    },
//    VERSION (ComponentProperty.VERSION.toString(), null, true, Version.class)
//    {
//        @Override
//        public VElement parse(VCalendar vCalendar, List<String> contentLines)
//        {
//            final String line;
//            if (contentLines.size() == 1)
//            {
//                line = contentLines.get(0);
//            } else
//            {
//                throw new IllegalArgumentException(toString() + " can only have one line of content");
//            }
//            Version property = Version.parse(line);
//            vCalendar.setVersion(property);
//            return property;
//        }
//
//        @Override
//        public void copyChild(VChild child, VCalendar destination)
//        {
//            throw new RuntimeException("not implemented");
//            
//        }
//    };

    // Map to match up name to enum
    private static Map<String, CalendarComponent> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, CalendarComponent> makeEnumFromNameMap()
    {
        Map<String, CalendarComponent> map = new HashMap<>();
        CalendarComponent[] values = CalendarComponent.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static CalendarComponent enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends VElement>, CalendarComponent> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends VElement>, CalendarComponent> makeEnumFromClassMap()
    {
        Map<Class<? extends VElement>, CalendarComponent> map = new HashMap<>();
        CalendarComponent[] values = CalendarComponent.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static CalendarComponent enumFromClass(Class<? extends VElement> myClass)
    {
        return enumFromClassMap.get(myClass);
    }
    
    private Class<? extends VElement> myClass;
    public Class<? extends VElement> getElementClass() { return myClass; }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    private List<PropertyType> allowedProperties;
    public List<PropertyType> allowedProperties() { return allowedProperties; }

//    private boolean isCalendarElement;
//    public boolean isCalendarElement() { return isCalendarElement; }
    
    CalendarComponent(String name, List<PropertyType> allowedProperties, Class<? extends VElement> myClass)
    {
        this.name = name;
        this.allowedProperties = allowedProperties;
//        this.isCalendarElement = isCalendarElement;
        this.myClass = myClass;
    }

//    abstract public List<? extends VComponent> getComponents(VCalendar vCalendar);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
//    abstract public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines);
    
    abstract public void copyChild(VChild child, VCalendar destination);
//    {
//        throw new RuntimeException("not implemented");
//    }
}
