package jfxtras.icalendarfx.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jfxtras.icalendarfx.utilities.Pair;

/**
 * Static utility methods used throughout iCalendar
 * 
 * @author David Bal
 *
 */
public final class ICalendarUtilities
{
    private ICalendarUtilities() { };
    
    public final static String PROPERTY_VALUE_KEY = ":";
   
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
    public static List<Pair<String,String>> parseInlineElementsToListPair(String propertyLine)
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
           parameters.add(new Pair<>(PROPERTY_VALUE_KEY, null));
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
     * Returns index where property name ends - after first ';' or ':'
     */
    @Deprecated
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
