/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.parameters.Language;
import jfxtras.icalendarfx.parameters.NonStandardParameter;
import jfxtras.icalendarfx.properties.PropBaseAltText;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;

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
    public static Summary parse(String content)
    {
    	return Summary.parse(new Summary(), content);
    }
}
