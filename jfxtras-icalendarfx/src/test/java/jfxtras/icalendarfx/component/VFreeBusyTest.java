package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

public class VFreeBusyTest
{
    @Test
    public void canBuildVFreeBusy()
    {
        VFreeBusy builtComponent = new VFreeBusy()
                .withContact("CONTACT:Harry Potter\\, Hogwarts\\, by owl")
                .withDateTimeEnd(LocalDate.of(2016, 4, 26))
                .withFreeBusyTime("FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M");
        String componentName = builtComponent.name();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "CONTACT:Harry Potter\\, Hogwarts\\, by owl" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20160426" + System.lineSeparator() +
                "FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M" + System.lineSeparator() +
                "END:" + componentName;
        
        VFreeBusy madeComponent = VFreeBusy.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContent());
        
        builtComponent.setDateTimeStamp(DateTimeStamp.parse("20160210T100000Z"));
        builtComponent.setUniqueIdentifier("66761d56-d248-4c12-a807-350e95abea66");
        builtComponent.setDateTimeStart(new DateTimeStart(LocalDate.of(2016, 4, 25)));
        assertTrue(builtComponent.isValid());
    }
}
