/**
 * Copyright (c) 2011-2021, JFXtras
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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.descriptive.PercentComplete;

/**
 * PERCENT-COMPLETE
 * RFC 5545 iCalendar 3.8.1.8. page 88
 * 
 * This property is used by an assignee or delegatee of a
 * to-do to convey the percent completion of a to-do to the "Organizer".
 * The property value is a positive integer between 0 and
 * 100.  A value of "0" indicates the to-do has not yet been started.
 * A value of "100" indicates that the to-do has been completed.
 * 
 * Example:  The following is an example of this property to show 39% completion:
 * PERCENT-COMPLETE:39
 *  
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VTodo
 */
public class PercentComplete extends VPropertyBase<Integer, PercentComplete>
{
    public PercentComplete(PercentComplete source)
    {
        super(source);
    }
    
    public PercentComplete(Integer value)
    {
        super(value);
    }

    public PercentComplete()
    {
        super();
    }
    
    public static PercentComplete parse(String content)
    {
    	return PercentComplete.parse(new PercentComplete(), content);
    }
}
