package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;

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
//        assertEquals(content, builtComponent.toStringLines());
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
        assertEquals(content, builtComponent.toString());
    }
    
    @Test
    public void canBuildRepeatable()
    {
        DaylightSavingTime builtComponent = new DaylightSavingTime()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                .withRecurrenceRule(new RecurrenceRuleValue()
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
        Set<Temporal> expectedValues = new HashSet<>(Arrays.asList(
                LocalDate.of(1996, 4, 2),
                LocalDate.of(1996, 4, 3),
                LocalDate.of(1996, 4, 4) ));
        RecurrenceDates newChild = new RecurrenceDates(expectedValues);
		builtComponent.getRecurrenceDates().add(newChild);
		builtComponent.orderChild(newChild);
        String content2 = "BEGIN:" + componentName + System.lineSeparator() +
                "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                "RDATE;VALUE=DATE:19960402,19960403,19960404" + System.lineSeparator() +
                "END:" + componentName;
        assertEquals(content2, builtComponent.toString());
    }
    
    @Test
    public void canBuildRepeatable2()
    {
        String componentName = DaylightSavingTime.NAME;
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                "END:" + componentName;

        DaylightSavingTime madeComponent = DaylightSavingTime.parse(content);
        assertEquals(content, madeComponent.toString());
    }
    
    @Test
    public void canCatchDifferentRepeatableTypes()
    {
        DaylightSavingTime builtComponent = new DaylightSavingTime()
                .withRecurrenceDates("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        builtComponent.getRecurrenceDates().add(new RecurrenceDates(ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z"))));
        assertEquals(4, builtComponent.errors().size());
        String expected = "RDATE: DateTimeType DATE_WITH_UTC_TIME doesn't match previous recurrence's DateTimeType DATE";
        boolean isErrorPresent = builtComponent.errors()
        	.stream()
        	.anyMatch(e -> e.equals(expected));
        assertTrue(isErrorPresent);
    }
}
