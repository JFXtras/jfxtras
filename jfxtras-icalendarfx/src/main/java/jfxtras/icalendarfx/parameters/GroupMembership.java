/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
