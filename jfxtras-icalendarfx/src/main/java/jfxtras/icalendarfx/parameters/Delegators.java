package jfxtras.icalendarfx.parameters;

import java.net.URI;
import java.util.List;

import jfxtras.icalendarfx.properties.component.relationship.Attendee;

/**
 * DELEGATED-FROM
 * Delegators
 * RFC 5545, 3.2.4, page 17
 * 
 * To specify the calendar users that have delegated their
 *    participation to the calendar user specified by the property.
 * 
 * Example:
 * ATTENDEE;DELEGATED-FROM="mailto:jsmith@example.com":mailto:
 *  jdoe@example.com
 * 
 * @author David Bal
 * @see Attendee
 */
public class Delegators extends ParameterBase<Delegators, List<URI>>
{
    public Delegators(List<URI> list)
    {
        super(list);
    }
    
    public Delegators()
    {
        super();
    }
    
    public Delegators(Delegators source)
    {
        super(source);
    }

    public static Delegators parse(String content)
    {
        Delegators parameter = new Delegators();
        parameter.parseContent(content);
        return parameter;
    }
}
