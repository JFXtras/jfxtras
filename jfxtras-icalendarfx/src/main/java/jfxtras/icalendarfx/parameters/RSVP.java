package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.RSVP;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * RSVP
 * RSVP Expectation
 * RFC 5545, 3.2.17, page 26
 * 
 * To specify whether there is an expectation of a favor of a
 *  reply from the calendar user specified by the property value.
 * 
 * Example:
 * ATTENDEE;RSVP=TRUE:mailto:jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class RSVP extends VParameterBase<RSVP, Boolean>
{
	private static final StringConverter<Boolean> CONVERTER = StringConverters.booleanConverter();
	
    public RSVP()
    {
        super(false, CONVERTER); // default value
    }

    public RSVP(Boolean value)
    {
        super(value, CONVERTER);
    }

    public RSVP(RSVP source)
    {
        super(source, CONVERTER);
    }
    
    public static RSVP parse(String content)
    {
    	return RSVP.parse(new RSVP(), content);
    }
}
