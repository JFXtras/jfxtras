package jfxtras.icalendarfx.utilities;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.StringConverter;

public class StringConverters
{
    public static StringConverter<String> defaultStringConverterWithQuotes()
    {
        return new StringConverter<String>()
        {
            @Override
            public String toString(String object)
            {
                return addDoubleQuotesIfNecessary(object);
            }

            @Override
            public String fromString(String string)
            {
                return StringConverters.removeDoubleQuote(string);
            }
        };
    }
    
    public static StringConverter<URI> uriConverterNoQuotes()
    {
        return new StringConverter<URI>()
        {
            @Override
            public String toString(URI object)
            {
                return object.toString();
            }

            @Override
            public URI fromString(String string)
            {
                try
                {
                    return new URI(string);
                } catch (URISyntaxException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
    
    public static StringConverter<URI> uriConverterWithQuotes()
    {
        return new StringConverter<URI>()
        {
            @Override
            public String toString(URI object)
            {
                return addDoubleQuotesIfNecessary(object.toString());
            }

            @Override
            public URI fromString(String string)
            {
                try
                {
                    return new URI(removeDoubleQuote(string));
                } catch (URISyntaxException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
    
    public static StringConverter<List<URI>> uriListConverter()
    {
        return new StringConverter<List<URI>>()
        {
            @Override
            public String toString(List<URI> object)
            {
                return object.stream()
                        .map(u -> addDoubleQuotesIfNecessary(u.toString()))
                        .collect(Collectors.joining(","));
            }

            @Override
            public List<URI> fromString(String string)
            {
                List<URI> uriList = new ArrayList<>();
                Iterator<String> i = Arrays.stream(string.split(",")).iterator();
                while (i.hasNext())
                {
                    String item = i.next();
                    URI uri = null;
                    try
                    {
                        uri = new URI(removeDoubleQuote(item));
                    } catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                    uriList.add(uri);
                }
                return uriList;
            }
        };
    }
    
    public static StringConverter<Boolean> booleanConverter()
    {
        return new StringConverter<Boolean>()
        {
            @Override
            public String toString(Boolean object)
            {
                return object.toString().toUpperCase();
            }

            @Override
            public Boolean fromString(String string)
            {
                 return Boolean.parseBoolean(string);            
            }
        };
    }
    
    /**
     * Remove leading and trailing double quotes
     * 
     * @param input - string with or without double quotes at front and end
     * @return - string stripped of leading and trailing double quotes
     */
    public static String removeDoubleQuote(String input)
    {
        final char quote = '\"';
        StringBuilder builder = new StringBuilder(input);
        if (builder.charAt(0) == quote)
        {
            builder.deleteCharAt(0);
        }
        if (builder.charAt(builder.length()-1) == quote)
        {
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }
    
    /**
     * Add Double Quotes to front and end of string if text contains \ : ;
     * 
     * @param text
     * @return
     */
    static String addDoubleQuotesIfNecessary(String text)
    {
        boolean hasDQuote = text.contains("\"");
        boolean hasColon = text.contains(":");
        boolean hasSemiColon = text.contains(";");
        if (hasDQuote || hasColon || hasSemiColon)
        {
            return "\"" + text + "\""; // add double quotes
        } else
        {
            return text;
        }
    }
}
