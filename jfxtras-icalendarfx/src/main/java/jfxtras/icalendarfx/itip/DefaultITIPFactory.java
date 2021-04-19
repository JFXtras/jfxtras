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
package jfxtras.icalendarfx.itip;

import jfxtras.icalendarfx.properties.calendar.Method.MethodType;

/**
 * Default iTIP process method factory that supports one calendar user - just an organizer, no attendees.
 * 
 * The following methods are implemented
 * <ul>
 * <li>PUBLISH
 * <li>REQUEST
 * <li>CANCEL
 * </ul>
 * 
 * 
 * @author David Bal
 *
 */
public class DefaultITIPFactory extends AbstractITIPFactory
{

    @Override
    public Processable getITIPMessageProcess(MethodType methodType)
    {
        switch (methodType)
        {
        case ADD:
            break;
        case CANCEL:
            return new ProcessCancel();
        case COUNTER:
            break;
        case DECLINECOUNTER:
            break;
        case PUBLISH:
            return new ProcessPublish();
        case REFRESH:
            break;
        case REPLY:
            break;
        case REQUEST:
            return new ProcessRequest();
        default:
            break;        
        }
        throw new RuntimeException("not implemented:" + methodType);
    }
}
