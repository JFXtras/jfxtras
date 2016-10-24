package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class DaylightSavingsTimeTest
{
//    @Test
//    public void canBuildBase()
//    {        
//        ObjectProperty<String> s = new SimpleObjectProperty<>("start");
//        s.set(null);
//        
//        DaylightSavingTime builtComponent = new DaylightSavingTime()
//                .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
//                .withIANAProperty("TESTPROP2:CASUAL")
//                .withNonStandardProperty("X-TEST-OBJ:testid");
//        builtComponent.propertySortOrder().put("X-ABC-MMSUBJ", 0);
//        builtComponent.propertySortOrder().put("TESTPROP2", 1);
//        builtComponent.propertySortOrder().put("X-TEST-OBJ", 2);
//        String componentName = builtComponent.componentName();
//        
//        String content = "BEGIN:" + componentName + System.lineSeparator() +
//                "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
//                "TESTPROP2:CASUAL" + System.lineSeparator() +
//                "X-TEST-OBJ:testid" + System.lineSeparator() +
//                "END:" + componentName;
//                
//        DaylightSavingTime madeComponent = new DaylightSavingTime(content);
//        assertEquals(madeComponent, builtComponent);
//        assertEquals(content, builtComponent.toContentLines());
//    }
    
    @Test
    public void canBuildPrimary()
    {
        DaylightSavingTime builtComponent = new DaylightSavingTime()
                .withDateTimeStart("20160306T080000Z")
                .withComments("This is a test comment", "Another comment")
                .withComments("COMMENT:My third comment");
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DTSTART:20160306T080000Z" + System.lineSeparator() +
                "COMMENT:This is a test comment" + System.lineSeparator() +
                "COMMENT:Another comment" + System.lineSeparator() +
                "COMMENT:My third comment" + System.lineSeparator() +
                "END:" + componentName;
                
        DaylightSavingTime madeComponent = DaylightSavingTime.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContent());
    }
    
    @Test
    public void canBuildRepeatable()
    {
        DaylightSavingTime builtComponent = new DaylightSavingTime()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.DAILY)
                    .withInterval(4));
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                "END:" + componentName;

        DaylightSavingTime madeComponent = DaylightSavingTime.parse(content);
        assertEquals(madeComponent, builtComponent);
        
        // add another set of recurrences
        ObservableSet<Temporal> expectedValues = FXCollections.observableSet(
                LocalDate.of(1996, 4, 2),
                LocalDate.of(1996, 4, 3),
                LocalDate.of(1996, 4, 4) );        
        builtComponent.getRecurrenceDates().add(new RecurrenceDates(expectedValues));
        String content2 = "BEGIN:" + componentName + System.lineSeparator() +
                "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                "RDATE;VALUE=DATE:19960402,19960403,19960404" + System.lineSeparator() +
                "END:" + componentName;
        assertEquals(content2, builtComponent.toContent());
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchDifferentRepeatableTypes()
    {
        Thread.currentThread().setUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        DaylightSavingTime builtComponent = new DaylightSavingTime()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        ObservableSet<Temporal> expectedValues = FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) );        
        builtComponent.getRecurrenceDates().add(new RecurrenceDates(expectedValues));
    }
}
