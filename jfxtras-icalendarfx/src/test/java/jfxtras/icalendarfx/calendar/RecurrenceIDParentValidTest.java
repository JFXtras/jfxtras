package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;

public class RecurrenceIDParentValidTest
{
    @Test (expected = DateTimeException.class)
    public void canCatchWrongRecurrenceIdType()
    {
        Thread.currentThread().setUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        VCalendar c = new VCalendar();
        VEvent parentComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        VEvent childComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016");
        c.addAllVComponents(parentComponent, childComponent);
        childComponent.withRecurrenceId(new RecurrenceId(LocalDateTime.of(2016, 3, 6, 8, 0)));
    }
    
    @Test
    public void canCatchWrongRecurrenceIdType2()
    {
        VCalendar c = new VCalendar();
        VEvent parentComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        VEvent childComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withRecurrenceId(LocalDate.of(1997, 3, 1));
        c.addAllVComponents(parentComponent, childComponent);
        parentComponent.setDateTimeStart(LocalDateTime.of(1997, 4, 1, 8, 0));
        String errorPrefix = PropertyType.RECURRENCE_IDENTIFIER.toString();
        boolean hasError = childComponent.errors().stream()
            .anyMatch(s -> s.substring(0, errorPrefix.length()).equals(errorPrefix));
        assertTrue(hasError);
    }
}
