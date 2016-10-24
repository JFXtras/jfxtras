package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;

/**
 * CN
 * Common Name
 * RFC 5545, 3.2.2, page 15
 * 
 * To specify the common name to be associated with the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
 * 
 * @author David Bal
 * @see Attendee
 * @see Organizer
 */
public class CommonName extends ParameterBase<CommonName, String>
{
    public CommonName()
    {
        super();
    }

    public CommonName(CommonName source)
    {
        super(source);
    }

    public static CommonName parse(String content)
    {
        CommonName parameter = new CommonName();
        parameter.parseContent(content);
        return parameter;

    }
}
