package jfxtras.icalendarfx.properties.component.descriptive;

import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseAltText;
import jfxtras.icalendarfx.properties.ValueType;

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
public class Resources extends PropBaseAltText<ObservableList<String>, Resources>
{
    private final static StringConverter<ObservableList<String>> CONVERTER = new StringConverter<ObservableList<String>>()
    {
        @Override
        public String toString(ObservableList<String> object)
        {
            return object.stream()
                    .map(v -> ValueType.TEXT.getConverter().toString(v)) // escape special characters
                    .collect(Collectors.joining(","));
        }

        @Override
        public ObservableList<String> fromString(String string)
        {
            return FXCollections.observableArrayList(Arrays.stream(string.replace("\\,", "~~").split(",")) // change comma escape sequence to avoid splitting by it
                    .map(s -> s.replace("~~", "\\,"))
                    .map(v -> (String) ValueType.TEXT.getConverter().fromString(v)) // unescape special characters
                    .collect(Collectors.toList()));
        }
    };

    public Resources(ObservableList<String> values)
    {
        this();
        setValue(values);
    }
    
    /** Constructor with varargs of property values 
     * Note: Do not use to parse the content line.  Use static parse method instead.*/
    public Resources(String...values)
    {
        this();
        setValue(FXCollections.observableArrayList(values));
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
    
    // set one category
    public void setValue(String category)
    {
        setValue(FXCollections.observableArrayList(category));
    }

    public static Resources parse(String propertyContent)
    {
        Resources property = new Resources();
        property.parseContent(propertyContent);
        return property;
    }
    
    @Override
    protected ObservableList<String> copyValue(ObservableList<String> source)
    {
        return FXCollections.observableArrayList(source);
    }
}
