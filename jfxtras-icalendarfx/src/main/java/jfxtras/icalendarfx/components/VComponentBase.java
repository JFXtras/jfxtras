package jfxtras.icalendarfx.components;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.CalendarComponent;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.VParentBase;
import jfxtras.icalendarfx.content.MultiLineContent;
import jfxtras.icalendarfx.properties.Property;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.UnfoldingStringIterator;;

/**
 * <p>Base class implementation of a {@link VComponent}</p>
 * 
 * @author David Bal
 */
public abstract class VComponentBase extends VParentBase implements VComponent
{
    private static final String FIRST_LINE_PREFIX = "BEGIN:";
    private static final String LAST_LINE_PREFIX = "END:";
    
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
//    @Deprecated
//    public void copyFrom(VComponent source)
//    {
//        myParent = source.getParent();
//        copyChildrenFrom(source);
//    }
    
    @Override
    public void copyInto(VParent destination)
    {
        super.copyInto(destination);
        childrenUnmodifiable().forEach((child) -> 
        {
            PropertyType type = PropertyType.enumFromClass(child.getClass());
            if (type != null)
            { /* Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT
               * and copying happens in overridden version of this method in subclasses */
                type.copyProperty((Property<?>) child, (VComponent) destination);
            } 
        });
    }
        
//    @Override
//    protected Callback<VChild, Void> copyIntoCallback()
//    {        
//        return (childSource) ->
//        {
//            PropertyType type = PropertyType.enumFromClass(childSource.getClass());
//            if (type != null)
//            { /* Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT
//               * and copying happens in overridden version of this method in subclasses */
//                type.copyProperty((Property<?>) childSource, this);
//            }
//            return null;
//        };
//    }
    
    final private String componentName;
    @Override
    public String name() { return componentName; }

    /*
     * CONSTRUCTORS
     */
    /**
     * Create default component by setting {@link componentName}, and setting content line generator.
     */
    VComponentBase()
    {
        componentName = CalendarComponent.enumFromClass(this.getClass()).toString();
        setContentLineGenerator(new MultiLineContent(
                orderer(),
                FIRST_LINE_PREFIX + componentName,
                LAST_LINE_PREFIX + componentName,
                400));
    }
    
    /**
     * Creates a deep copy of a component
     */
    VComponentBase(VComponentBase source)
    {
        this();
        source.copyInto(this);
//        copyFrom(source);
    }
    
    @Override
    public List<String> parseContent(String content)
    {
        return parseContent(content, false)
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream().map(v -> e.getKey().name() + ":" + v))
                .collect(Collectors.toList());
    }
    
    public Map<VElement, List<String>> parseContent(String content, boolean useRequestStatus)
    {
        if (content == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        content.indexOf(System.lineSeparator());
        List<String> contentLines = Arrays.asList(content.split(System.lineSeparator()));
        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(contentLines.iterator());
        return parseContent(unfoldedLineIterator, useRequestStatus);
    }

    @Override
    public Map<VElement, List<String>> parseContent(UnfoldingStringIterator unfoldedLineIterator, boolean collectErrorMessages)
    {
        if (unfoldedLineIterator == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        Map<VElement, List<String>> messageMap = new HashMap<>();
        List<String> myMessages = new ArrayList<>();
        
        // handle exceptions in JavxFx threads by re-throwing
        Thread t = Thread.currentThread();
        UncaughtExceptionHandler originalExceptionHandler = t.getUncaughtExceptionHandler();
        t.setUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        while (unfoldedLineIterator.hasNext())
        {
            String unfoldedLine = unfoldedLineIterator.next();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
            String propertyName = (nameEndIndex > 0) ? unfoldedLine.substring(0, nameEndIndex) : "";
            // Parse subcomponent
            if (propertyName.equals("BEGIN"))
            {
                boolean isMainComponent = unfoldedLine.substring(nameEndIndex+1).equals(name());
                if  (! isMainComponent)
                {
                    String subcomponentName = unfoldedLine.substring(nameEndIndex+1);
                    VComponent subcomponent = SimpleVComponentFactory.emptyVComponent(subcomponentName);
                    Map<VElement, List<String>> subMessages = subcomponent.parseContent(unfoldedLineIterator, collectErrorMessages);
                    messageMap.putAll(subMessages);
                    addSubcomponent(subcomponent);
                }
            } else if (propertyName.equals("END"))
            {
                break; // exit when end found
            } else
                // TODO - GENERATE RFC 5546 REQUEST STATUS MESSAGES
            {  // parse properties - ignore unknown properties
                PropertyType propertyType = PropertyType.enumFromName(propertyName);
                if (propertyType != null)
                {
                    Object existingProperty = propertyType.getProperty(this);
                    if (existingProperty == null || existingProperty instanceof List)
                    {
                        try
                        {
                            try
                            {
                                propertyType.parse(this, unfoldedLine);
                            } catch (Exception e)
                            {
                                if (propertyType.isRequired(this))
                                {
                                    myMessages.add("3.2;Invalid property value;" + unfoldedLine);
                                } else
                                {
                                    myMessages.add("2.2;Success; invalid property ignored;" + unfoldedLine);
                                }
                            }
                        } catch (Exception e)
                        { // exceptions from JavaFX thread
                            if (propertyType.isRequired(this))
                            {
                                myMessages.add("3.2;Invalid property value;" + unfoldedLine);
                            } else
                            {
                                myMessages.add("2.2;Success; invalid property ignored;" + unfoldedLine);
                            }
                        }
                    } else
                    {
                        myMessages.add("2.2;Success; invalid property ignored.  Property can only occur once in a calendar component.  Subsequent property is ignored;" + unfoldedLine);
                    }
                } else
                {
                    myMessages.add("2.4;Success; unknown, non-standard property ignored.;" + unfoldedLine);
                }
            }
        }
        if (myMessages.isEmpty())
        {
            myMessages.add("2.0;Success");
        }
//        System.out.println(System.identityHashCode(this) + " " + this.name() + " " + myMessages);
        messageMap.put(this, myMessages);
        t.setUncaughtExceptionHandler(originalExceptionHandler); // return original exception handler
        // TODO - Log status messages if not using RequestStatus
        return messageMap;
    }

    // Note: can't check equals or hashCode of parents - causes stack overflow

//    @Override
//    public boolean equals(Object obj)
//    {
//        boolean childrenEquals = super.equals(obj);
//        if (! childrenEquals) return false;
//        PropertyBase<?,?> testObj = (PropertyBase<?,?>) obj;
//        boolean parentEquals = (getParent() == null) ? testObj.getParent() == null : getParent() == testObj.getParent(); // equality by reference - if not desired only other option is equality of toContent, can't do equals method because it causes stack overflow (must have same reference to be equal)
//        return parentEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = super.hashCode();
//        final int prime = 31;
//        hash = prime * hash + System.identityHashCode(getParent()); // using identityHashCode because hashCode method causes cyclic stack overflow (must have same reference to be equal)
//        return hash;
//    }
    
    /**
     * Hook to add subcomponent such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * 
     * @param subcomponent
     */
    void addSubcomponent(VComponent subcomponent) { }
            
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + toContent();
    }
}
