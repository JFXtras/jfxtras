package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.properties.PropBaseAltText;

/**
<h2>3.8.1.12.  Summary</h2>

   <p>Property Name:  SUMMARY</p>

   <p>Purpose:  This property defines a short summary or subject for the
      calendar component.</p>

   <p>Value Type:  TEXT</p>

   <p>Property Parameters:  IANA, {@link NonStandardParameter non-standard},
      {@link AlternateText alternate text representation}, and {@link Language language property}
      parameters can be specified on this property.</p>

   <p>Conformance:  The property can be specified in {@link VEvent VEVENT}, {@link VTodo VTODO},
    {@link VJournal VJOURNAL}, or {@link VAlarm VALARM} calendar components.</p>

   <p>Description:  This property is used in the {@link VEvent VEVENT}, {@link VTodo VTODO} AND
    {@link VJournal VJOURNAL} calendar components to capture a short, one-line
      summary about the activity or journal entry.</p>

      <p>This property is used in the {@link VAlarm VALARM} calendar component to
      capture the subject of an EMAIL category of alarm.</p>

  <p>Format Definition:  This property is defined by the following notation:
  <ul>
  <li>summary
    <ul>
    <li>"SUMMARY" summparam ":" text CRLF
    </ul>
  <li>summparam
    <ul>
    <li>The following are OPTIONAL, but MUST NOT occur more than once.
      <ul>
      <li>";" {@link AlternateText Alternate text representation}
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
  </ul>
  
  <p>Example:  The following is an example of this property:
  <ul>
  <li>SUMMARY:Department Party
  </ul>
  </p>
  RFC 5545                       iCalendar                  September 2009
 */
public class Summary extends PropBaseAltText<String, Summary>
{
    /** Create deep copy of source Summary */
    public Summary(Summary source)
    {
        super(source);
    }
    
    /** Create Summary with property value set to parameter value*/
    public Summary(String value)
    {
        super();
        setValue(value);
    }

    /** Create default Summary with no value set */
    public Summary()
    {
        super();
    }
    
    /** Create new Summary by parsing unfolded calendar content */
    public static Summary parse(String unfoldedContent)
    {
        Summary property = new Summary();
        property.parseContent(unfoldedContent);
        return property;
    }
}
