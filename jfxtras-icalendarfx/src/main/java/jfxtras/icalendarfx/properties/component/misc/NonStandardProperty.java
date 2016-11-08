package jfxtras.icalendarfx.properties.component.misc;

import java.util.List;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.parameters.NonStandardParameter;

/**
   <h2>3.8.8.2.  Non-Standard Properties</h2>

   <p>Property Name:  Any property name with a "X-" prefix</p>

   <p>Purpose:  This class of property provides a framework for defining
      non-standard properties.</p>

   <p>Value Type:  The default value type is TEXT.  The value type can be
      set to any value type.</p>

   <p>Property Parameters:  IANA, {@link NonStandardParameter non-standard},
      {@link Language language} parameters can be specified on this property.
      Note: Contradicting above RFC 5545 text, based on examples (even one
      below), it seems that this property can contain all parameters - David Bal.</p>

   <p>Conformance:  This property can be specified in any calendar
      component ({@link VEvent}, {@link VTodo}, {@link VJournal}. {@link VAlarm},
      {@link VTimeZone}, {@link DaylightSavingTime}, {@link StandardTime})</p>

   <p>Description:  The MIME Calendaring and Scheduling Content Type
      provides a "standard mechanism for doing non-standard things".
      This extension support is provided for implementers to "push the
      envelope" on the existing version of the memo.  Extension
      properties are specified by property and/or property parameter
      names that have the prefix text of "X-" (the two-character
      sequence: LATIN CAPITAL LETTER X character followed by the HYPHEN-
      MINUS character).  It is recommended that vendors concatenate onto
      this sentinel another short prefix text to identify the vendor.
      This will facilitate readability of the extensions and minimize
      possible collision of names between different vendors.  User
      agents that support this content type are expected to be able to
      parse the extension properties and property parameters but can
      ignore them.</p>

      <p>At present, there is no registration authority for names of
      extension properties and property parameters.  The value type for
      this property is TEXT.  Optionally, the value type can be any of
      the other valid value types.</p>

   <p>Format Definition:  This property is defined by the following
      notation:</p>
  <ul>
  <li>x-prop
    <ul>
    <li>x-name *(";" icalparameter) ":" value CRLF
    </ul>
  </ul>
  </p>
  
  <p>Example:  The following is an example of this property:
  <ul>
  <li>X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.<br>
        org/mysubj.au
  </ul>
  </p>
  <h2>RFC 5545                       iCalendar                  September 2009</h2>

 * @author David Bal
 */
public class NonStandardProperty extends UnknownProperty<Object, NonStandardProperty>
{
    /** Create NonStandardProperty with property value set to the input parameter */
    public NonStandardProperty(Object value)
    {
        super(value);
    }

    /** Create deep copy of source NonStandardProperty */
    public NonStandardProperty(NonStandardProperty source)
    {
        super(source);
    }
    
    /** Create default NonStandardProperty with no value set */
    public NonStandardProperty()
    {
        super();
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (name() != null && ! name().substring(0, 2).equals("X-"))
        {
            errors.add(name() + " is not a proper non-standard property name.  It must begin with X-");
        }
        return errors;
    }
    
    /** Create new Description by parsing unfolded calendar content */
    public static NonStandardProperty parse(String unfoldedContent)
    {
        NonStandardProperty property = new NonStandardProperty();
        property.parseContent(unfoldedContent);
        return property;
    }
}
