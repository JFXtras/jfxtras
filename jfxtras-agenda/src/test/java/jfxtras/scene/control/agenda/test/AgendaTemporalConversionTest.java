/**
 * AgendaTemporalConversionTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.scene.control.agenda.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import org.junit.Test;

import jfxtras.scene.control.agenda.TemporalUtilities;

/**
 * 
 */
public class AgendaTemporalConversionTest
{
    /*
     * initialTemporal is Temporal from startTemporal or endTemporal
     * adjuster is the argument passed to setStartLocalDateTime and setEndLocalDateTime.
     * adjuster is modified to be LocalDate if Appointment isWholeDay and LocalDateTime otherwise.
     * actual is the value to be put into startTemporal or endTemporal
     */

    @Test
    public void canConvertLocalDateToLocalDate()
    {
        // LocalDate into LocalDate
        {
            Temporal initialTemporal = LocalDate.of(2015, 11, 18);
            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 19);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            assertEquals(LocalDate.of(2015, 11, 19), actual);
        }
    }
    
    @Test
    public void canConvertLocalDateToThaiBuddhistDate()
    {
        // LocalDate into ThaiBuddhistDate
        {
            Temporal initialTemporal = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 18));
            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 19);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 19));
            assertEquals(expected, actual);
        }
    }
    
    @Test
    public void canConvertLocalDateToZonedDateTime()
    {
        // LocalDate into ZonedDateTime
        {
            Temporal initialTemporal = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 5, 0), ZoneId.of("Japan"));
            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 19);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 5, 0), ZoneId.of("Japan"));
            assertEquals(expected, actual);
        }
    }
    
    @Test
    public void canConvertLocalDateToLocalDateTime()
    {
        // LocalDate into LocalDateTime
        {
            Temporal initialTemporal = LocalDateTime.of(2015, 11, 19, 5, 30);
            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 18);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = LocalDateTime.of(2015, 11, 18, 5, 30);
            assertEquals(expected, actual);
        }
    }
    
    @Test
    public void canConvertLocalDateTimeToLocalDateTime()
    {
        // LocalDateTime into LocalDateTime
        {
            Temporal initialTemporal = LocalDateTime.of(2015, 11, 19, 5, 30);
            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 22, 11, 30);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = LocalDateTime.of(2015, 11, 22, 11, 30);
            assertEquals(expected, actual);
        }
    }
    
    @Test
    public void canConvertLocalDateTimeToLocalDate()
    {
        // LocalDateTime into LocalDate
        {
            Temporal initialTemporal = LocalDate.of(2015, 11, 18);
            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 19, 5, 30);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = LocalDateTime.of(2015, 11, 19, 5, 30);
            assertEquals(expected, actual);
        }
    }
    
    @Test
    public void canConvertLocalDateTimeToZonedDateTime()
    {
        // LocalDateTime into ZonedDateTime
        {
            Temporal initialTemporal = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 5, 0), ZoneId.of("Japan"));
            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 19, 5, 30);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 5, 30), ZoneId.of("Japan"));
            assertEquals(expected, actual);
        }
    }
    
    @Test
    public void canConvertLocalDateTimeToThaiBuddhistDate()
    {
        // LocalDateTime into ThaiBuddhistDate
        {
            Temporal initialTemporal = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 18));
            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 19, 5, 30);
            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
            Temporal expected = LocalDateTime.of(2015, 11, 19, 5, 30);
            assertEquals(expected, actual);
        }
    }
}
