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
package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;

public class DescriptionTest
{
    @Test
    public void canParseDescriptionSimple() throws URISyntaxException
    {
        Description description = Description.parse("this is a simple description without parameters");
        String expectedContentLine = "DESCRIPTION:this is a simple description without parameters";
        String madeContentLine = description.toString();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseDescriptionComplex() throws URISyntaxException
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        Description madeDescription = Description.parse(contentLine);
        Description expectedDescription = Description.parse("Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management")
                .withAlternateText("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en");
        assertEquals(expectedDescription, madeDescription);
        String foldedContent = ICalendarUtilities.foldLine(contentLine).toString();
        assertEquals(foldedContent, expectedDescription.toString());
    }
    
    @Test
    public void canParseDescriptionWithOtherParameters()
    {
        String contentLine = "DESCRIPTION;X-MYPARAMETER=some value;IGNORE ME;X-PARAMETER2=other value:Example description";        
        Description madeDescription = Description.parse(contentLine);
        Description expectedDescription = Description.parse("Example description")
        		.withNonStandard("X-MYPARAMETER=some value", "X-PARAMETER2=other value");
        assertEquals(expectedDescription, madeDescription);
        String foldedContent = ICalendarUtilities.foldLine("DESCRIPTION;X-MYPARAMETER=some value;X-PARAMETER2=other value:Example description").toString();
        assertEquals(foldedContent, expectedDescription.toString());
    }
    
    @Test
    public void canParseEmptyDescription()
    {
        String contentLine = "DESCRIPTION:";
        Description madeDescription = Description.parse(contentLine);
        madeDescription.toString();
        Description expectedDescription = new Description();
        assertEquals(expectedDescription, madeDescription);
        assertEquals("DESCRIPTION:", expectedDescription.toString());
    }
    
    @Test
    public void canBuildEmptyDescription()
    {
        String contentLine = "DESCRIPTION:";
        Description madeDescription = new Description();
        madeDescription.toString();
        assertEquals(contentLine, madeDescription.toString());
        assertNull(madeDescription.getValue());
    }
}
