package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class OrdererTest
{
    @Test // can remove a property and avoid null pointer with toContent
    public void canRemoveProperty()
    {
        VEvent vComponent = new VEvent()
                .withSummary("example")
                .withRecurrenceRule("RRULE:FREQ=DAILY");
        vComponent.setRecurrenceRule((RecurrenceRule2) null); // remove Recurrence Rule
        assertEquals(1, vComponent.childrenUnmodifiable().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "SUMMARY:example" + System.lineSeparator() +
                                 "END:VEVENT";
        assertEquals(expectedContent, vComponent.toContent());
    }
    
    @Test // can remove a property and avoid null pointer with toContent
    public void canRemoveListProperty()
    {
        VEvent vComponent = new VEvent()
                .withSummary("example")
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        vComponent.setRecurrenceDates(null); // remove Recurrence Rule
        assertEquals(1, vComponent.childrenUnmodifiable().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "SUMMARY:example" + System.lineSeparator() +
                                 "END:VEVENT";
        assertEquals(expectedContent, vComponent.toContent());
    }
    
    @Test // shows removing null property does nothing
    public void canRemoveEmptyProperty()
    {
        VEvent vComponent = new VEvent();
        vComponent.setRecurrenceRule((RecurrenceRule2) null); // remove null Recurrence Rule
        assertEquals(0, vComponent.childrenUnmodifiable().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "END:VEVENT";
        assertEquals(expectedContent, vComponent.toContent());
    }
}
