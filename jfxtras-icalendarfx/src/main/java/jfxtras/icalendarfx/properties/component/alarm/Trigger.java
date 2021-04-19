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
package jfxtras.icalendarfx.properties.component.alarm;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.properties.PropAlarmTrigger;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.ValueType;

/**
 * TRIGGER
 * RFC 5545, 3.8.6.3, page 133
 * 
 * This property specifies when an alarm will trigger.
 * 
 * Value defaults to DURATION, but can also be DATE-TIME.  Only UTC-formatted
 * DATE-TIME is valid.
 * 
 * Example:  A trigger set 15 minutes prior to the start of the event or to-do.
 * TRIGGER:-PT15M
 * A trigger set five minutes after the end of an event or the due date of a to-do.
 * TRIGGER;RELATED=END:PT5M
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VAlarm
 */
public class Trigger<T> extends VPropertyBase<T, Trigger<T>> implements PropAlarmTrigger<T>
{
    /**
    * RELATED: Alarm Trigger Relationship
    * RFC 5545, 3.2.14, page 24
    * To specify the relationship of the alarm trigger with
    * respect to the start or end of the calendar component.
    */
   @Override
   public AlarmTriggerRelationship getAlarmTrigger() { return relationship; }
   private AlarmTriggerRelationship relationship;
   @Override
   public void setAlarmTrigger(AlarmTriggerRelationship relationship)
   {
       if (relationship != null)
       {
           ValueType valueType = (getValueType() == null) ? defaultValueType : getValueType().getValue();
           if (valueType == ValueType.DURATION)
           {
        	   orderChild(relationship);
               this.relationship = relationship;
           } else
           {
               throw new IllegalArgumentException("Alarm Trigger Relationship can only be set if value type is DURATION");
           }
       }
   }
   public Trigger<T> withAlarmTrigger(AlarmTriggerRelationship format) { setAlarmTrigger(format); return this; }
    
    public Trigger(Trigger<T> source)
    {
        super(source);
    }
    
    public Trigger(T value)
    {
        super(value);
    }
    
    public Trigger()
    {
        super();
    }
    
    @Override
    public void setValue(T value)
    {
        if (value instanceof ZonedDateTime)
        {
            ZoneId zone = ((ZonedDateTime) value).getZone();
            if (! zone.equals(ZoneId.of("Z")))
            {
                throw new DateTimeException("Unsupported ZoneId:" + zone + " only Z supported");
            }
            setValueType(ValueType.DATE_TIME); // override default value type
        }
        super.setValue(value);
    }

    @Override
    public void setValueType(ValueParameter valueType)
    {
        if ((valueType.getValue() == ValueType.DATE_TIME) && (getAlarmTrigger() != null))
        {
            throw new IllegalArgumentException("Value type can only be set to DATE-TIME if Alarm Trigger Relationship is null");
        }
        super.setValueType(valueType);
    }
    
    @Override
    protected void setConverterByClass(Class<T> clazz)
    {
        if (TemporalAmount.class.isAssignableFrom(clazz))
        {
            setConverter(ValueType.DURATION.getConverter());
        } else if (clazz.equals(ZonedDateTime.class))
        {
            setConverter(ValueType.DATE_TIME.getConverter());           
        } else
        {
            throw new IllegalArgumentException("Only parameterized types of Duration, Period and ZonedDateTime are supported.");           
        }
    }
    
    @Override
    public boolean isValid()
    {
        boolean isDateTimeValue = (getValueType() == null) ? false : getValueType().getValue() != ValueType.DATE_TIME;
        if (isDateTimeValue)
        {
            // The "RELATED" property parameter is not valid if the value type of the property is set to DATE-TIME
            if (getAlarmTrigger() != null)
            {
                return false;
            }
        }
        return true && super.isValid();
    }
    
    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static <U> Trigger<U> parse(String content)
    {
        return Trigger.parse(new Trigger<U>(), content);
    }
    
    /** Parse string with Temporal class explicitly provided as parameter */
    public static <U> Trigger<U> parse(Class<U> clazz, String value)
    {
        Trigger<U> property = new Trigger<U>();
        property.setConverterByClass(clazz);
        List<Message> m = property.parseContent(value);
        if (! m.isEmpty()) throw new DateTimeException("Invalid value:" + value);
        return property;
    }
}
