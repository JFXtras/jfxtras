package jfxtras.icalendarfx.properties.component.relationship;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * UID
 * Unique Identifier
 * RFC 5545, 3.8.4.7, page 117
 * 
 * This property defines the persistent, globally unique identifier for the calendar component.
 *
 * The "UID" itself MUST be a globally unique identifier.
 * The generator of the identifier MUST guarantee that the identifier
 * is unique.  There are several algorithms that can be used to
 * accomplish this.  A good method to assure uniqueness is to put the
 * domain name or a domain literal IP address of the host on which
 * the identifier was created on the right-hand side of an "@", and
 * on the left-hand side, put a combination of the current calendar
 * date and time of day (i.e., formatted in as a DATE-TIME value)
 * along with some other currently unique (perhaps sequential)
 * identifier available on the system (for example, a process id
 * number).  Using a DATE-TIME value on the left-hand side and a
 * domain name or domain literal on the right-hand side makes it
 * possible to guarantee uniqueness since no two hosts should be
 * using the same domain name or IP address at the same time.  Though
 * other algorithms will work, it is RECOMMENDED that the right-hand
 * side contain some domain identifier (either of the host itself or
 * otherwise) such that the generator of the message identifier can
 * guarantee the uniqueness of the left-hand side within the scope of
 * that domain.
 *
 * Examples:
 * UID:19960401T080045Z-4000F192713-0052@example.com
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public class UniqueIdentifier extends PropertyBase<String, UniqueIdentifier>
{
    public UniqueIdentifier(UniqueIdentifier source)
    {
        super(source);
    }
    
    public UniqueIdentifier()
    {
        super();
    }

    public static UniqueIdentifier parse(String propertyContent)
    {
        UniqueIdentifier property = new UniqueIdentifier();
        property.parseContent(propertyContent);
        return property;
    }
}
