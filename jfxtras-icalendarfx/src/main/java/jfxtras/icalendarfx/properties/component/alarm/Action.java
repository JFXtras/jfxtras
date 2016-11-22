package jfxtras.icalendarfx.properties.component.alarm;

import javafx.util.StringConverter;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.properties.component.alarm.Action.ActionType;

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
public class Action extends PropertyBase<ActionType, Action>
{
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
        super();
        setConverter(CONVERTER);
    }
    
    public static Action parse(String value)
    {
        Action property = new Action();
        property.parseContent(value);
        return property;
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