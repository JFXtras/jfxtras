package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.VPropertyElement;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;

public class RecurrenceIDParentValidTest
{
    @Test
    public void canCatchWrongRecurrenceIdType()
    {
        VCalendar c = new VCalendar();
        VEvent parentComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        VEvent childComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016");
        c.addChild(parentComponent);
        c.addChild(childComponent);
        childComponent.withRecurrenceId(new RecurrenceId(LocalDateTime.of(2016, 3, 6, 8, 0)));
        String expectedError = "RECURRENCE-ID:RecurrenceId DateTimeType (DATE_WITH_LOCAL_TIME) must be same as the type of its parent's DateTimeStart (DATE)";
        boolean isErrorPresent = c.errors().stream().anyMatch(e -> e.equals(expectedError));
        assertTrue(isErrorPresent);
    }
    
    @Test
    public void canCatchWrongRecurrenceIdType2()
    {
        VEvent parentComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        VEvent childComponent = new VEvent()
                .withUniqueIdentifier("testRecurrenceID08242016")
                .withRecurrenceId(LocalDate.of(1997, 3, 1));
        VCalendar c = new VCalendar()
        		.withVEvents(parentComponent, childComponent);
        parentComponent.setDateTimeStart(LocalDateTime.of(1997, 4, 1, 8, 0));
        String errorPrefix = VPropertyElement.RECURRENCE_IDENTIFIER.toString();
        boolean hasError = childComponent.errors().stream()
            .anyMatch(s -> s.substring(0, errorPrefix.length()).equals(errorPrefix));
        assertTrue(hasError);
    }
}
