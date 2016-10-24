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
