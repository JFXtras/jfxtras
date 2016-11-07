package jfxtras.icalendarfx.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.util.Pair;
import jfxtras.icalendarfx.VCalendar;

// TODO - COMBINE ICalendarUtilities and VCalendarUtilities
@Deprecated
public final class VCalendarUtilities
{
    private VCalendarUtilities() { }
    
    /**
     * Parse iCalendar ics and add its properties to vCalendar parameter
     * 
     * @param icsFilePath - URI of ics file
     * @param vCalendar - vCalendar obj2ect with callbacks set for making components (e.g. makeVEventCallback)
     */
    @Deprecated
    public static void parseICalendarFile(Path icsFilePath, VCalendar vCalendar)
    {
        ExecutorService service = Executors.newSingleThreadExecutor();
        List<Callable<Object>> tasks = new ArrayList<>();
        try
        {
            BufferedReader br = Files.newBufferedReader(icsFilePath);
            Iterator<String> lineIterator = br.lines().iterator();
            while (lineIterator.hasNext())
            {
                String line = lineIterator.next();
                Pair<String, String> p = ICalendarUtilities.parsePropertyLine(line); // TODO - REPLACE WITH PROPERTY NAME GET
                String propertyName = p.getKey();
                Arrays.stream(VCalendarComponent.values())
                        .forEach(property -> 
                        {
                            boolean matchOneLineProperty = propertyName.equals(property.toString());
                            if (matchOneLineProperty)
                            {
                                property.parseAndSetProperty(vCalendar, p.getValue());
                            } else if (line.equals(property.startDelimiter()))
                            {// multi-line property
                                StringBuilder propertyValue = new StringBuilder(line + System.lineSeparator());
                                boolean matchEnd = false;
                                do
                                {
                                    String propertyLine = lineIterator.next();
                                    matchEnd = propertyLine.equals(property.endDelimiter());
                                    propertyValue.append(propertyLine + System.lineSeparator());
                                } while (! matchEnd);
                                Runnable multiLinePropertyRunnable = () -> property.parseAndSetProperty(vCalendar, propertyValue.toString());
                                tasks.add(Executors.callable(multiLinePropertyRunnable));
                            } // otherwise, unknown property should be ignored
                        });
            }
                service.invokeAll(tasks);
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }  
    }
    
    /**
     * Parse iCalendar ics and add its properties to vCalendar parameter
     * 
     * @param icsFilePath - URI of ics file
     * @param vCalendar - vCalendar obj2ect with callbacks set for making components (e.g. makeVEventCallback)
     */
    public static VCalendar parseICalendarFile(Path icsFilePath)
    {
        VCalendar vCalendar = new VCalendar();
        ExecutorService service = Executors.newSingleThreadExecutor();
        List<Callable<Object>> tasks = new ArrayList<>();
        try
        {
            BufferedReader br = Files.newBufferedReader(icsFilePath);
            Iterator<String> lineIterator = br.lines().iterator();
            while (lineIterator.hasNext())
            {
                String line = lineIterator.next();
                Pair<String, String> p = ICalendarUtilities.parsePropertyLine(line); // TODO - REPLACE WITH PROPERTY NAME GET
                String propertyName = p.getKey();
                Arrays.stream(VCalendarComponent.values())
                        .forEach(property -> 
                        {
                            boolean matchOneLineProperty = propertyName.equals(property.toString());
                            if (matchOneLineProperty)
                            {
                                property.parseAndSetProperty(vCalendar, p.getValue());
                            } else if (line.equals(property.startDelimiter()))
                            {// multi-line property
                                StringBuilder propertyValue = new StringBuilder(line + System.lineSeparator());
                                boolean matchEnd = false;
                                do
                                {
                                    String propertyLine = lineIterator.next();
                                    matchEnd = propertyLine.equals(property.endDelimiter());
                                    propertyValue.append(propertyLine + System.lineSeparator());
                                } while (! matchEnd);
                                Runnable multiLinePropertyRunnable = () -> property.parseAndSetProperty(vCalendar, propertyValue.toString());
                                tasks.add(Executors.callable(multiLinePropertyRunnable));
                            } // otherwise, unknown property should be ignored
                        });
            }
                service.invokeAll(tasks);
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return vCalendar;
    }
    
//    public static boolean isEqualTo(VComponent obj1, VComponent obj2)
//    {
//        if (obj2 == obj1) return true;
//        if((obj2 == null) || (obj2.getClass() != obj1.getClass())) {
//            return false;
//        }
//        VComponent testObj = obj2;
//        
//        final boolean propertiesEquals;
//        List<PropertyType> properties = obj1.propertyEnums(); // make properties local to avoid creating list multiple times
//        List<PropertyType> testProperties = testObj.propertyEnums(); // make properties local to avoid creating list multiple times
//        if (properties.size() == testProperties.size())
//        {
//            Iterator<PropertyType> i1 = properties.iterator();
//            Iterator<PropertyType> i2 = testProperties.iterator();
//            boolean isFailure = false;
//            while (i1.hasNext())
//            {
//                Object p1 = i1.next().getProperty(obj1);
//                Object p2 = i2.next().getProperty(testObj);
//                if (! p1.equals(p2))
//                {
//                    isFailure = true;
//                    break;
//                }
//            }
//            propertiesEquals = ! isFailure;
//        } else
//        {
//            propertiesEquals = false;
//        }
//        return propertiesEquals;
//    }
    
    /**
     * Parses the property-value pair to the matching property, if a match is found.
     * If no matching property, does nothing.
     * 
     * @param vCalendar - obj2ect to add property values
     * @param propertyValuePair - property name-value pair (e.g. DTSTART and TZID=America/Los_Angeles:20160214T110000)
     */
    @Deprecated // make instance variable - implement ICalendarProperty
    public static void parse(VCalendar vCalendar, Pair<String, String> propertyValuePair)
    {
        String propertyName = propertyValuePair.getKey();
        String value = propertyValuePair.getValue();
        VCalendarComponent vCalendarProperty = VCalendarComponent.propertyFromString(propertyName);
        if (vCalendarProperty != null)
        {
            vCalendarProperty.parseAndSetProperty(vCalendar, value);
        }
    }
    @Deprecated
    public enum VCalendarComponent
    {
        CALENDAR_SCALE ("CALSCALE") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                if ((vCalendar.getCalendarScale() == VCalendar.DEFAULT_CALENDAR_SCALE) || (vCalendar.getCalendarScale() == null))
//                {
//                    vCalendar.setCalendarScale(value);
//                } else
//                {
//                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
//                }
            }

            @Override
            public String startDelimiter() { return toString(); }

            @Override
            public String endDelimiter() { return null; }
        } ,
        OBJECT_METHOD ("METHOD") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                if (vCalendar.getObjectMethod() == null)
//                {
//                    vCalendar.setObjectMethod(value);
//                } else
//                {
//                    System.out.println("method:" + vCalendar.getObjectMethod() + "e");
//                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
//                }
            }

            @Override
            public String startDelimiter() { return toString(); }

            @Override
            public String endDelimiter() { return null; }
        } ,
        PRODUCT_IDENTIFIER ("PRODID") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                if ((vCalendar.getProductIdentifier() == VCalendar.DEFAULT_PRODUCT_IDENTIFIER) || (vCalendar.getProductIdentifier() == null))
//                {
//                    vCalendar.setProductIdentifier(value);
//                } else
//                {
//                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
//                }
            }

            @Override
            public String startDelimiter() { return toString(); }

            @Override
            public String endDelimiter() { return null; }
        } ,
        ALARM_COMPONENT ("VALARM") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                throw new RuntimeException(toString() + " not supported.");
            }

            @Override
            public String startDelimiter() { return "BEGIN:" + toString(); }

            @Override
            public String endDelimiter() { return "END:" + toString(); }
        } ,
        EVENT_COMPONENT ("VEVENT") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                vCalendar.vEvents().add( vCalendar.getMakeVEventCallback().call(value) );
            }

            @Override
            public String startDelimiter() { return "BEGIN:" + toString(); }

            @Override
            public String endDelimiter() { return "END:" + toString(); }
        } ,
        ICALENDAR_SPECIFICATION_VERSION ("VERSION") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                if ((vCalendar.getICalendarSpecificationVersion() == VCalendar.DEFAULT_ICALENDAR_SPECIFICATION_VERSION) || (vCalendar.getICalendarSpecificationVersion() == null))
//                {
//                    vCalendar.setICalendarSpecificationVersion(value);
//                } else
//                {
//                    throw new IllegalArgumentException(toString() + " can only appear once in calendar");                    
//                }
            }

            @Override
            public String startDelimiter() { return toString(); }

            @Override
            public String endDelimiter() { return null; }
        } ,
        FREE_BUSY_COMPONENT ("VFREEBUSY") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                // not supported - ignore property
            }

            @Override
            public String startDelimiter() { return "BEGIN:" + toString(); }

            @Override
            public String endDelimiter() { return "END:" + toString(); }
        } ,
        JOURNALCOMPONENT ("VJOURNAL") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                vCalendar.vJournals().add( vCalendar.getMakeVJournalCallback().call(value) );
            }

            @Override
            public String startDelimiter() { return "BEGIN:" + toString(); }

            @Override
            public String endDelimiter() { return "END:" + toString(); }
        } ,
        TIME_ZONE_COMPONENT ("VTIMEZONE") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
                // not supported - ignore property
            }

            @Override
            public String startDelimiter() { return "BEGIN:" + toString(); }

            @Override
            public String endDelimiter() { return "END:" + toString(); }
        } ,
        TO_DO_COMPONENT ("VTODO") {
            @Override
            public void parseAndSetProperty(VCalendar vCalendar, String value)
            {
//                vCalendar.vTodos().add( vCalendar.getMakeVTodoCallback().call(value) );
            }

            @Override
            public String startDelimiter() { return "BEGIN:" + toString(); }

            @Override
            public String endDelimiter() { return "END:" + toString(); }
        };
        
        // Map to match up string tag to VCalendarProperty enum
        private static Map<String, VCalendarComponent> propertyFromTagMap = makePropertiesFromNameMap();
        private static Map<String, VCalendarComponent> makePropertiesFromNameMap()
        {
            Map<String, VCalendarComponent> map = new HashMap<>();
            VCalendarComponent[] values = VCalendarComponent.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        
        private String name;
        
        VCalendarComponent(String name)
        {
            this.name = name;
        }
        
        /** Returns the iCalendar property name (e.g. DTSTAMP) */
        @Override
        public String toString() { return name; }
        
        /*
         * ABSTRACT METHODS
         */
       
        /** sets VComponent's property for this VComponentProperty to parameter value
         * value is a string that is parsed if necessary to the appropriate type
         */
        public abstract void parseAndSetProperty(VCalendar vCalendar, String value);

        /** returns string that delimitates beginning of property */
        public abstract String startDelimiter();

        /** returns string that delimitates end of property.  Is null for one-line properties*/
        public abstract String endDelimiter();

        /*
         * STATIC METHODS
         */
    
        /** get VCalendarProperty enum from property name */
        public static VCalendarComponent propertyFromString(String propertyName)
        {
            return propertyFromTagMap.get(propertyName.toUpperCase());
        }
    }
}
