package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * 
 * for the following properties:
 * @see Description
 * @see GeographicPosition
 * @see DurationProp
 * @see Location
 * @see Priority
 * @see Resources
 * 
 * @author David Bal
 *
 */
public class LocatableTest
{
    @Test
    public void canBuildLocatable() throws InstantiationException, IllegalAccessException
    {
        List<VLocatable<?>> components = Arrays.asList(
                new VEvent()
                    .withDescription("DESCRIPTION:A simple description")
                    .withDuration(Duration.ofMinutes(45))
                    .withGeographicPosition("37.386013;-122.082932")
                    .withLocation("Antarctica")
                    .withPriority(2)
                    .withResources(new Resources("Nettoyeur haute pression")
                            .withLanguage("fr"))
                    .withVAlarms(VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                                     "ACTION:DISPLAY" + System.lineSeparator() +
                                     "DESCRIPTION:Test alarm" + System.lineSeparator() +
                                     "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
                                     "END:VALARM"),
                                 VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                                     "TRIGGER;VALUE=DATE-TIME:19970317T133000Z" + System.lineSeparator() +
                                     "REPEAT:4" + System.lineSeparator() +
                                     "DURATION:PT15M" + System.lineSeparator() +
                                     "ACTION:AUDIO" + System.lineSeparator() +
                                     "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud" + System.lineSeparator() +
                                     "END:VALARM")),
                new VTodo()
                    .withDescription("DESCRIPTION:A simple description")
                    .withDuration(Duration.ofMinutes(45))
                    .withGeographicPosition("37.386013;-122.082932")
                    .withLocation("Antarctica")
                    .withPriority(2)
                    .withResources(new Resources("Nettoyeur haute pression")
                            .withLanguage("fr"))
                    .withVAlarms(VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                            "ACTION:DISPLAY" + System.lineSeparator() +
                            "DESCRIPTION:Test alarm" + System.lineSeparator() +
                            "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
                            "END:VALARM"),
                        VAlarm.parse("BEGIN:VALARM" + System.lineSeparator() +
                            "TRIGGER;VALUE=DATE-TIME:19970317T133000Z" + System.lineSeparator() +
                            "REPEAT:4" + System.lineSeparator() +
                            "DURATION:PT15M" + System.lineSeparator() +
                            "ACTION:AUDIO" + System.lineSeparator() +
                            "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud" + System.lineSeparator() +
                            "END:VALARM"))
                );
        
        for (VLocatable<?> builtComponent : components)
        {
            String componentName = builtComponent.name();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "DESCRIPTION:A simple description" + System.lineSeparator() +
                    "DURATION:PT45M" + System.lineSeparator() +
                    "GEO:37.386013;-122.082932" + System.lineSeparator() +
                    "LOCATION:Antarctica" + System.lineSeparator() +
                    "PRIORITY:2" + System.lineSeparator() +
                    "RESOURCES;LANGUAGE=fr:Nettoyeur haute pression" + System.lineSeparator() +
                    "BEGIN:VALARM" + System.lineSeparator() +
                    "ACTION:DISPLAY" + System.lineSeparator() +
                    "DESCRIPTION:Test alarm" + System.lineSeparator() +
                    "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
                    "END:VALARM" + System.lineSeparator() +
                    "BEGIN:VALARM" + System.lineSeparator() +
                    "TRIGGER;VALUE=DATE-TIME:19970317T133000Z" + System.lineSeparator() +
                    "REPEAT:4" + System.lineSeparator() +
                    "DURATION:PT15M" + System.lineSeparator() +
                    "ACTION:AUDIO" + System.lineSeparator() +
                    "ATTACH;FMTTYPE=audio/basic:ftp://example.com/pub/sounds/bell-01.aud" + System.lineSeparator() +
                    "END:VALARM" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.parseContent(expectedContent);

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());            
        }
    }
}
