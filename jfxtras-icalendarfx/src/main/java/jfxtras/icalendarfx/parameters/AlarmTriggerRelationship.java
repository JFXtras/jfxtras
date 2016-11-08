package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;

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
  <li>trigrelparam
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
public class AlarmTriggerRelationship extends ParameterBase<AlarmTriggerRelationship, AlarmTriggerRelationshipType>
{
    /** Create default AlarmTriggerRelationship with property value set to START */
    public AlarmTriggerRelationship()
    {
        super(AlarmTriggerRelationshipType.START);
    }

    /** Create new AlarmTriggerRelationshipType with property value set to input parameter */
    public AlarmTriggerRelationship(AlarmTriggerRelationshipType value)
    {
        super(value);
    }

    /** Create deep copy of source AlarmTriggerRelationship */
    public AlarmTriggerRelationship(AlarmTriggerRelationship source)
    {
        super(source);
    }
    
    /** Property value types for AlarmTriggerRelationship */
    public enum AlarmTriggerRelationshipType
    {
        START,
        END;
    }

    /** Create new AlarmTriggerRelationship by parsing unfolded calendar content */
    public static AlarmTriggerRelationship parse(String unfoldedContent)
    {
        AlarmTriggerRelationship parameter = new AlarmTriggerRelationship();
        parameter.parseContent(unfoldedContent);
        return parameter;
    }
}
