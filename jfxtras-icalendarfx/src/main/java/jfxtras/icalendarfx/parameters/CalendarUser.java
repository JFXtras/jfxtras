package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.CalendarUser;
import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 <h2>3.2.3.  Calendar User Type</h2>

   <p>Parameter Name:  CUTYPE

   <p>Purpose:  To identify the type of calendar user specified by the
      property.

   <p>Format Definition:  This property parameter is defined by the
      following notation:

       cutypeparam        = "CUTYPE" "="
                          ({@link CalendarUserType#INDIVIDUAL}   ; An individual
                         / "GROUP"        ; A group of individuals
                         / "RESOURCE"     ; A physical resource
                         / "ROOM"         ; A room resource
                         / "UNKNOWN"      ; Otherwise not known
                         / x-name         ; Experimental type
                         / iana-token)    ; Other IANA-registered
                                          ; type
       ; Default is INDIVIDUAL

   <p>Description:  This parameter can be specified on properties with a
      {@link ValueType#CALENDAR_USER_ADDRESS CAL-ADDRESS} value type.  The parameter identifies the type of
      calendar user specified by the property.  If not specified on a
      property that allows this parameter, the default is INDIVIDUAL.
      Applications MUST treat x-name and iana-token values they don't
      recognize the same way as they would the UNKNOWN value.<p>

   Example:

       ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org
 * CUTYPE
 * Calendar User Type
 * RFC 5545, 3.2.3, page 16
 * 
 * To identify the type of calendar user specified by the property.
 * 
 * Example:
 * ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org
 * 
 * @author David Bal
 */
public class CalendarUser extends ParameterEnumBasedWithUnknown<CalendarUser, CalendarUserType>
{
	private static final StringConverter<CalendarUserType> CONVERTER = new StringConverter<CalendarUserType>()
    {
        @Override
        public String toString(CalendarUserType object)
        {
            return object.toString();
        }

        @Override
        public CalendarUserType fromString(String string)
        {
            return CalendarUserType.valueOfWithUnknown(string.toUpperCase());
        }
    };

    public CalendarUser()
    {
        super(CalendarUserType.INDIVIDUAL, CONVERTER); // default value
    }

    public CalendarUser(CalendarUserType type)
    {
        super(type, CONVERTER);
    }

    public CalendarUser(CalendarUser source)
    {
        super(source, CONVERTER);
    }
    
    public enum CalendarUserType
    {
        INDIVIDUAL, // default is INDIVIDUAL
        GROUP,
        RESOURCE,
        ROOM,
        UNKNOWN;
        
        static CalendarUserType valueOfWithUnknown(String value)
        {
            CalendarUserType match;
            try
            {
                match = valueOf(value);
            } catch (Exception e)
            {
                match = UNKNOWN;
            }
            return match;
        }
    }
    
    public static CalendarUser parse(String content)
    {
    	return CalendarUser.parse(new CalendarUser(), content);
    }
}
