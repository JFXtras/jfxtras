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
package jfxtras.icalendarfx.components;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDescribable;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;

/**
 * For single DESCRIPTION property
 * Note: Not for VJournal - allows multiple descriptions
 * 
 * <p>{@link VComponent} with the following properties
 * <ul>
 * <li>{@link Description DESCRIPTION}
 * </ul>
 * </p>
 * 
 * @author David Bal
 */
public interface VDescribable2<T> extends VDescribable<T>
{
    /**
     * <p>This property provides a more complete description of the
     * calendar component than that provided by the {@link Summary} property.<br>
     * RFC 5545 iCalendar 3.8.1.5. page 84</p>
     * 
     * <p>Example:
     * <ul>
     * <li>DESCRIPTION:Meeting to provide technical review for "Phoenix"<br>
     *  design.\nHappy Face Conference Room. Phoenix design team<br>
     *  MUST attend this meeting.\nRSVP to team leader.
     * </ul>
     *
     * <p>Note: Only {@link VJournal} allows multiple instances of DESCRIPTION</p>
     */
    Description getDescription();
    void setDescription(Description description);
    default void setDescription(String description)
    {
    	setDescription(Description.parse(description));
	}
    /**
     * Sets the value of the {@link Description}
     * 
     * @return - this class for chaining
     */
    default T withDescription(Description description)
    {
        setDescription(description);
        return (T) this;
    }
    /**
     * Sets the value of the {@link Description} by parsing iCalendar text
     * 
     * @return - this class for chaining
     */
    default T withDescription(String description)
    {
        setDescription(description);
        return (T) this;
    }
}
