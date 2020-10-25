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
package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class RequestStatusTest
{
    @Test
    @Ignore // TBEERNOT see what is going wrong here
    public void canParseRequestStatus()
    {
        String content = "REQUEST-STATUS:2.0;Success";
        RequestStatus madeProperty = RequestStatus.parse(content);
        assertEquals(content, madeProperty.toString());
        RequestStatus expectedProperty = new RequestStatus()
                .withStatusCode(2.0)
                .withDescription("Success");
        assertEquals(expectedProperty, madeProperty);
        
        madeProperty.setStatusCode(2.81);
        assertEquals("REQUEST-STATUS:2.81;Success", madeProperty.toString());
        
        madeProperty.setValue("3.7;Invalid calendar user;ATTENDEE:mailto:jsmith@example.com");
        assertEquals((Double) 3.7, madeProperty.getStatusCode());
        assertEquals("Invalid calendar user", madeProperty.getDescription());
        assertEquals("ATTENDEE:mailto:jsmith@example.com", madeProperty.getException());
    }
    
    @Test
    @Ignore // TBEERNOT see what is going wrong here
    public void canParseRequestStatus2()
    {
        String content = "REQUEST-STATUS:2.8;Success\\, repeating event ignored. Scheduled as a single event.;RRULE:FREQ=WEEKLY\\;INTERVAL=2";
        RequestStatus madeProperty = RequestStatus.parse(content);
        assertEquals(ICalendarUtilities.foldLine(content).toString(), madeProperty.toString());
        RequestStatus expectedProperty = new RequestStatus()
                .withStatusCode(2.8)
                .withDescription("Success, repeating event ignored. Scheduled as a single event.")
                .withException("RRULE:FREQ=WEEKLY;INTERVAL=2");
        assertEquals(expectedProperty, madeProperty);
    }
}
