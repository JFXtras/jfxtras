/**
 * Copyright (c) 2011-2021, JFXtras
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
package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * <h2> 3.2.14.  Alarm Trigger Relationship</h2>

   <p>Parameter Name:  RELATED</p>

   <p>Purpose:  To specify the relationship of the alarm trigger with
      respect to the start or end of the calendar component.</p>

   <p>Description:  This parameter can be specified on properties that
      specify an alarm trigger with a "DURATION" value type.  The
      parameter specifies whether the alarm will trigger relative to the
      start or end of the calendar component.  The parameter value START
      will set the alarm to trigger off the start of the calendar
      component; the parameter value END will set the alarm to trigger
      off the end of the calendar component.  If the parameter is not
      specified on an allowable property, then the default is START.</p>

  <p>Format Definition:  This property parameter is defined by the following notation::
  <ul>
  <li>triggerparam
    <ul>
    <li>"RELATED" "="
    <li>("START"       ; Trigger off of start
    <li>/ "END")        ; Trigger off of end
    </ul>
  </ul>
  <p>Example:
  <ul>
  <li>TRIGGER;RELATED=END:PT5M
  </ul>
  </p>
  RFC 5545                       iCalendar                  September 2009
 * 
 * @author David Bal
 *
 */
public class AlarmTriggerRelationship extends VParameterBase<AlarmTriggerRelationship, AlarmTriggerRelationshipType>
{
	private static final StringConverter<AlarmTriggerRelationshipType> CONVERTER = new StringConverter<AlarmTriggerRelationshipType>()
    {
        @Override
        public String toString(AlarmTriggerRelationshipType object)
        {
            return object.toString();
        }

        @Override
        public AlarmTriggerRelationshipType fromString(String string)
        {
            return AlarmTriggerRelationshipType.valueOf(string.toUpperCase());
        }
    };
    
    /** Create default AlarmTriggerRelationship with property value set to START */
    public AlarmTriggerRelationship()
    {
        super(AlarmTriggerRelationshipType.START, CONVERTER);
    }

    /** Create new AlarmTriggerRelationshipType with property value set to input parameter */
    public AlarmTriggerRelationship(AlarmTriggerRelationshipType value)
    {
        super(value, CONVERTER);
    }

    /** Create deep copy of source AlarmTriggerRelationship */
    public AlarmTriggerRelationship(AlarmTriggerRelationship source)
    {
        super(source, CONVERTER);
    }
    
    /** Property value types for AlarmTriggerRelationship */
    public enum AlarmTriggerRelationshipType
    {
        START,
        END;
    }
    
    public static AlarmTriggerRelationship parse(String content)
    {
    	return AlarmTriggerRelationship.parse(new AlarmTriggerRelationship(), content);
    }
}
