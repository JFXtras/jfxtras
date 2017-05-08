package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VPrimary;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see StandardTime
 * @see DaylightSavingTime
 * 
 * for the following properties:
 * @see DateTimeStart
 * @see Comment
 * 
 * @author David Bal
 *
 */
public class PrimaryTest
{
    @Test
    public void canBuildPrimary() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VPrimary<?>> components = Arrays.asList(
                new VEvent()
                    .withDateTimeStart("20160306T080000")
                    .withComments("This is a test comment", "Another comment")
                    .withComments("COMMENT:My third comment"),
                new VTodo()
                    .withDateTimeStart("20160306T080000")
                    .withComments("This is a test comment", "Another comment")
                    .withComments("COMMENT:My third comment"),
                new VJournal()
                    .withDateTimeStart("20160306T080000")
                    .withComments("This is a test comment", "Another comment")
                    .withComments("COMMENT:My third comment"),
                new VFreeBusy()
                    .withDateTimeStart("20160306T080000")
                    .withComments("This is a test comment", "Another comment")
                    .withComments("COMMENT:My third comment"),
                new DaylightSavingTime()
                    .withDateTimeStart("20160306T080000")
                    .withComments("This is a test comment", "Another comment")
                    .withComments("COMMENT:My third comment"),
                new StandardTime()
                    .withDateTimeStart("20160306T080000")
                    .withComments("This is a test comment", "Another comment")
                    .withComments("COMMENT:My third comment")
                );
        
        for (VPrimary<?> builtComponent : components)
        {
            String componentName = builtComponent.name();
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "DTSTART:20160306T080000" + System.lineSeparator() +
                    "COMMENT:This is a test comment" + System.lineSeparator() +
                    "COMMENT:Another comment" + System.lineSeparator() +
                    "COMMENT:My third comment" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = (VComponent) builtComponent.getClass()
            		.getMethod("parse", String.class)
            		.invoke(null, expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toString());
            assertEquals(3, builtComponent.getComments().size());
            List<String> expectedComments = Arrays.asList(
                    "COMMENT:This is a test comment",
                    "COMMENT:Another comment",
                    "COMMENT:My third comment");
            List<String> comments = builtComponent.getComments()
            		.stream()
            		.map(c -> c.toString())
            		.collect(Collectors.toList());
			assertEquals(expectedComments, comments);
        }
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canCatchAlreadySet()
    {
        String content = 
            "BEGIN:VEVENT" + System.lineSeparator() +
            "DTSTART:20151109T090000" + System.lineSeparator() +
            "DTSTART:20151119T090000" + System.lineSeparator() +
            "END:VEVENT";
        VEvent v = VEvent.parse(content);
        assertEquals(LocalDateTime.of(2015, 11, 9, 9, 0), v.getDateTimeStart().getValue());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canHandleInvalidPropertyValue()
    {
        String content = 
            "BEGIN:VEVENT" + System.lineSeparator() +
            "DTSTART:INVALID" + System.lineSeparator() +
            "END:VEVENT";
        VEvent e = VEvent.parse(content);
        String expectedContent = 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "END:VEVENT";
        assertEquals(expectedContent, e.toString());
    }

}
