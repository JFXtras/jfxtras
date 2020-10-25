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
package jfxtras.scene.control.agenda.icalendar.factories;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.components.VDisplayable;

/**
 * Abstract factory to create recurrences from VComponents.
 * {@link #getStartRange()} and {@link #getEndRange()} must be set before recurrences can be made.
 * 
 * @author David Bal
 *
 * @param <R> - type of recurrences
 */
public abstract class RecurrenceFactory<R>
{
    private ObjectProperty<LocalDateTime> startRange = new SimpleObjectProperty<LocalDateTime>(); // must be updated when range changes
    /** Property for start of range to make recurrences */
    public ObjectProperty<LocalDateTime> startRangeProperty() { return startRange; }
    /** set start of range to make recurrences */
    public void setStartRange(LocalDateTime startRange) { this.startRange.set(startRange); }
    /** get start of range to make recurrences */
    public LocalDateTime getStartRange() { return startRange.get(); }

    private ObjectProperty<LocalDateTime> endRange = new SimpleObjectProperty<LocalDateTime>(); // must be updated when range changes
    /** Property for end of range to make recurrences */
    public ObjectProperty<LocalDateTime> endRangeProperty() { return endRange; }
    /** set end of range to make recurrences */
    public void setEndRange(LocalDateTime endRange) { this.endRange.set(endRange); }
    /** get end of range to make recurrences */
    public LocalDateTime getEndRange() { return endRange.get(); }
    
    /**
     * Makes recurrences from a {@link VDisplayable}
     * Recurrences are made between {@link #getStartRange()} and {@link #getEndRange()}
     * 
     * @param vComponent - calendar component
     * @return created appointments
     */
    public List<R> makeRecurrences(VDisplayable<?> vComponent)
    {
        if ((getStartRange() == null) || (getEndRange() == null))
        {
            throw new DateTimeException("Both startRange and endRange MUST NOT be null (" + startRange + ", " + endRange + ")");
        }
        List<R> newRecurrences = new ArrayList<>();
        Boolean isWholeDay = vComponent.getDateTimeStart().getValue() instanceof LocalDate;
        
        // Make start and end ranges in Temporal type that matches DTSTART
        final Temporal startRange2;
        final Temporal endRange2;
        if (isWholeDay)
        {
            startRange2 = LocalDate.from(getStartRange());
            endRange2 = LocalDate.from(getEndRange());            
        } else
        {
            startRange2 = vComponent.getDateTimeStart().getValue().with(getStartRange());
            endRange2 = vComponent.getDateTimeStart().getValue().with(getEndRange());            
        }
        // make recurrences
//        vComponent.streamRecurrences(startRange2, endRange2) // Gradle won't compile with this line.  I don't understand why.
        Stream<Temporal> streamRecurrences = vComponent.streamRecurrences(startRange2, endRange2);
        streamRecurrences
            .forEach(startTemporal -> 
            {
                R recurrence = makeRecurrence(vComponent, startTemporal);
                newRecurrences.add(recurrence);
            });
        return newRecurrences;
    }
    
    /** Strategy to make Recurrence from VComponent and start Temporal */
    abstract R makeRecurrence(VDisplayable<?> vComponent, Temporal startTemporal);
}
