package jfxtras.icalendarfx.parameters;

import java.net.URI;

import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.relationship.Organizer;

/**
 * DIR
 * Directory Entry Reference
 * RFC 5545, 3.2.6, page 18
 * 
 * To specify reference to a directory entry associated with
 *     the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;DIR="ldap://example.com:6666/o=ABC%20Industries,
 *  c=US???(cn=Jim%20Dolittle)":mailto:jimdo@example.com
 * 
 * @author David Bal
 * @see Attendee
 * @see Organizer
 *
 */
public class DirectoryEntry extends ParameterBase<DirectoryEntry, URI>
{
    public DirectoryEntry(URI uri)
    {
        super(uri);
    }
    
    public DirectoryEntry()
    {
        super();
    }

    public DirectoryEntry(DirectoryEntry source)
    {
        super(source);
    }

    public static DirectoryEntry parse(String content)
    {
        DirectoryEntry parameter = new DirectoryEntry();
        parameter.parseContent(content);
        return parameter;
    }
}
