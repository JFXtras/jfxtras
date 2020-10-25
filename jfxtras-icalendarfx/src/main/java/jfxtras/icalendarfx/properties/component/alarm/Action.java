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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.alarm;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.alarm.Action.ActionType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * <h1>ACTION</h1>
 * <p>RFC 5545, 3.8.6.1, page 132</p>
 * 
 * <p>This property defines the action to be invoked when an alarm is triggered.</p>
 * 
 * <p>actionvalue = "AUDIO" / "DISPLAY" / "EMAIL" / iana-token / x-name</p>
 * 
 * <p>Applications MUST ignore alarms with x-name and iana-token values they don't recognize.</p>
 * 
 * <p>Examples:
 * <ul>
 * <li>ACTION:AUDIO
 * <li>ACTION:DISPLAY
 * </ul>
 * </p>
 * 
 * @author David Bal
 * 
 * @see VAlarm
 */
public class Action extends VPropertyBase<ActionType, Action>
{
//	private static final Collection<ValueType> ALLOWED_VALUE_TYPES =  Arrays.asList(ValueType.TEXT);
//	private static final ValueType DEFAULT_VALUE_TYPE =  ValueType.TEXT;

    private final static StringConverter<ActionType> CONVERTER = new StringConverter<ActionType>()
    {
        @Override
        public String toString(ActionType object)
        {
            // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
            return (object == ActionType.UNKNOWN) ? null: object.toString();
        }

        @Override
        public ActionType fromString(String string)
        {
            return ActionType.valueOf2(string.toUpperCase());
        }
    };
    
    public Action(ActionType type)
    {
//        super(ALLOWED_VALUE_TYPES, DEFAULT_VALUE_TYPE);
        super();
        setConverter(CONVERTER);
        setValue(type);
    }
    
    public Action(Action source)
    {
        super(source);
    }

    public Action()
    {
//      super(ALLOWED_VALUE_TYPES, DEFAULT_VALUE_TYPE);
      super();
      setConverter(CONVERTER);
    }
    
    public static Action parse(String content)
    {
    	return Action.parse(new Action(), content);
    }
    
    public enum ActionType
    {
        AUDIO,
        DISPLAY,
        EMAIL,
        UNKNOWN; // must ignore
        
        static ActionType valueOf2(String value)
        {
            try
            {
                return valueOf(value);
            } catch (IllegalArgumentException e)
            {
                return UNKNOWN;
            }
        }
    }
}