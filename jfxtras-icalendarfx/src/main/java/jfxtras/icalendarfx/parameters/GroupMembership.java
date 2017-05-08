package jfxtras.icalendarfx.parameters;

import java.net.URI;
import java.util.List;

import jfxtras.icalendarfx.parameters.GroupMembership;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * MEMBER
 * Group or List Membership
 * RFC 5545, 3.2.11, page 21
 * 
 * To specify the group or list membership of the calendar user specified by the property.
 * 
 * Example:
 * ATTENDEE;MEMBER="mailto:projectA@example.com","mailto:pr
 *  ojectB@example.com":mailto:janedoe@example.com
 * 
 * @author David Bal
 *
 */
public class GroupMembership extends VParameterBase<GroupMembership, List<URI>>
{
	private static final StringConverter< List<URI>> CONVERTER = StringConverters.uriListConverter();

    public GroupMembership(List<URI> values)
    {
        super(values, CONVERTER);
    }
    
    public GroupMembership()
    {
        super(CONVERTER);
    }
    
    public GroupMembership(GroupMembership source)
    {
        super(source, CONVERTER);
    }
    
    public static GroupMembership parse(String content)
    {
    	return GroupMembership.parse(new GroupMembership(), content);
    }
}
