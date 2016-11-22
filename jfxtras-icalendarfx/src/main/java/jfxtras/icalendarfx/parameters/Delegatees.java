package jfxtras.icalendarfx.parameters;

import java.net.URI;
import java.util.List;

import jfxtras.icalendarfx.properties.component.relationship.Attendee;

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
 * @see Attendee
 */
public class Delegatees extends ParameterBase<Delegatees, List<URI>>
{
    public Delegatees(List<URI> list)
    {
        super(list);
    }
    
    public Delegatees(Delegatees source)
    {
        super(source);
    }

    public Delegatees()
    {
        super();
    }
    
    public static Delegatees parse(String content)
    {
        Delegatees parameter = new Delegatees();
        parameter.parseContent(content);
        return parameter;
    }
}
