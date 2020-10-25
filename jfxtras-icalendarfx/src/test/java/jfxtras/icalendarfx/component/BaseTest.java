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
package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 * @see VFreeBusy
 * @see VTimeZone
 * @see StandardTime
 * @see DaylightSavingTime
 * 
 * for the following properties:
 * @see NonStandardProperty
 * 
 * @author David Bal
 *
 */
public class BaseTest
{
    @Test
    public void canBuildBase() throws InstantiationException, IllegalAccessException
    {
        List<VComponentBase> components = Arrays.asList(
                new VEvent()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VTodo()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VJournal()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VFreeBusy()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VAlarm()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VTimeZone()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new DaylightSavingTime()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new StandardTime()
                    .withNonStandard(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withNonStandard(NonStandardProperty.parse("X-TEST-OBJ:testid"))
                );
        
        for (VComponentBase builtComponent : components)
        {
            String componentName = builtComponent.name();
            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
                    "X-TEST-OBJ:testid" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.addChild(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toString());            
        }
    }
}
