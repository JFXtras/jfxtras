package jfxtras.icalendarfx.properties.component.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseAltText;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * RESOURCES
 * RFC 5545 iCalendar 3.8.1.10. page 91
 * 
 * This property defines the equipment or resources
 * anticipated for an activity specified by a calendar component.
 * 
 * Examples:
 * RESOURCES:EASEL,PROJECTOR,VCR
 * RESOURCES;LANGUAGE=fr:Nettoyeur haute pression
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 */
public class Resources extends PropBaseAltText<List<String>, Resources>
{
    private final static StringConverter<List<String>> CONVERTER = new StringConverter<List<String>>()
    {
        @Override
        public String toString(List<String> object)
        {
            return object.stream()
                    .map(v -> ValueType.TEXT.getConverter().toString(v)) // escape special characters
                    .collect(Collectors.joining(","));
        }

        @Override
        public List<String> fromString(String string)
        {
            return new ArrayList<>(Arrays.stream(string.replace("\\,", "~~").split(",")) // change comma escape sequence to avoid splitting by it
                    .map(s -> s.replace("~~", "\\,"))
                    .map(v -> (String) ValueType.TEXT.getConverter().fromString(v)) // unescape special characters
                    .collect(Collectors.toList()));
        }
    };

    public Resources(List<String> values)
    {
        this();
        setValue(values);
    }
    
    /** Constructor with varargs of property values 
     * Note: Do not use to parse the content line.  Use static parse method instead.*/
    public Resources(String...values)
    {
        this();
        setValue(new ArrayList<>(Arrays.asList(values)));
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    public Resources()
    {
        super();
        setConverter(CONVERTER);
    }

    public static Resources parse(String content)
    {
    	return Resources.parse(new Resources(), content);
    }
    
    @Override
    protected List<String> copyValue(List<String> source)
    {
        return new ArrayList<String>(source);
    }
}
