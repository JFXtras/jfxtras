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
package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

public class VFreeBusyTest
{
    @Test
    public void canBuildVFreeBusy()
    {
        VFreeBusy builtComponent = new VFreeBusy()
                .withContact("CONTACT:Harry Potter\\, Hogwarts\\, by owl")
                .withDateTimeEnd(LocalDate.of(2016, 4, 26))
                .withFreeBusyTime("FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M");
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "CONTACT:Harry Potter\\, Hogwarts\\, by owl" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20160426" + System.lineSeparator() +
                "FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M" + System.lineSeparator() +
                "END:" + componentName;
        
        VFreeBusy madeComponent = VFreeBusy.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toString());
        
        builtComponent.setDateTimeStamp(DateTimeStamp.parse("20160210T100000Z"));
        builtComponent.setUniqueIdentifier("66761d56-d248-4c12-a807-350e95abea66");
        builtComponent.setDateTimeStart(new DateTimeStart(LocalDate.of(2016, 4, 25)));
        assertTrue(builtComponent.isValid());
    }
}
