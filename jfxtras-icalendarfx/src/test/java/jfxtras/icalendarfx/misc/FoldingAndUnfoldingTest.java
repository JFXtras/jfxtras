package jfxtras.icalendarfx.misc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;

public class FoldingAndUnfoldingTest
{
    @Test
    public void canUnfoldLine()
    {
        String line = "COMMENT:Ek and Lorentzon said they would consider halting investment at th,eir headquarters in Stockholm. The pioneering music streaming company employs about 850 people in the city, and more than 1,000 in nearly 30 other offices around the world.";
        VEvent builtComponent = new VEvent()
                .withComments(line);
        String content = builtComponent.toContent();
        String foldedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                                 "COMMENT:Ek and Lorentzon said they would consider halting investment at th" + System.lineSeparator() +
                                 " \\,eir headquarters in Stockholm. The pioneering music streaming company em" + System.lineSeparator() +
                                 " ploys about 850 people in the city\\, and more than 1\\,000 in nearly 30 oth" + System.lineSeparator() +
                                 " er offices around the world." + System.lineSeparator() +
                                 "END:VEVENT";
        VEvent unfoldedComponent = VEvent.parse(foldedContent);
        assertEquals(builtComponent, unfoldedComponent);
        assertEquals(foldedContent, builtComponent.toContent());
    }
}
