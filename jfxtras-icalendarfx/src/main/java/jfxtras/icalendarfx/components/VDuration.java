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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.components;

import java.time.temporal.TemporalAmount;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/**
 * Interface for {@link DurationProp} property
 * 
 * @author David Bal
 *
 * @param <T>  concrete subclass
 */
public interface VDuration<T> extends VComponent
{
    /*
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    /** Gets the value of the {@link DurationProp} */
    DurationProp getDuration();
    /** Sets the value of the {@link DurationProp} */
    void setDuration(DurationProp duration);
    /** Sets the value of the {@link DurationProp} by parsing iCalendar content text */
    default void setDuration(String duration)
    {
    	setDuration(DurationProp.parse(duration));
    }
    /** Sets the value of the {@link DurationProp} by creating new {@link DurationProp} from the TemporalAmount parameter */
    default void setDuration(TemporalAmount duration)
    {
        setDuration(new DurationProp(duration));
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property } by creating a new {@link DurationProp} from
     * the TemporalAmount parameter</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(TemporalAmount duration)
    {
        setDuration(duration);
        return (T) this;
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property } by parsing iCalendar content text</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(String duration)
    {
        setDuration(duration);
        return (T) this;
    }
    /**
     * <p>Sets the value of the {@link DurationProp} property}</p>
     * 
     * @return - this class for chaining
     */
    default T withDuration(DurationProp duration)
    {
        setDuration(duration);
        return (T) this;
    }
}
