package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.ICalendarTestAbstract;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VTimeZone;

public class VTimeZoneTest
{
    @Test
    public void canBuildVTimeZone() throws InstantiationException, IllegalAccessException
    {
        VTimeZone builtComponent = new VTimeZone()
                .withTimeZoneIdentifier("America/Los_Angeles");

        String componentName = builtComponent.name();            
        String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                "TZID:America/Los_Angeles" + System.lineSeparator() +
                "END:" + componentName;
                
        VComponent parsedComponent = builtComponent.getClass().newInstance();
        parsedComponent.parseContent(expectedContent);

        assertEquals(parsedComponent, builtComponent);
        assertEquals(expectedContent, builtComponent.toContent());            
    }
    
    @Test
    public void canParseVTimeZone()
    {
        String expectedContent = 
            "BEGIN:VTIMEZONE" + System.lineSeparator() +
            "TZID:America/New_York" + System.lineSeparator() +
            "LAST-MODIFIED:20050809T050000Z" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 1
            "DTSTART:19670430T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z" + System.lineSeparator() +            
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:STANDARD" + System.lineSeparator() + // 2
            "DTSTART:19671029T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z" + System.lineSeparator() +
            "TZOFFSETFROM:-0400" + System.lineSeparator() +
            "TZOFFSETTO:-0500" + System.lineSeparator() +
            "TZNAME:EST" + System.lineSeparator() +
            "END:STANDARD" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 3
            "DTSTART:19740106T020000" + System.lineSeparator() +
            "RDATE:19750223T020000" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 4
            "DTSTART:19760425T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 5
            "DTSTART:19870405T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:DAYLIGHT" + System.lineSeparator() + // 6
            "DTSTART:20070311T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU" + System.lineSeparator() +
            "TZOFFSETFROM:-0500" + System.lineSeparator() +
            "TZOFFSETTO:-0400" + System.lineSeparator() +
            "TZNAME:EDT" + System.lineSeparator() +
            "END:DAYLIGHT" + System.lineSeparator() +
            
            "BEGIN:STANDARD" + System.lineSeparator() + // 7
            "DTSTART:20071104T020000" + System.lineSeparator() +
            "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU" + System.lineSeparator() +
            "TZOFFSETFROM:-0400" + System.lineSeparator() +
            "TZOFFSETTO:-0500" + System.lineSeparator() +
            "TZNAME:EST" + System.lineSeparator() +
            "END:STANDARD" + System.lineSeparator() +
            "END:VTIMEZONE";
        VTimeZone component = VTimeZone.parse(expectedContent);
        VTimeZone builtComponent = ICalendarTestAbstract.getTimeZone1();
        component.equals(builtComponent);
        assertEquals(component, builtComponent);
        assertEquals(expectedContent, component.toContent());
        assertEquals(builtComponent.toContent(), component.toContent());
    }
}
