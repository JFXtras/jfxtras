package jfxtras.icalendarfx.components;

import jfxtras.icalendarfx.CalendarComponent;

/**
 * <p>Parses calendar content text to a {@link VComponent}</p>
 * 
 * <p>Creates the correct calendar component depending on the text following the "BEGIN" keyword.
 * For example BEGIN:VEVENT will result in a new {@link VEvent}</p>
 * 
 * @author David Bal
 *
 */
public class SimpleVComponentFactory
{
//    /** Create new VComponent from component name and parsing iCalendar content text */
//    public static VComponent newVComponent(String componentName, Iterator<String> contentIterator)
//    {
//        final VComponent myComponent;
//        CalendarComponent component = CalendarComponent.enumFromName(componentName.toString());
//        if (component == null)
//        {
//            throw new IllegalArgumentException(componentName + " is not a valid calendar component name.");            
//        }
//        switch (component)
//        {
//        case DAYLIGHT_SAVING_TIME:
//            myComponent = new DaylightSavingTime();
//            break;
//        case STANDARD_TIME:
//            myComponent = new StandardTime();
//            break;
//        case VALARM:
//            myComponent = new VAlarm();
//            break;
//        case VEVENT:
//            myComponent = new VEvent();
//            break;
//        case VFREEBUSY:
//            myComponent = new VFreeBusy();
//            break;
//        case VJOURNAL:
//            myComponent = new VJournal();
//            break;
//        case VTIMEZONE:
//            myComponent = new VTimeZone();
//            break;
//        case VTODO:
//            myComponent = new VTodo();
//            break;
//        default:
//            throw new IllegalArgumentException("Unsupported component:" + component);
//        }
//        boolean collectErrorList = false;
//        myComponent.parseContent(contentIterator, collectErrorList);
//        return myComponent;
//    }
//    
//    /** Create new VComponent by parsing iCalendar content text */
//    public static VComponent newVComponent(Iterator<String> contentIterator)
//    {
//        final VComponent myComponent;
//        String line = contentIterator.next();
//        int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
//        String propertyName = line.substring(0, nameEndIndex);
//        
//        // Parse component
//        if ("BEGIN".contentEquals(propertyName))
//        {
//            // make new component
//            String componentName = line.substring(nameEndIndex+1, line.length());
//            myComponent = newVComponent(componentName, contentIterator);
//        } else
//        {
//            throw new IllegalArgumentException("First content line MUST start with \"BEGIN\" not:" + line);
//        }        
//        return myComponent;
//    }
//    
//    public static VComponent parseVComponent(String contentText)
//    {
//        List<String> contentLines = Arrays.asList(contentText.split(System.lineSeparator()));
//        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
//        return newVComponent(unfoldedLines);
//    }
    
    /**
     * 
     * @param contentText
     * @return
     */
    public static VComponent emptyVComponent(String contentText)
    {
        int endFirstLine = contentText.indexOf(System.lineSeparator());
        endFirstLine = (endFirstLine < 0) ? contentText.length() : endFirstLine;
        String firstLine = contentText.substring(0, endFirstLine);
        String componentName = firstLine.replace("BEGIN:", "");

        // make new component
        final VComponent myComponent;
        CalendarComponent component = CalendarComponent.enumFromName(componentName.toString());
        if (component == null)
        {
            throw new IllegalArgumentException(componentName + " is not a valid calendar component name.");            
        }
        switch (component)
        {
        case DAYLIGHT_SAVING_TIME:
            myComponent = new DaylightSavingTime();
            break;
        case STANDARD_TIME:
            myComponent = new StandardTime();
            break;
        case VALARM:
            myComponent = new VAlarm();
            break;
        case VEVENT:
            myComponent = new VEvent();
            break;
        case VFREEBUSY:
            myComponent = new VFreeBusy();
            break;
        case VJOURNAL:
            myComponent = new VJournal();
            break;
        case VTIMEZONE:
            myComponent = new VTimeZone();
            break;
        case VTODO:
            myComponent = new VTodo();
            break;
        default:
            throw new IllegalArgumentException("Unsupported component:" + component);
        }
        return myComponent;
    }
    
//    /**
//     * 
//     * @param contentText  iCalendar content lines
//     * @return  created VComponent with {@link RequestStatus REQUEST-STATUS} properties containing result
//     */
//    public static VComponent importVComponent(String contentText)
//    {
//        List<String> contentLines = Arrays.asList(contentText.split(System.lineSeparator()));
//        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
//        return newVComponent(unfoldedLines);
//    }
}
