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
package jfxtras.icalendarfx.property.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.icalendarfx.properties.component.descriptive.Categories;

public class CategoriesTest
{
    @Test
    public void canParseCategories()
    {
        Categories property = new Categories("group03","g,roup\\;p");
        String expectedContent = "CATEGORIES:group03,g\\,roup\\\\\\;p";
        assertEquals(expectedContent, property.toString());
        Categories property2 = Categories.parse(expectedContent);
        assertEquals(property, property2);
    }
    
    @Test
    public void canParseMultipleCategories()
    {
        Categories property = Categories.parse("CATEGORIES:group03,group04,group05");
        String expectedSummary = "CATEGORIES:group03,group04,group05";
        assertEquals(expectedSummary, property.toString());
        assertEquals(3, property.getValue().size());        
    }
    
    @Test
    public void canParseMultipleCategories2()
    {
        Categories property = Categories.parse("group03,group04,group05");
        String expectedSummary = "CATEGORIES:group03,group04,group05";
        assertEquals(expectedSummary, property.toString());
        assertEquals(3, property.getValue().size());        
    }
    
    @Test
    public void canCopyCategories()
    {
        String content = "group03,group04,group05";
        Categories property1 = Categories.parse(content);
        Categories property2 = new Categories(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
        assertFalse(property1.getValue() == property2.getValue());
        assertEquals("CATEGORIES:" + content, property2.toString());
        
        // make sure wrapped collection is different
        String first = property1.getValue().iterator().next();
        property1.getValue().remove(first);
        assertEquals(2, property1.getValue().size());
        assertEquals(3, property2.getValue().size());
    }
}
