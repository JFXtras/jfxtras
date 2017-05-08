package jfxtras.icalendarfx.parameters;

import java.net.URI;
import java.util.List;

import jfxtras.icalendarfx.parameters.Delegatees;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * DELEGATED-TO
 * Delegatees
 * RFC 5545, 3.2.5, page 17
 * 
 * To specify the calendar users to whom the calendar user
 *    specified by the property has delegated participation.
 * 
 * Example:
 * ATTENDEE;DELEGATED-TO="mailto:jdoe@example.com","mailto:jqpublic
 *  @example.com":mailto:jsmith@example.com
 * 
 * @author David Bal
 */
public class Delegatees extends VParameterBase<Delegatees, List<URI>>
{
	private static final StringConverter< List<URI>> CONVERTER = StringConverters.uriListConverter();

    public Delegatees(List<URI> list)
    {
        super(list, CONVERTER);
    }
    
    public Delegatees(Delegatees source)
    {
        super(source, CONVERTER);
    }

    public Delegatees()
    {
        super(CONVERTER);
    }
    
    public static Delegatees parse(String content)
    {
    	return Delegatees.parse(new Delegatees(), content);
    }
}
