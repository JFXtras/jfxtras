package jfxtras.icalendarfx.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.ObjectProperty;
import javafx.util.Pair;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.utilities.VCalendarUtilities.VCalendarComponent;

/**
 * Static utility methods used throughout iCalendar
 * 
 * @author David Bal
 *
 */
// NEEDS TO BE CLEANED UP - REMOVE UNUSED METHODS
public final class ICalendarUtilities
{
    private ICalendarUtilities() { };
    
    public final static String PROPERTY_VALUE_KEY = ":";

//    public static List<String> splitStringToLines(String text)
//    {
//        int lastIndex = 0;
//        int length = text.length();
//        List<String> list = new ArrayList<>(length / 80);
//        int index = 0;
//        while (index < length)
//        {
//            int split = text.indexOf(System.lineSeparator(), index+1);
////            System.out.println("split:" + split + " " + index);
//            if (split > 0)
//            {
//                list.add(text.substring(index, split));
//                index = split;
//            } else
//            {
//                break;
//            }
//            if (lastIndex == index) break;
//            lastIndex = index;
//        }
//        list.add(text.substring(index));
//        return list;
//    }
    
//    /**
//     * Starting with lines-separated list of content lines, the lines are unwrapped and 
//     * converted into a list of property name and property value list wrapped in a Pair.
//     * DTSTART property is put on top, if present.
//     * 
//     * This method unwraps multi-line content lines as defined in iCalendar RFC5545 3.1, page 9
//     * 
//     * For example:
//     * string is
//     * BEGIN:VEVENT
//     * SUMMARY:test1
//     * DTSTART;TZID=Etc/GMT:20160306T080000Z
//     * DTEND;TZID=Etc/GMT:20160306T093000Z
//     * RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
//     * UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
//     * DTSTAMP:20160313T053147Z
//     * END:VEVENT
//     *  
//     *  results in list of Pair<String,String>
//     *  Pair 1, key = SUMMARY, value = test1
//     *  Pair 2, key = DTSTART, value = TZID=Etc/GMT:20160306T080000Z
//     *  Pair 3, key = DTEND, value = TZID=Etc/GMT:20160306T093000Z
//     *  Pair 4, key = RRULE, value = FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
//     *  Pair 5, key = UID, value = fc3577e0-8155-4fa2-a085-a15bdc50a5b4
//     *  Pair 6, key = DTSTAMP, value = 20160313T053147Z
//     *  
//     *  Note: the list of Pair<String,String> is used instead of a Map<String,String> because some properties
//     *  can appear more than once resulting in duplicate key values.
//     *  
//     * @param componentString
//     * @return
//     */
//    public static List<String> componentStringToPropertyList(String componentString)
//    {
////        List<Pair<String,String>> propertyPairs = new ArrayList<>();
//        List<String> propertyLines = new ArrayList<>();
//        String storedLine = "";
//        Iterator<String> lineIterator = Arrays.stream( componentString.split(System.lineSeparator()) ).iterator();
//        while (lineIterator.hasNext())
//        {
//            // unwrap lines by storing previous line, adding to it if next is a continuation
//            // when no more continuation lines are found loop back and start with last storedLine
//            String startLine;
//            if (storedLine.isEmpty())
//            {
//                startLine = lineIterator.next();
//            } else
//            {
//                startLine = storedLine;
//                storedLine = "";
//            }
//            StringBuilder builder = new StringBuilder(startLine);
//            while (lineIterator.hasNext())
//            {
//                String anotherLine = lineIterator.next();
//                if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
//                { // unwrap anotherLine into line
//                    builder.append(anotherLine.substring(1, anotherLine.length()));
//                } else
//                {
//                    storedLine = anotherLine; // save for next iteration
//                    break;  // no continuation line, exit while loop
//                }
//            }
//            String line = builder.toString();
//            propertyLines.add(line);
////            Pair<String, String> pair = parsePropertyLine(line);
////            if (pair != null) { propertyPairs.add(pair); }
//        }
//        Collections.sort(propertyLines, DTSTART_FIRST_COMPARATOR); // put DTSTART property on top of list
//        return propertyLines;
//    }
    
//    /**
//     * parse property content line into a parameter name/value map
//     * content line must have the property name stripped off the front
//     * 
//     * For example, for the content line DTEND;TZID=Etc/GMT:20160306T103000Z
//     * the propertyLine must be ;TZID=Etc/GMT:20160306T103000Z
//     * 
//     * @param propertyLine - name-stripped property line
//     * @return - map where key=parameter names as, value=parameter value
//     */
//    public static Map<String,String> propertyLineToParameterMap(String propertyLine)
//    {
//       Map<String,String> parameterMap = new LinkedHashMap<>();
//       // find start of parameters (go past property name)
//       int parameterStart=0;
//       for (parameterStart = 0; parameterStart < propertyLine.length(); parameterStart++)
//       {
//           if ((propertyLine.charAt(parameterStart) == ';') || (propertyLine.charAt(parameterStart) == ':'))
//           {
//               break;
//           } else if (propertyLine.charAt(parameterStart) == '=') // propertyLine doesn't contain the property name, start searching for parameters at beginning
//           {
//               parameterStart = -1;
//               break;
//           }
//       }
//       
//       // make adjustments before processing
//       char firstCharacter;      
//       if (parameterStart == propertyLine.length())
//       { // contains no property name, only value
//           parameterStart = 0; // reset to front of line because it only contains a value
//           firstCharacter = ':';
//       } else if (parameterStart == propertyLine.length()-1)
//       { // contains only property name, has no value, return empty value 
//           parameterMap.put(PROPERTY_VALUE_KEY, "");
//           return parameterMap;
//       } else if (parameterStart < 0)
//       { // doesn't contain the property name, but has parameters
//           firstCharacter = ';';
//           parameterStart = 0;
//       } else if (parameterStart == 0)
//       { // has parameter or value at start (first character is a ';' or ':')
//           firstCharacter = propertyLine.charAt(parameterStart);
//           parameterStart = 1;
//       } else
//       { // contains a property name and parameters and/or value
//           firstCharacter = propertyLine.charAt(parameterStart);
//           parameterStart++;
//       }
//       
//       // find parameters
//       int parameterEnd = parameterStart;
//       boolean quoteOn = false;
//       while (parameterEnd < propertyLine.length())
//       {
//           final String name;
//           final String value;
//           if (firstCharacter == ':')
//           { // found property value.  It continues to end of the string.
//               parameterEnd = propertyLine.length();
//               name = PROPERTY_VALUE_KEY;
//               value = propertyLine.substring(parameterStart, parameterEnd);
//           } else if (firstCharacter == ';')
//           { // found parameter/value pair.
//               int equalsPosition = propertyLine.indexOf('=', parameterStart);
//               int nextSemicolonPosition = propertyLine.indexOf(';', parameterStart);
//               if ((nextSemicolonPosition> 0) && (nextSemicolonPosition < equalsPosition))
//               { // parameter has no value
//                   value = null;
//                   name = propertyLine.substring(parameterStart, nextSemicolonPosition).toUpperCase();
//                   parameterEnd = nextSemicolonPosition;
////                   System.out.println("parameter no value:" + name);
//               } else
//               {
//                   name = propertyLine.substring(parameterStart, equalsPosition).toUpperCase();
//                   for (parameterEnd = equalsPosition+1; parameterEnd < propertyLine.length(); parameterEnd++)
//                   {
//                       if (propertyLine.charAt(parameterEnd) == '\"')
//                       {
//                           quoteOn = ! quoteOn;
//                       }
//                       if (! quoteOn) // can't end while quote is on
//                       {
//                           if ((propertyLine.charAt(parameterEnd) == ';') || (propertyLine.charAt(parameterEnd) == ':'))
//                           {
//                               break;
//                           }
//                       }
//                   }
//                   value = propertyLine.substring(equalsPosition+1, parameterEnd);
////                  System.out.println("parameter:" + value);
//               }
//           } else
//           {
//               throw new IllegalArgumentException("Invalid property line:" + propertyLine);
//           }
//           parameterMap.put(name, value);
//           if (parameterEnd < propertyLine.length())
//           {
//               parameterStart = parameterEnd+1;
//               firstCharacter = propertyLine.charAt(parameterStart-1);
//           }
//       }
//       return parameterMap;
//    }
    
    /**
     * parse property content line into a parameter name/value map
     * content line must have the property name stripped off the front
     * 
     * For example, for the content line DTEND;TZID=Etc/GMT:20160306T103000Z
     * the propertyLine must be ;TZID=Etc/GMT:20160306T103000Z
     * 
     * @param propertyLine - name-stripped property line
     * @return - map where key=parameter names as, value=parameter value
     */
    // TODO - CONSIDER USING STREAMTOKENIZER instead
    public static List<Pair<String,String>> contentToParameterListPair(String propertyLine)
    {
        List<Pair<String,String>> parameters = new ArrayList<>();
       // find start of parameters (go past property name)
       int parameterStart=0;
       for (parameterStart = 0; parameterStart < propertyLine.length(); parameterStart++)
       {
           if ((propertyLine.charAt(parameterStart) == ';') || (propertyLine.charAt(parameterStart) == ':'))
           {
               break;
           } else if (propertyLine.charAt(parameterStart) == '=') // propertyLine doesn't contain the property name, start searching for parameters at beginning
           {
               parameterStart = -1;
               break;
           }
       }
       
       // make adjustments before processing
       char firstCharacter;      
       if (parameterStart == propertyLine.length())
       { // contains no property name, only value
           parameterStart = 0; // reset to front of line because it only contains a value
           firstCharacter = ':';
       } else if (parameterStart == propertyLine.length()-1)
       { // contains only property name, has no value, return empty value 
           parameters.add(new Pair<>(PROPERTY_VALUE_KEY, ""));
           return parameters;
       } else if (parameterStart < 0)
       { // doesn't contain the property name, but has parameters
           firstCharacter = ';';
           parameterStart = 0;
       } else if (parameterStart == 0)
       { // has parameter or value at start (first character is a ';' or ':')
           firstCharacter = propertyLine.charAt(parameterStart);
           parameterStart = 1;
       } else
       { // contains a property name and parameters and/or value
           firstCharacter = propertyLine.charAt(parameterStart);
           parameterStart++;
       }
       
       // find parameters
       int parameterEnd = parameterStart;
       boolean quoteOn = false;
       while (parameterEnd < propertyLine.length())
       {
           final String name;
           final String value;
           if (firstCharacter == ':')
           { // found property value.  It continues to end of the string.
               parameterEnd = propertyLine.length();
               name = PROPERTY_VALUE_KEY;
               value = propertyLine.substring(parameterStart, parameterEnd);
           } else if (firstCharacter == ';')
           { // found parameter/value pair.
               int equalsPosition = propertyLine.indexOf('=', parameterStart);
               int nextSemicolonPosition = propertyLine.indexOf(';', parameterStart);
               if ((nextSemicolonPosition> 0) && (nextSemicolonPosition < equalsPosition))
               { // parameter has no value
                   value = null;
                   name = propertyLine.substring(parameterStart, nextSemicolonPosition).toUpperCase();
                   parameterEnd = nextSemicolonPosition;
//                   System.out.println("parameter no value:" + name);
               } else
               {
                   name = propertyLine.substring(parameterStart, equalsPosition).toUpperCase();
                   for (parameterEnd = equalsPosition+1; parameterEnd < propertyLine.length(); parameterEnd++)
                   {
                       if (propertyLine.charAt(parameterEnd) == '\"')
                       {
                           quoteOn = ! quoteOn;
                       }
                       if (! quoteOn) // can't end while quote is on
                       {
                           if ((propertyLine.charAt(parameterEnd) == ';') || (propertyLine.charAt(parameterEnd) == ':'))
                           {
                               break;
                           }
                       }
                   }
                   value = propertyLine.substring(equalsPosition+1, parameterEnd);
//                  System.out.println("parameter:" + value);
               }
           } else
           {
               throw new IllegalArgumentException("Invalid property line:" + propertyLine);
           }
           parameters.add(new Pair<>(name, value));
           if (parameterEnd < propertyLine.length())
           {
               parameterStart = parameterEnd+1;
               firstCharacter = propertyLine.charAt(parameterStart-1);
           }
       }
       return parameters;
    }
    
    /**
     * Returns iCalendar property name from property content line.
     * Returns empty string if propertyLine is null or empty
     * 
     * @param propertyLine - a iCalendar property line.  The property name must be at the beginning of the line
     * @return - property name
     */
    public static String getPropertyName(String propertyLine)
    {
        if ((propertyLine == null) || propertyLine.isEmpty())
        {
            return "";
        }
        int propertyNameEnd;
        for (propertyNameEnd=0; propertyNameEnd<propertyLine.length(); propertyNameEnd++)
        {
            if ((propertyLine.charAt(propertyNameEnd) == ';') || (propertyLine.charAt(propertyNameEnd) == ':'))
            {
                break;
            }
        }
        if (propertyNameEnd < propertyLine.length())
        {
            return propertyLine.substring(0, propertyNameEnd);
        } else
        {
            throw new IllegalArgumentException("Illegal property line.  No value after name:" + propertyLine);
        }
    }
    
    /**
     * Returns index where property name ends - after first ';' or ':'
     */
    public static int getPropertyNameIndex(String propertyLine)
    {
        if ((propertyLine == null) || (propertyLine.length() == 0))
        {
            return 0;
        }
        int propertyNameEnd;
        for (propertyNameEnd=0; propertyNameEnd<propertyLine.length(); propertyNameEnd++)
        {
            if ((propertyLine.charAt(propertyNameEnd) == ';') || (propertyLine.charAt(propertyNameEnd) == ':'))
            {
                break;
            }
        }
        if (propertyNameEnd < propertyLine.length())
        {
            return propertyNameEnd;
//            return propertyLine.substring(0, propertyNameEnd);
        } else
        {
            return -1; // no name
//            throw new IllegalArgumentException("Illegal property line.  No value after name:" + propertyLine);
        }
    }
    
    /**
     * Starting with lines-separated list of content lines, the lines are unwrapped and 
     * converted into a list of property name and property value list wrapped in a Pair.
     * DTSTART property is put on top, if present.
     * 
     * This method unwraps multi-line content lines as defined in iCalendar RFC5545 3.1, page 9
     * 
     * For example:
     * string is
     * BEGIN:VEVENT
     * SUMMARY:test1
     * DTSTART;TZID=Etc/GMT:20160306T080000Z
     * DTEND;TZID=Etc/GMT:20160306T093000Z
     * RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     * ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yaho
     *  o.com
     * UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
     * DTSTAMP:20160313T053147Z
     * END:VEVENT
     *  
     *  results in list
     *  Element 1, BEGIN:VEVENT
     *  Element 2, SUMMARY:test1
     *  Element 3, DTSTART;TZID=Etc/GMT:20160306T080000Z
     *  Element 4, DTEND;TZID=Etc/GMT:20160306T093000Z
     *  Element 5, RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     *  Element 5, ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yahoo.com
     *  Element 6, UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
     *  Element 7, DTSTAMP:20160313T053147Z
     *  Element 8, END:VEVENT
     *  
     *  Note: the list of Pair<String,String> is used instead of a Map<String,String> because some properties
     *  can appear more than once resulting in duplicate key values.
     *  
     * @param componentString  text of calendar component content lines
     * @return  {@code List<String>} of unfolded content lines, empty list if content lines is null
     */
//    @Deprecated // use unfolding reader instead
//    public static List<String> unfoldLines(String componentString)
//    {
//        List<String> propertyLines = new ArrayList<>();
//        if (componentString != null)
//        {
////            String storedLine = "";
//            Iterator<String> lineIterator = Arrays.stream( componentString.split(System.lineSeparator()) ).iterator();
//            return unfoldLines(lineIterator);
//
////            while (lineIterator.hasNext())
////            {
////                // unwrap lines by storing previous line, adding to it if next is a continuation
////                // when no more continuation lines are found loop back and start with last storedLine
////                String startLine;
////                if (storedLine.isEmpty())
////                {
////                    startLine = lineIterator.next();
////                } else
////                {
////                    startLine = storedLine;
////                    storedLine = "";
////                }
////                StringBuilder builder = new StringBuilder(startLine);
////                while (lineIterator.hasNext())
////                {
////                    String anotherLine = lineIterator.next();
////                    if (anotherLine.isEmpty()) continue; // ignore blank lines
////                    if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
////                    { // unwrap anotherLine into line
////                        builder.append(anotherLine.substring(1, anotherLine.length()));
////                    } else
////                    {
////                        storedLine = anotherLine; // save for next iteration
////                        break;  // no continuation line, exit while loop
////                    }
////                }
////                String line = builder.toString();
////                propertyLines.add(line);
////            }
//        }
////        Collections.sort(propertyLines, DTSTART_FIRST_COMPARATOR); // put DTSTART property on top of list (so I can get its Temporal type)
//        return propertyLines;
//    }
    
//    public static List<String> unfoldLines(List<String> foldedContent)
//    {
//        List<String> unfoldedContent = new ArrayList<>();
//        if (foldedContent != null)
//        {
////            String storedLine = "";
//            Iterator<String> lineIterator = foldedContent.iterator();
//            return unfoldLines(lineIterator);
////            while (lineIterator.hasNext())
////            {
////                // unwrap lines by storing previous line, adding to it if next is a continuation
////                // when no more continuation lines are found loop back and start with last storedLine
////                CharSequence startLine;
////                if (storedLine.length() == 0)
////                {
////                    startLine = lineIterator.next();
////                } else
////                {
////                    startLine = storedLine;
////                    storedLine = "";
////                }
////                StringBuilder builder = new StringBuilder(startLine);
////                while (lineIterator.hasNext())
////                {
////                    String anotherLine = lineIterator.next();
////                    if (anotherLine.length() == 0) continue; // ignore blank lines
////                    if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
////                    { // unwrap anotherLine into line
////                        builder.append(anotherLine.subSequence(1, anotherLine.length()));
////                    } else
////                    {
////                        storedLine = anotherLine; // save for next iteration
////                        break;  // no continuation line, exit while loop
////                    }
////                }
////                String unfoldedLine = builder.toString();
////                unfoldedContent.add(unfoldedLine);
////            }
//        }
////        Collections.sort(propertyLines, DTSTART_FIRST_COMPARATOR); // put DTSTART property on top of list (so I can get its Temporal type)
//        return unfoldedContent;
//    }
    
//    public static List<String> unfoldLines(Iterator<String> lineIterator)
//    {
//        List<String> propertyLines = new ArrayList<>();
//        String bufferedLine = "";
//        while (lineIterator.hasNext())
//        {
//            // unwrap lines by storing previous line, adding to it if next is a continuation
//            // when no more continuation lines are found loop back and start with last storedLine
//            String startLine;
//            if (bufferedLine.isEmpty())
//            {
//                startLine = lineIterator.next();
//            } else
//            {
//                startLine = bufferedLine;
//                bufferedLine = "";
//            }
//            StringBuilder builder = new StringBuilder(startLine);
//            while (lineIterator.hasNext())
//            {
//                String anotherLine = lineIterator.next();
//                if (anotherLine.isEmpty()) continue; // ignore blank lines
//                // Space or tab characters at index 0 indicate a fold
//                if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
//                { // unwrap anotherLine into line
//                    builder.append(anotherLine.substring(1, anotherLine.length()));
//                } else
//                {
//                    bufferedLine = anotherLine; // save for next iteration
//                    break;  // no continuation line, exit while loop
//                }
//            }
//            String line = builder.toString();
//            propertyLines.add(line);
//        }
//        if (! bufferedLine.isEmpty()) propertyLines.add(bufferedLine);
//        return propertyLines;
//    }
    
    /**
     * 
     * @param lineIterator  content text iterator
     * @param storedLine  last line from iterator, empty string if none
     * @return
     */
    @Deprecated
    public static String unfoldLines(Iterator<String> lineIterator, ObjectProperty<String> storedLine)
    {
        while (lineIterator.hasNext())
        {
            // unwrap lines by storing previous line, adding to it if next is a continuation
            // when no more continuation lines are found loop back and start with last storedLine
            String startLine;
            if (storedLine.get().isEmpty())
            {
                startLine = lineIterator.next();
            } else
            {
                startLine = storedLine.get();
                storedLine.set("");
            }
            StringBuilder builder = new StringBuilder(startLine);
            boolean isEnd = startLine.subSequence(0, 3).equals("END");
            if (! isEnd)
            {
                while (lineIterator.hasNext())
                {
                    String anotherLine = lineIterator.next();
                    if (anotherLine.isEmpty()) continue; // ignore blank lines
                    if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
                    { // unwrap anotherLine into line
                        builder.append(anotherLine.substring(1, anotherLine.length()));
                    } else
                    {
                        storedLine.set(anotherLine); // save for next iteration
                        break;  // no continuation line, exit while loop
                    }
                }
            }
            return builder.toString();
        }
        return null;
    }
    
    /**
     * Folds lines at character 75 into multiple lines.  Follows rules in
     * RFC 5545, 3.1 Content Lines, page 9.
     * A space is added to the first character of the subsequent lines.
     * doesn't break lines at escape characters
     * 
     * @param line - content line
     * @return - folded content line
     */
    public static CharSequence foldLine(CharSequence line)
    {
        // first position is 0
        final int maxLineLength = 75;
        if (line.length() <= maxLineLength)
        {
            return line;
        } else
        {
            StringBuilder builder = new StringBuilder(line.length()+20);
            int leadingSpaceAdjustment = 0;
            String leadingSpace = "";
            int startIndex = 0;
            while (startIndex < line.length())
            {
                int endIndex = Math.min(startIndex+maxLineLength-leadingSpaceAdjustment, line.length());
                if (endIndex < line.length())
                {
                    // ensure escaped characters are not broken up
                    if (line.charAt(endIndex-1) == '\\')
                    {
                        endIndex = endIndex-1; 
                    }
                    builder.append(leadingSpace + line.subSequence(startIndex, endIndex) + System.lineSeparator());
                } else
                {                    
                    builder.append(leadingSpace + line.subSequence(startIndex, endIndex));
                }
                startIndex = endIndex;
                leadingSpaceAdjustment = 1;
                leadingSpace = " ";
            }
            return builder;
        }
    }
    
    /**
     * Converts property line into a property-parameter/value map
     * For example:
     * FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     * 
     *  results in a Map<String,String> (Note: this property has no value, only a group of parameters, so there is no element with the ":" key)
     *  Key         Value
     *  FREQ        DAILY                           Frequency parameter
     *  UNTIL       20160417T235959Z                Until parameter
     *  INTERVAL    1                               Interval parameter
     *  
     *  Another example
     *  CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yahoo.com
     *  results in a Map<String,String>
     *  Key         Value
     *  CN          David Bal                       Common Name parameter
     *  SENT-BY     "mailto:ddbal1@yahoo.com"         Sent By parameter (quotes will be stripped in the parameter enum's parsing method)
     *  :           mailto:ddbal1@yahoo.com         ***NOTE the ":" key indicates the property value
     * 
     * @param propertyLine
     * @return
     */
    /** key mapping to property value, instead of parameter value*/
    
//    public static Map<String,String> propertyLineToParameterMap(String propertyLine)
//    {
////        System.out.println("propertyline:" + propertyLine);
//        return Arrays.stream(propertyLine.split(";"))
////                .peek(System.out::println)
//                .collect(Collectors.toMap(
//                    p -> 
//                    {
//                        if (p.contains("="))
//                        {
//                            return p.substring(0, p.indexOf('=')); // parameter key
//                        } else
//                        {
//                            return PROPERTY_VALUE_KEY; // indicates property value key
//                        }
//                    },
//                    p ->
//                    {
//                        if (p.contains("="))
//                        {
//                            return p.substring(p.indexOf('=')+1); // parameter value
//                        } else
//                        {
//                            return p; // property value
//                        }
//                    }));
//    }
    
    /**
     * Converts property line into a property-parameter/value map
     * For example:
     * RDATE;VALUE=DATE:19970304,19970504,19970704,19970904
     * 
     *  results in a Map<String,String> (Note: this property has no value, only a group of parameters, so there is no element with the ":" key)
     *  Key         Value
     *  RDATE       19970304,19970504,19970704,19970904
     *  VALUE       DATE
     *  
     *  Another example
     *  ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yahoo.com
     *  results in a Map<String,String>
     *  Key         Value
     *  CN          David Bal                       Common Name parameter
     *  SENT-BY     "mailto:ddbal1@yahoo.com"         Sent By parameter (quotes will be stripped in the parameter enum's parsing method)
     *  ORGANIZER   mailto:ddbal1@yahoo.com
     * 
     * STATUS:CONFIRMED
     * Key         Value
     * STATUS      CONFIRMED
     * 
     * RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     * Key         Value
     * RRULE
     * FREQ        DAILY                           Frequency parameter
     * UNTIL       20160417T235959Z                Until parameter
     * INTERVAL    1                               Interval parameter
     * 
     * @param propertyLine
     * @return
     */
    @Deprecated
    public static Map<String,String> propertyLineToParameterMapOld(String propertyLine)
    {
       System.out.println("propertyline:" + propertyLine);
        
//        Arrays.stream(propertyLine.split(";|(.*=.*(\"mailto:)?.*:)")).forEach(System.out::println);
//        Arrays.stream(propertyLine.split("(;)")).forEach(System.out::println);

        Map<String,String> parameterMap = new LinkedHashMap<>();

        // Extract property name and its value, if any
        int parameterValueStart = 0;
        for (int i=0; i<propertyLine.length(); i++)
        {
            if ((propertyLine.charAt(i) == ';') || (propertyLine.charAt(i) == ':'))
            {
                parameterValueStart = i+1;
                break;
            }
        }
        if (parameterValueStart == 0)
        {
            return null; // line doesn't contain a property, skip
        }
        String propertyName = propertyLine.substring(0, parameterValueStart-1);
        
//        Arrays.stream(propertyLine.split("(mailto)(?!.*:)")).forEach(System.out::println);
//        Arrays.stream(propertyLine.split("(\":)|:(?!.*:)")).forEach(System.out::println);
        final String propertyValue;
        Pattern pattern = Pattern.compile(".*\":(.*)"); // match ":
        Matcher m = pattern.matcher(propertyLine);
        if (m.find())
        {
            propertyValue = m.group(1);
        } else
        {
            Pattern pattern2 = Pattern.compile(":(?!.*=)(.*)"); // match last :
            Matcher m2 = pattern2.matcher(propertyLine);
            if (m2.find())
            {
                propertyValue = m2.group(1);
            } else
            {
                propertyValue = "";
            }
        }
        parameterMap.put(PROPERTY_VALUE_KEY, propertyValue);
//      parameterMap.put(propertyName, propertyValue);
        
        int parameterValueEnd = propertyLine.lastIndexOf(propertyValue);
//        System.out.println("se:" + parameterValueStart + " " + parameterValueEnd);
        if (parameterValueEnd > parameterValueStart)
        {
            String remainingLine = propertyLine.substring(parameterValueStart, parameterValueEnd).replace("\"", "");
          System.out.println("remaining line:" + remainingLine);
//        System.out.println("propertyValue=" + propertyValue);
        
//        List<String> animals = new ArrayList<String>();
//        while (m.find()) {
//            System.out.println("Found a " + m.group(1));
//            animals.add(m.group());
//        }

            parameterMap.putAll(Arrays.stream(remainingLine.split(";"))
                    .peek(System.out::println)
                    .collect(Collectors.toMap(
                        p -> p.substring(0, p.indexOf('=')), // parameter key
                        p -> p.substring(p.indexOf('=')+1)) // parameter value
                        ));
        }
        return parameterMap;
    }

    /**
     * Converts property-parameter/value map into property line.  This is the opposite operation
     * of {@link #propertyLineToParameterMap(String)}
     * 
     * @param parameterMap
     * @return
     */
    @Deprecated // May use enum makeLine methods instead
    // TODO - NEED LINE WRAPPING
    public static String parameterMapToPropertyLine(Map<String,String> parameterMap)
    {
        return parameterMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(";"));
    }
    
//    @Deprecated
//    public static List<Pair<String,String>> componentStringToPropertyNameAndValueListNoUnwrap(String string)
//    {
//        return Arrays
//                .stream(string.split(System.lineSeparator()))
//                .map(line -> {
//                    return parsePropertyLine(line);
//                })
//                .filter(p -> p != null)
//                .sorted(DTSTART_FIRST_COMPARATOR_PAIR)
//                .collect(Collectors.toList());        
//    }

    /**
     * Splits a content line into its property name/value pair
     * For example:
     * RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO
     * key = RRULE
     * value = FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO
     * 
     * DTSTART;TZID=America/Los_Angeles:19970512T090000
     * key = DTSTART
     * value = TZID=America/Los_Angeles:19970512T090000
     * 
     * @param line - single content line from calendar
     * @return - pair where key is property name and value is its value as a string
     */
    @Deprecated // going to split line into map in propertyLineToParameterMap - first element will be property name/value
    public static Pair<String, String> parsePropertyLine(String line)
    {
        int propertyValueSeparatorIndex = 0;
        for (int i=0; i<line.length(); i++)
        {
            if ((line.charAt(i) == ';') || (line.charAt(i) == ':'))
            {
                propertyValueSeparatorIndex = i;
                break;
            }
        }
        if (propertyValueSeparatorIndex == 0)
        {
            return null; // line doesn't contain a property, skip
        }
        String propertyName = line.substring(0, propertyValueSeparatorIndex);
        String value = line.substring(propertyValueSeparatorIndex + 1).trim();
        if (value.isEmpty())
        { // skip empty properties
            return null;
        }
        return new Pair<String,String>(propertyName, value);
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
                // SEARCH FOR BEGIN OF COMPONENT - LIKE PARSE IN VCALENDAR
                // GET LINES UNTIL END
                // MAKE TASK TO PARSE COMPONENT
                // WHAT ABOUT CALENDAR PROPERTIES?
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

    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    // will be obsolete with Java 9
    static <T> Spliterator<T> takeWhile(
            Spliterator<T> splitr, Predicate<? super T> predicate) {
          return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
            boolean stillGoing = true;
            @Override public boolean tryAdvance(Consumer<? super T> consumer) {
              if (stillGoing) {
                boolean hadNext = splitr.tryAdvance(elem -> {
                  if (predicate.test(elem)) {
                    consumer.accept(elem);
                  } else {
                    stillGoing = false;
                  }
                });
                return hadNext && stillGoing;
              }
              return false;
            }
          };
        }

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
       return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
}
