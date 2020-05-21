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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.properties.component.relationship;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.component.relationship.UniqueIdentifier;

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
public class UniqueIdentifier extends VPropertyBase<String, UniqueIdentifier>
{
    public UniqueIdentifier(UniqueIdentifier source)
    {
        super(source);
    }
    
    public UniqueIdentifier()
    {
        super();
    }

    public static UniqueIdentifier parse(String content)
    {
    	return UniqueIdentifier.parse(new UniqueIdentifier(), content);
    }
}
