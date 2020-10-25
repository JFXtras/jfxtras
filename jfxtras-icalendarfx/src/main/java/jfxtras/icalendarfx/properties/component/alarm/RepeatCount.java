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
package jfxtras.icalendarfx.properties.component.alarm;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.properties.VPropertyBase;

/**
 * REPEAT
 * Repeat Count
 * RFC 5545, 3.8.6.2, page 133
 * 
 * This property defines the number of times the alarm should
 * be repeated, after the initial trigger.
 * 
 * If the alarm triggers more than once, then this property MUST be specified
 * along with the "DURATION" property.
 * 
 * Examples:
 * REPEAT:4
 * DURATION:PT5M
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VAlarm
 */
public class RepeatCount extends VPropertyBase<Integer, RepeatCount>
{
    public RepeatCount(Integer value)
    {
        super(value);
    }
    
    public RepeatCount(RepeatCount source)
    {
        super(source);
    }
    
    public RepeatCount()
    {
        super(0); // default is 0
    }
    
    public static RepeatCount parse(String content)
    {
    	return RepeatCount.parse(new RepeatCount(), content);
    }
    
    @Override
    public void setValue(Integer value)
    {
        if (value >= 0)
        {
            super.setValue(value);
        } else
        {
            throw new IllegalArgumentException(name() + " must be greater than or equal to zero");
        }
    }
}
