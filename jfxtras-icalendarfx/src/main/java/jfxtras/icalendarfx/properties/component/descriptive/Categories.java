package jfxtras.icalendarfx.properties.component.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.properties.PropBaseLanguage;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 <h2>3.8.1.2.  Categories</h2>

   <p>Property Name:  CATEGORIES

   <p>Purpose:  This property defines the categories for a calendar
      component.

   <p>Value Type:  TEXT

   <p>Property Parameters:  IANA, {@link NonStandardParameter non-standard}, and {@link Language language property}
      parameters can be specified on this property.

   <p>Conformance:  The property can be specified within {@link VEvent VEVENT}, {@link VTodo VTODO},
      or {@link VJournal VJOURNAL} calendar components.

   <p>Description:  This property is used to specify categories or subtypes
      of the calendar component.  The categories are useful in searching
      for a calendar component of a particular type and category.
      Within the {@link VEvent VEVENT}, {@link VTodo VTODO}, or {@link VJournal VJOURNAL} calendar components,
      more than one category can be specified as a COMMA-separated list
      of categories.
      
  <p>Format Definition:  This property is defined by the following notation:
  <ul>
  <li>categories
    <ul>
    <li>"CATEGORIES" catparam ":" text *("," text) CRLF
    </ul>
  <li>catparam
    <ul>
    <li>The following are OPTIONAL, but MUST NOT occur more than once.
      <ul>
      <li>";" {@link Language Language for text}
      </ul>
    <li>The following are OPTIONAL, and MAY occur more than once.
    <ul>
      <li>other-param
        <ul>
        <li>";" {@link NonStandardParameter}
        <li>";" {@link IANAParameter}
        </ul>
    </ul>
  </ul>
  <p>Example:  The following is an example of this property with formatted
      line breaks in the property value:
  <ul>
  <li>CATEGORIES:APPOINTMENT,EDUCATION
  <li>CATEGORIES:MEETING
  </ul>
  </p>
 */
public class Categories extends PropBaseLanguage<List<String>, Categories>
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
    
    public Categories(List<String> values)
    {
        this();
        setValue(values);
    }
    
    /** Constructor with varargs of property values 
     * Note: Do not use to parse the content line.  Use static parse method instead.*/
    public Categories(String...values)
    {
        this();
        setValue(new ArrayList<>(Arrays.asList(values)));
    }
    
    public Categories(Categories source)
    {
        super(source);
    }
    
    public Categories()
    {
        super();
        setConverter(CONVERTER);
    }

    public static Categories parse(String content)
    {
    	return Categories.parse(new Categories(), content);
    }
    
    @Override
    protected List<String> copyValue(List<String> source)
    {
        return new ArrayList<>(source);
    }
}
